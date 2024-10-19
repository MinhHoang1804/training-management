package com.g96.ftms.service.subject;

import com.g96.ftms.dto.CurriculumDTO;
import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Curriculum;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.CurriculumRepository;
import com.g96.ftms.repository.SubjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private CurriculumRepository curriculumRepository;

    @Override
    public ApiResponse<List<Subject>> getAllSubjectsWithCurriculum() {
        List<Subject> subjects = subjectRepository.findAllSubjectsWithCurriculum();
        if (subjects.isEmpty()) {
            throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND);
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subjects);
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
    public ApiResponse<SubjectDTO> addSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectDTO, subject);

        Set<Curriculum> curriculums = new HashSet<>();
        for (CurriculumDTO curriculumDTO : subjectDTO.getCurriculums()) {
            Curriculum curriculum = curriculumRepository.findById(curriculumDTO.getCurriculumId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.CURRICULUM_NOT_FOUND));
            curriculums.add(curriculum);
        }
        subject.setCurriculums(curriculums);

        Subject savedSubject = subjectRepository.save(subject);

        SubjectDTO savedSubjectDTO = new SubjectDTO();
        BeanUtils.copyProperties(savedSubject, savedSubjectDTO);

        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), savedSubjectDTO);
    }


    @Override
    public ApiResponse<Subject> updateSubject(Long subjectId, Subject subject) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND);
        }
        subject.setSubjectId(subjectId);
        Subject updatedSubject = subjectRepository.save(subject);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), updatedSubject);
    }
}
