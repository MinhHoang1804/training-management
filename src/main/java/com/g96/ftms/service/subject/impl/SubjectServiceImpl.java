package com.g96.ftms.service.subject.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.dto.response.SessionResponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.subject.ISubjectService;
import com.g96.ftms.util.ExcelUltil;
import com.g96.ftms.util.SqlBuilderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        schemeRepository.deleteBySubject_SubjectId(subject.getSubjectId());

        // set new scheme
        List<MarkScheme> schemeList = model.getSchemes().stream().map(s -> {
            MarkScheme scheme = mapper.map(s, MarkScheme.class);
            scheme.setStatus(true);
            scheme.setSubject(subject);
            return scheme;
        }).toList();
        //save scheme
        List<MarkScheme> schemeListSaved = schemeRepository.saveAll(schemeList);

        //save data
        Subject map = mapper.map(model, Subject.class);
        map.setMarkSchemeList(schemeListSaved);
        subjectRepository.save(map);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
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
       List<Session> sessionList= mapper.map(lessonList,new TypeToken<List<Session>>() {
        }.getType());
       sessionList.forEach(s -> s.setSubject(subject));
        sessionRepository.saveAll(sessionList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
    }

    @Override
    public ApiResponse<List<SubjectResponse.SubjectOptionDTO>> getAllSubjectOption() {
        List<Subject> list = subjectRepository.findByStatusTrue();
        List<SubjectResponse.SubjectOptionDTO> response = mapper.map(list, new TypeToken<List<SubjectResponse.SubjectOptionDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    public List<Subject> getSubjectInClass(Long classId){
        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        return c.getCurriculum().getCurriculumSubjectRelationList().stream().map(CurriculumSubjectRelation::getSubject).toList();
    }

    @Override
    public ApiResponse<List<SessionResponse.SessionInfoDTO>> importExcelFile(MultipartFile file) {
        try {
            List<SessionResponse.SessionInfoDTO> sessionInfoDTOS = readExcelFile(file);
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), sessionInfoDTOS);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.FILE_WRONG_FORMAT);
    }

    private List<SessionResponse.SessionInfoDTO> readExcelFile(MultipartFile file) throws IOException {
        List<SessionResponse.SessionInfoDTO> list = new ArrayList<>();

        // Mở file Excel
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String lesson = ExcelUltil.getCellValueAsString(row.getCell(0));  // cột lesson
                String description = ExcelUltil.getCellValueAsString(row.getCell(1));  // cột description
                String sessionOrder = ExcelUltil.getCellValueAsString(row.getCell(2));  // cột sessionOrder
                if(lesson!=null||description!=null||sessionOrder!=null){
                SessionResponse.SessionInfoDTO item= SessionResponse.SessionInfoDTO.builder()
                        .sessionOrder(Integer.valueOf(sessionOrder))
                        .lesson(lesson)
                        .description(description)
                        .build();
                list.add(item);

                }
            }
        }
        workbook.close();
        return list;
    }
}
