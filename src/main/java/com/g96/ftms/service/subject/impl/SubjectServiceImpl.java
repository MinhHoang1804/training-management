package com.g96.ftms.service.subject.impl;

import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.CurriculumRepository;
import com.g96.ftms.repository.SubjectRepository;
import com.g96.ftms.service.subject.ISubjectService;
import com.g96.ftms.util.SqlBuilderUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements ISubjectService {

    private final SubjectRepository subjectRepository;

    private final CurriculumRepository curriculumRepository;
    private final ModelMapper mapper;

    @Override
    public ApiResponse<PagedResponse<Subject>> search(SubjectRequest.SubjectPagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<Subject> pages = subjectRepository.searchFilter(keywordFilter, model.getStatus(), model.getPageable());
        PagedResponse<Subject> response = new PagedResponse<>(pages.getContent(), pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse getSubjectDetail(Long subjectId) {

        if (subjectId == null || subjectId <= 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT);
        }

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));

        SubjectDTO subjectDTO = new SubjectDTO();
        BeanUtils.copyProperties(subject, subjectDTO);

        return new ApiResponse(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subjectDTO);
    }

    @Override
    public ApiResponse<Subject> updateSubject(SubjectRequest.SubjectEditRequest model) {
        //check exist id
        Subject subject = subjectRepository.findById(model.getId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        //check exist name
//        if(subject.getSubjectName()!=model.getSubjectName()&&subjectRepository.existsBySubjectName(model.getSubjectName())){
//            throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.DUPLICATE_SUBJECT_NAME);
//        }
        //check exist code
        if(!Objects.equals(subject.getSubjectCode(), model.getSubjectCode()) && subjectRepository.existsBySubjectCode(model.getSubjectCode())){
            throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.DUPLICATE_SUBJECT_CODE);
        }

        //save data
        Subject map = mapper.map(model, Subject.class);
        subjectRepository.save(map);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
    }

    @Override
    public ApiResponse<Subject> addSubject(SubjectRequest.SubjectAddRequest model) {
        //check exist code
        if(subjectRepository.existsBySubjectCode(model.getSubjectCode())){
            throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.DUPLICATE_SUBJECT_CODE);
        }
        Subject map = mapper.map(model, Subject.class);
        subjectRepository.save(map);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
    }

}
