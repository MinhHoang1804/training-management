package com.g96.ftms.service.feedback.feedback;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.FeedBackResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.feedback.IFeedBackService;
import com.g96.ftms.util.SqlBuilderUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedBackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final FeedBackAnswerRepository feedBackAnswerRepository;
    private final QuestionsRepository questionsRepository;

    @Override
    public ApiResponse<PagedResponse<FeedBackResponse.FeedBackInfoDTO>> search(FeedBackRequest.FeedBackPagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<FeedBack> pages = feedBackRepository.searchFilter(keywordFilter, model.getUserId(), model.getSubjectId(), model.getClassId(), model.getPageable());
        List<FeedBackResponse.FeedBackInfoDTO> list = pages.getContent().stream().map(f -> {
            return FeedBackResponse
                    .FeedBackInfoDTO.builder().feedbackId(f.getFeedbackId())
                    .userId(f.getUser().getUserId()).traineeName(f.getUser().getFullName())
                    .subjectCode(f.getSubject().getSubjectCode()).avgRating(f.getAvgRating())
                    .openDate(f.getOpenTime()).lastUpdate(f.getLastUpdateTime())
                    .build();
        }).collect(Collectors.toList());
        PagedResponse<FeedBackResponse.FeedBackInfoDTO> response = new PagedResponse<>(list, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<FeedBackResponse.FeedBackFormDTO> getFeedBackFormDetail(FeedBackRequest.FeedBackDetailFormRequest model) {
        // Tìm kiếm Feedback từ feedbackId
        FeedBack feedback = feedBackRepository.findById(model.getFeedBackId())
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found for ID: " + model.getFeedBackId()));

        // Chuyển đổi danh sách câu trả lời thành QuestionAnswerFormInfoDTO
        List<FeedBackResponse.QuestionAnswerFormInfoDTO> questionAnswerForm = feedback.getFeedbackAnswerList().stream()
                .map(this::mapToQuestionAnswerFormInfoDTO)
                .collect(Collectors.toList());

        //get trainer
        Class classs = feedback.getClasss();
        Schedule schedule = classs.getSchedules().stream().filter(s -> s.getSubject().getSubjectId() == feedback.getSubject().getSubjectId()).findFirst().orElse(null);
        User trainer = null;
        if(schedule != null&& !StringUtils.isEmpty(schedule.getTrainer())) {
            trainer=userRepository.findByAccount(schedule.getTrainer());
        }
        if (trainer == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        //get admin
        User admin = userRepository.findByAccount(classs.getAdmin());
        if (admin == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        // Tạo đối tượng FeedBackFormDTO với các trường cần thiết
        FeedBackResponse.FeedBackFormDTO response = FeedBackResponse.FeedBackFormDTO.builder()
                .feedbackId(feedback.getFeedbackId())
                .traineeName(feedback.getUser().getFullName())
                .traineeId(feedback.getUser().getUserId())
                .trainerName(trainer.getFullName())
                .trainerId(trainer.getUserId())
                .adminName(admin.getAccount())
                .subjectCode(feedback.getSubject().getSubjectCode())
                .subjectName(feedback.getSubject().getSubjectName())
                .avgRating(feedback.getAvgRating())
                .openDate(feedback.getOpenTime())
                .description(feedback.getDescription())
                .endDate(feedback.getLastUpdateTime())
                .questionAnswerForm(questionAnswerForm)
                .build();

        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);

    }

    @Override
    @Transactional
    public ApiResponse<FeedBack> createFeedBack(FeedBackRequest.FeedBackAddRequest model) {
        //create feed back
        Subject subject = subjectRepository.findById(model.getSubjectId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        User user = userRepository.findById(model.getUserId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));
        Class aClass = classRepository.findById(model.getClassId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        //check exist
        boolean isExist = feedBackRepository.existsByUser_UserIdAndClasss_ClassIdAndSubject_SubjectId(user.getUserId(), model.getClassId(), model.getSubjectId());
        if (isExist) throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_FEEDBACK_EXIST);
        FeedBack feedBack = FeedBack.builder()
                .subject(subject).user(user).classs(aClass)
                .description(model.getDescription())
                .openTime(model.getOpenTime())
                .feedbackDate(model.getFeedBackDate())
                .build();
        feedBackRepository.save(feedBack);
        //save feedback
        List<FeedbackAnswer> listFeedBack = new ArrayList<>();
        for (FeedBackRequest.QuestionAnswerFormRequest qa : model.getListAnswers()) {
            Questions questions = questionsRepository.findById(qa.getQuestionId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.QUESTION_NOT_FOUND));
            FeedbackAnswer feedbackAnswer = fillAnswerFeedBack(questions, feedBack, qa.getAnswer());
            listFeedBack.add(feedbackAnswer);
        }
        feedBackAnswerRepository.saveAll(listFeedBack);

        //update avg setting;
        Double averageRatingByFeedbackId = feedBackAnswerRepository.findAverageRatingByFeedbackId(feedBack.getFeedbackId());
        feedBack.setAvgRating(averageRatingByFeedbackId);
        feedBackRepository.save(feedBack);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), feedBack);
    }

    @Override
    @Transactional
    public ApiResponse<FeedBack> updateFeedBack(FeedBackRequest.FeedBackEditRequest model) {
        FeedBack feedBack = feedBackRepository.findById(model.getFeedBackId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.FEEDBACK_NOT_FOUND));
        //update feedback property
        feedBack.setDescription(model.getDescription());
        FeedBack fSave = feedBackRepository.save(feedBack);
        //remove all feedback answer;
        feedBackAnswerRepository.deleteByFeedback_FeedbackId(fSave.getFeedbackId());
        //update new feedback answer
        List<FeedbackAnswer> listFeedBack = new ArrayList<>();
        for (FeedBackRequest.QuestionAnswerFormRequest qa : model.getListAnswers()) {
            Questions questions = questionsRepository.findById(qa.getQuestionId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.QUESTION_NOT_FOUND));
            FeedbackAnswer feedbackAnswer = fillAnswerFeedBack(questions, fSave, qa.getAnswer());
            listFeedBack.add(feedbackAnswer);
        }
        feedBackAnswerRepository.saveAll(listFeedBack);
        //save feedback avg
        Double averageRatingByFeedbackId = feedBackAnswerRepository.findAverageRatingByFeedbackId(feedBack.getFeedbackId());
        feedBack.setAvgRating(averageRatingByFeedbackId);
        feedBackRepository.save(feedBack);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), fSave);
    }

    @Override
    public ApiResponse<FeedBackResponse.FeedBackCheckResponse> checkFeedBackSubject(FeedBackRequest.FeedBackCheckRequest model) {
        //create feed back
        Subject subject = subjectRepository.findById(model.getSubjectId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        User user = userRepository.findById(model.getUserId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));
        Class aClass = classRepository.findById(model.getClassId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        FeedBackResponse.FeedBackCheckResponse response = FeedBackResponse.FeedBackCheckResponse.builder()
                .isOpenFeedBack(false)
                .isExistFeedBack(false)
                .build();

        if (!checkClassIntime(aClass)) { //expired time
            response.setIsOpenFeedBack(true);
        }
        boolean isExist = feedBackRepository.existsByUser_UserIdAndClasss_ClassIdAndSubject_SubjectId(user.getUserId(), aClass.getClassId(), subject.getSubjectId());
        if (isExist) {
            response.setIsExistFeedBack(true);
            Optional<FeedBack> fb = feedBackRepository.findFirstByUser_UserIdAndClasss_ClassIdAndSubject_SubjectId(user.getUserId(), aClass.getClassId(), subject.getSubjectId());
            fb.ifPresent(feedBack -> response.setFeedBackId(feedBack.getFeedbackId()));
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    public Boolean checkClassIntime(Class classs) {
        if (classs.getEndDate() == null) return false;
        if (classs.getEndDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    private FeedBackResponse.QuestionAnswerFormInfoDTO mapToQuestionAnswerFormInfoDTO(FeedbackAnswer feedbackAnswer) {
        // Xác định câu trả lời dựa trên questionType
        String answer;
        switch (feedbackAnswer.getQuestion().getQuestionType()) {
            case RATING:
                answer = feedbackAnswer.getRating() != null ? feedbackAnswer.getRating().toString() : "No Answer";
                break;
            case YES_NO:
                answer = feedbackAnswer.getYesNoAnswer() != null ? feedbackAnswer.getYesNoAnswer().toString() : "No Answer";
                break;
            case TEXT:
                answer = feedbackAnswer.getTextAnswer() != null ? feedbackAnswer.getTextAnswer() : "No Answer";
                break;
            default:
                answer = "Unknown Type";
                break;
        }

        // Tạo QuestionAnswerFormInfoDTO với câu hỏi và câu trả lời
        return FeedBackResponse.QuestionAnswerFormInfoDTO.builder()
                .questions(feedbackAnswer.getQuestion())
                .answer(answer)
                .build();
    }

    private FeedbackAnswer fillAnswerFeedBack(Questions question, FeedBack feedBack, String answer) {
        // Khởi tạo khóa chính tổng hợp
        FeedbackAnswerRelationId id = new FeedbackAnswerRelationId();
        id.setQuestionId(question.getQuestionId());
        id.setFeedbackId(feedBack.getFeedbackId());

        // Khởi tạo FeedbackAnswer với khóa chính
        FeedbackAnswer feedbackAnswer = FeedbackAnswer.builder()
                .id(id)
                .feedback(feedBack)
                .question(question)
                .build();

        // Xử lý giá trị câu trả lời dựa trên loại câu hỏi
        switch (question.getQuestionType()) {
            case RATING:
                feedbackAnswer.setRating(Double.parseDouble(answer));
                break;
            case YES_NO:
                feedbackAnswer.setYesNoAnswer(Boolean.parseBoolean(answer));
                break;
            case TEXT:
                feedbackAnswer.setTextAnswer(answer);
                break;
            default:
                throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.QUESTION_TYPE_WRONG_FORMAT);
        }

        return feedbackAnswer;
    }

}
