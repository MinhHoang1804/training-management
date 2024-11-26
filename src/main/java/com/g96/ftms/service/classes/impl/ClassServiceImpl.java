package com.g96.ftms.service.classes.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.request.TrainerRequest;
import com.g96.ftms.dto.response.*;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.classes.IClassService;
import com.g96.ftms.service.email.EmailService;
import com.g96.ftms.service.schedule.IScheduleService;
import com.g96.ftms.util.SqlBuilderUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements IClassService {
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final SubjectRepository subjectRepository;
    private final ScheduleRepository scheduleRepository;
    private final LocationRepository locationRepository;
    private final CurriculumRepository curriculumRepository;
    private final IScheduleService scheduleService;
    private final GenerationRepository generationRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final EmailService emailService;

    @Override
    public ApiResponse<PagedResponse<ClassReponse.ClassInforDTO>> search(ClassRequest.ClassPagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<Class> pages = classRepository.searchFilter(keywordFilter, model.getStatus(), model.getPageable());
        List<ClassReponse.ClassInforDTO> collect = pages.getContent().stream().map(s -> {
            return mapper.map(s, ClassReponse.ClassInforDTO.class);
        }).collect(Collectors.toList());
        PagedResponse<ClassReponse.ClassInforDTO> response = new PagedResponse<>(collect, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<ClassReponse.ClassInforDTO> getClassDetail(Long classId) {
        if (classId == null || classId <= 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT);
        }

        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));

        ClassReponse.ClassInforDTO response = mapper.map(c, ClassReponse.ClassInforDTO.class);
        response.setCurriculumName(c.getCurriculum().getCurriculumName());
        response.setLocationName(c.getLocation().getLocationName());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<ClassReponse.ClassInforDTO> getClassForTrainee(Long classId) {
        return null;
    }

    @Override
    @Transactional
    public ApiResponse<?> addClass(ClassRequest.ClassAddRequest model) {

        // check admin exist
        User user = userRepository.findByAccount(model.getAdmin());
        if (user == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }

        Class map = mapper.map(model, Class.class);
        map.setStatus(false); //send mail for cadmin

        Curriculum curriculum = curriculumRepository.findById(model.getCurriculumId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CURRICULUM_NOT_FOUND));
        map.setCurriculum(curriculum);
        //save entity
        Class classSave = classRepository.save(map);

        try {
            emailService.sendMailForCreateClassRequest("Admin", user.getEmail(), user.getFullName(), map.getClassCode(), map.getClassId());
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        //create schedule
//        List<Subject> subjectsInCurriculum = subjectRepository.findDistinctByCurriculumSubjectRelationList_Curriculum_CurriculumId(model.getCurriculumId());
//        List<Schedule> scheduleList = new ArrayList<>();
//        for (Subject subject : subjectsInCurriculum) {
//            Schedule schedule = Schedule.builder().startDate(model.getStartDate()).endDate(model.getEndDate()).status(true)
//                    .classs(map).subject(subject).location(location).trainer(user.getAccount()).description(model.getDescription()).build();
//            scheduleList.add(schedule);
//        }
        //save scheduleList
//        scheduleRepository.saveAll(scheduleList);
        //generate schedule
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), classSave);
    }

    @Override
    public ApiResponse<List<TrainerResponse.TrainerInfoDTO>> getTrainerForClass(TrainerRequest.TrainerForClassRequest model) {
        //get list trainer inrange
        List<String> traineeUnAvailable = scheduleRepository.findTraineesInRangeAndSlot(model.getSlot(), model.getStartDate(), model.getEndDate());

        List<User> roleTrainer = userRepository.findByRoleAvail("ROLE_TRAINER", traineeUnAvailable);
        List<TrainerResponse.TrainerInfoDTO> trainerList = mapper.map(roleTrainer, new TypeToken<List<TrainerResponse.TrainerInfoDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), trainerList);
    }

    @Override
    @Transactional
    public ApiResponse<?> updateClassByAdmin(ClassRequest.UpdateClassByAdminForm model) {
        Class c = classRepository.findById(model.getClassId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        User user = userRepository.findByAccount(c.getAdmin());
        Generation generation = generationRepository.findById(model.getGenerationId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.GENERATION_NOT_FOUND));
        Location location = locationRepository.findById(model.getLocationId()).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.LOCATION_NOT_FOUND));
        c.setGeneration(generation);
        c.setLocation(location);
        classRepository.save(c);
        //check location Exist
        //save class

        //create schedule
        List<Subject> subjectsInCurriculum = subjectRepository.findDistinctByCurriculumSubjectRelationList_Curriculum_CurriculumId(model.getCurriculumId());
        List<Schedule> scheduleList = new ArrayList<>();
        for (Subject subject : subjectsInCurriculum) {

            ClassRequest.SubjectTraineeDto cst = model.getSubjectList().stream()
                    .filter(a -> subject.getSubjectId().equals(a.getSubjectId())) // Replace `subjectId` with the desired ID to match
                    .findFirst()
                    .orElse(null); // Return null if no match is found
            if (cst != null) {
                Schedule schedule = Schedule.builder().startDate(model.getStartDate()).endDate(model.getEndDate()).status(true).slot(cst.getSlot())
                        .classs(c).subject(subject).location(location).trainer(cst.getTrainer()).description(model.getDescription()).build();
                scheduleList.add(schedule);
            }
        }
//        save scheduleList
        List<Schedule> schedules = scheduleRepository.saveAll(scheduleList);

        //save schedule detail;
        List<ClassRequest.SubjectSessionDto> subjectSessionList = model.getSubjectSessionList();
        List<ScheduleDetail> scheduleDetailList = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ClassRequest.SubjectSessionDto subjectSessionDto = subjectSessionList.stream().filter(s -> s.getSubjectId() == schedule.getSubject().getSubjectId()).findFirst().orElse(null);
            if (subjectSessionDto != null) {
                List<ScheduleDetail> list = subjectSessionDto.getSessionList().stream().map(s -> {
                    ScheduleDetail scheduleDetail = ScheduleDetail.builder()
                            .sessionId(s.getSessionId())
                            .schedule(schedule)
                            .date(s.getDate())
                            .lesson(s.getLesson())
                            .status(false)
                            .startTime(s.getStartDate())
                            .endTime(s.getEndDate())
                            .slot(schedule.getSlot())
                            .description(s.getDescription())
                            .build();
                    return scheduleDetail;
                }).toList();
                scheduleDetailList.addAll(list);
            }
        }
        scheduleDetailRepository.saveAll(scheduleDetailList);
        try {
            emailService.sendMailToAcceptRequest("Admin", user.getEmail(), user.getFullName(), c.getClassCode(), c.getClassId());
        } catch (Exception E) {
            E.printStackTrace();
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), c);
    }

    @Override
    public ApiResponse<?> acceptClass(ClassRequest.UpdateStatusClassFrom model) {
        Class c = classRepository.findById(model.getClassId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        c.setStatus(true);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), c);
    }

    @Override
    public ApiResponse<List<TraineeResponse.TraineeInfoDTO>> getTraineeForClass(Long classId) {
        List<User> list = userRepository.findUsersByClassId(classId);
        List<TraineeResponse.TraineeInfoDTO> trainerList = mapper.map(list, new TypeToken<List<TraineeResponse.TraineeInfoDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), trainerList);
    }

}
