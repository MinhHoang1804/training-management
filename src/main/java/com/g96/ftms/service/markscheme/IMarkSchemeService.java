package com.g96.ftms.service.markscheme;

import com.g96.ftms.dto.request.SchemeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.repository.MarkSchemeRepository;

import java.util.List;

public interface IMarkSchemeService {
    public ApiResponse<List<SchemeResponse.SubjectSchemeInfo>>getSchemeInSubjectClass(SchemeRequest.SchemeClassSubjectRequest model);
}
