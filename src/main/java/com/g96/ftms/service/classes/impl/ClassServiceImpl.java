package com.g96.ftms.service.classes.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.request.TrainerRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ClassReponse;
import com.g96.ftms.dto.response.TrainerResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.classes.IClassService;
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
        //check location Exist
        Location location = locationRepository.findById(model.getLocationId()).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.LOCATION_NOT_FOUND));

        Class map = mapper.map(model, Class.class);
        map.setStatus(false); //send mail for cadmin

        Curriculum curriculum = curriculumRepository.findById(model.getCurriculumId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CURRICULUM_NOT_FOUND));
        map.setCurriculum(curriculum);
        //save entity
        Class classSave = classRepository.save(map);
        //create schedule
        List<Subject> subjectsInCurriculum = subjectRepository.findDistinctByCurriculumSubjectRelationList_Curriculum_CurriculumId(model.getCurriculumId());
        List<Schedule> scheduleList = new ArrayList<>();
        for (Subject subject : subjectsInCurriculum) {
            Schedule schedule = Schedule.builder().startDate(model.getStartDate()).endDate(model.getEndDate()).status(true)
                    .classs(map).subject(subject).location(location).trainer(user.getAccount()).description(model.getDescription()).build();
            scheduleList.add(schedule);
        }
        //save scheduleList
        scheduleRepository.saveAll(scheduleList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), classSave);
    }

    @Override
    public ApiResponse<List<TrainerResponse.TrainerInfoDTO>> getTrainerForClass(TrainerRequest.TrainerForClassRequest model) {
        //get list trainer inrange
        List<String> traineeUnAvailable = scheduleRepository.findTraineesInRangeAndSlot(model.getSlot(), model.getStartDate(), model.getEndDate());

        List<User> roleTrainer = userRepository.findByRoleAvail("ROLE_TRAINER",traineeUnAvailable);
        List<TrainerResponse.TrainerInfoDTO> trainerList=mapper.map(roleTrainer, new TypeToken<List<TrainerResponse.TrainerInfoDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), trainerList);
    }
}
