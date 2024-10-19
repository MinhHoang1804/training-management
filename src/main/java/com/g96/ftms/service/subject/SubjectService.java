package com.g96.ftms.service.subject;

import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Subject;

import java.util.List;

public interface SubjectService {
    ApiResponse<List<Subject>> getAllSubjectsWithCurriculum();

    ApiResponse getSubjectDetail(Long subjectId);

    ApiResponse<Subject> updateSubject(Long subjectId,Subject subject);

    ApiResponse<SubjectDTO> addSubject(SubjectDTO subjectDTO);

}
