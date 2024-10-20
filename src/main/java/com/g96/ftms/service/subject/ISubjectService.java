package com.g96.ftms.service.subject;

import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Subject;

public interface ISubjectService {
    ApiResponse<PagedResponse<Subject>> search(SubjectRequest.SubjectPagingRequest model);

    ApiResponse getSubjectDetail(Long subjectId);

    ApiResponse<Subject> updateSubject(SubjectRequest.SubjectEditRequest model);

    ApiResponse<Subject> addSubject(SubjectRequest.SubjectAddRequest model);

}
