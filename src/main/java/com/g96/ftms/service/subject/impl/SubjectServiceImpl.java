package com.g96.ftms.service.subject.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.subject.ISubjectService;
import com.g96.ftms.util.SqlBuilderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements ISubjectService {

    private final SubjectRepository subjectRepository;

    private final CurriculumRepository curriculumRepository;
    private final SchemeRepository schemeRepository;
    private final ClassRepository classRepository;
    private final ModelMapper mapper;

    private final SessionRepository sessionRepository;

    @Override
    public ApiResponse<PagedResponse<Subject>> search(SubjectRequest.SubjectPagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<Subject> pages = subjectRepository.searchFilter(keywordFilter, model.getStatus(), model.getPageable());
        PagedResponse<Subject> response = new PagedResponse<>(pages.getContent(), pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<SubjectResponse.SubjectInfoDTO> getSubjectDetail(Long subjectId) {

        if (subjectId == null || subjectId <= 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT);
        }

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));

        SubjectResponse.SubjectInfoDTO map = mapper.map(subject, SubjectResponse.SubjectInfoDTO.class);
        List<SchemeResponse.SubjectSchemeInfo> schemeList = mapper.map(subject.getMarkSchemeList(), new TypeToken<List<SchemeResponse.SubjectSchemeInfo>>() {
        }.getType());
        map.setSchemes(schemeList);
        return new ApiResponse<SubjectResponse.SubjectInfoDTO>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
    }

    @Override
    @Transactional
    public ApiResponse<Subject> updateSubject(SubjectRequest.SubjectEditRequest model) {
        //check exist id
        Subject subject = subjectRepository.findById(model.getId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));

        //check exist code
        if (!Objects.equals(subject.getSubjectCode(), model.getSubjectCode()) && subjectRepository.existsBySubjectCode(model.getSubjectCode())) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SUBJECT_CODE);
        }
        //delete all relation
//        schemeRepository.deleteBySubject_SubjectId(subject.getSubjectId());

        // set new scheme
        subject.setSubjectCode(model.getSubjectCode());
        subject.setSubjectName(model.getSubjectName());
        subject.setDescriptions(model.getDescriptions());
        subject.setStatus(model.isStatus());
        if (model.getSchemes() != null) {
            //remove all
            List<Long> ids = model.getSchemes().stream().filter(s -> s.getMarkSchemeId() != null).map(SubjectRequest.SubjectSchemeAddRequest::getMarkSchemeId).collect(Collectors.toList());
            removeRangeMarkScheme(ids);

            List<MarkScheme> schemeList = model.getSchemes().stream().map(s -> {
                MarkScheme scheme = mapper.map(s, MarkScheme.class);
                scheme.setStatus(true);
                scheme.setSubject(subject);
                return scheme;
            }).toList();
            //save scheme
            List<MarkScheme> schemeListSaved = schemeRepository.saveAll(schemeList);
            subject.setMarkSchemeList(schemeListSaved);
        }
        //save data
        //set newSession
        if (model.getLessonList() != null) {
            List<Long> ids = model.getLessonList().stream().filter(s -> s.getSessionId() != null).map(SubjectRequest.SubjectLessonRequest::getSessionId).collect(Collectors.toList());
            //remove all
            sessionRepository.removeRangeExclude(ids);
            List<Session> sessionList = model.getLessonList().stream().map(s -> {
                Session session = mapper.map(s, Session.class);
                session.setLesson(s.getDescription());
                session.setSubject(subject);
                session.setSessionId(s.getSessionId());
                return session;
            }).toList();
            List<Session> sessionSaved = new ArrayList<>();
            sessionSaved = sessionRepository.saveAll(sessionList);
            subject.setSessionsList(sessionSaved);
        }
        subjectRepository.save(subject);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subject);
    }

    @Override
    @Transactional
    public ApiResponse<Subject> addSubject(SubjectRequest.SubjectAddRequest model) {
        //check exist code
        if (subjectRepository.existsBySubjectCode(model.getSubjectCode())) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SUBJECT_CODE);
        }
        //save sheme
        Subject map = mapper.map(model, Subject.class);
        Subject subject = subjectRepository.save(map);
        List<MarkScheme> schemeList = model.getSchemes().stream().map(s -> {
            MarkScheme scheme = mapper.map(s, MarkScheme.class);
            scheme.setStatus(true);
            scheme.setSubject(subject);
            return scheme;
        }).collect(Collectors.toList());
        //create scheme
        schemeRepository.saveAll(schemeList);

        //save lesson
        List<SubjectRequest.SubjectLessonRequest> lessonList = model.getLessonList();
        List<Session> sessionList = lessonList.stream()
                .map(lesson -> {
                    Session session = new Session();
                    session.setLesson(lesson.getLesson());
                    session.setDescription(lesson.getDescription());
                    session.setSessionOrder(lesson.getSessionOrder());
                    session.setSubject(subject);
                    return session;
                }).collect(Collectors.toList());
        sessionRepository.saveAll(sessionList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subject);
    }


    @Override
    public ApiResponse<List<SubjectResponse.SubjectOptionDTO>> getAllSubjectOption() {
        List<Subject> list = subjectRepository.findByStatusTrue();
        List<SubjectResponse.SubjectOptionDTO> response = mapper.map(list, new TypeToken<List<SubjectResponse.SubjectOptionDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    public List<Subject> getSubjectInClass(Long classId) {
        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        return c.getCurriculum().getCurriculumSubjectRelationList().stream().map(CurriculumSubjectRelation::getSubject).toList();
    }

    @Override
    public ApiResponse<?> checkUpdate(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        List<Class> collect = subject.getSchedules().stream().map(Schedule::getClasss).toList();
        boolean check = true;
        for (Class c : collect) {
            Boolean ck = classRepository.countClassesInTime(c.getClassId(), LocalDateTime.now()) > 0;
            if (ck) { //have class in time
                check = false;
            }
            if (c.getStatus()) {
                check = false;
            }
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), check);
    }

    public void removeRangeMarkScheme(List<Long> ids) {
        // Xóa quan hệ grades và subject
        schemeRepository.clearGradesAndSubject(ids);

        // Xóa các MarkScheme không nằm trong danh sách ids
        schemeRepository.removeRangeExclude(ids);
    }

}
