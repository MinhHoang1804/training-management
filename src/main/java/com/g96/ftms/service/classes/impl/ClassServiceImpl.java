package com.g96.ftms.service.classes.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ClassReponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.classes.IClassService;
import com.g96.ftms.service.curriculum.ICurriculumService;
import com.g96.ftms.util.SqlBuilderUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final RoomRepository roomRepository;
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
        //check room Exist
        Room room = roomRepository.findById(model.getRoomId()).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.ROOM_NOT_FOUND));

        Class map = mapper.map(model, Class.class);

        Curriculum curriculum = curriculumRepository.findById(model.getCurriculumId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CURRICULUM_NOT_FOUND));
        map.setCurriculum(curriculum);
        //save entity
        Class classSave = classRepository.save(map);
        //create schedule
        List<Subject> subjectsInCurriculum = subjectRepository.findDistinctByCurriculumSubjectRelationList_Curriculum_CurriculumId(model.getCurriculumId());
        List<Schedule> scheduleList = new ArrayList<>();
        for (Subject subject : subjectsInCurriculum) {
            Schedule schedule = Schedule.builder().startTime(model.getStartDate()).endTime(model.getEndDate()).status(true)
                    .classs(map).subject(subject).room(room).userId(user.getUserId()).description(model.getDescriptions()).build();
            scheduleList.add(schedule);
        }
        //save scheduleList
        scheduleRepository.saveAll(scheduleList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), classSave);
    }
}
