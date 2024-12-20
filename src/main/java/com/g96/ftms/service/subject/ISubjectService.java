package com.g96.ftms.service.subject;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.entity.Subject;

import java.util.List;

public interface ISubjectService {
    ApiResponse<PagedResponse<Subject>> search(SubjectRequest.SubjectPagingRequest model);

    ApiResponse<SubjectResponse.SubjectInfoDTO> getSubjectDetail(Long subjectId);

    ApiResponse<Subject> updateSubject(SubjectRequest.SubjectEditRequest model);

    ApiResponse<Subject> addSubject(SubjectRequest.SubjectAddRequest model);
    //get subject for option
    ApiResponse<List<SubjectResponse.SubjectOptionDTO>> getAllSubjectOption();

    public SubjectResponse.SubjectClassInfo getSubjectInClass(Long classId);

    public ApiResponse<?>checkUpdate(Long subjectId);
}
