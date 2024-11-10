package com.g96.ftms.service.markscheme.impl;

import com.g96.ftms.dto.request.SchemeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.entity.MarkScheme;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.MarkSchemeRepository;
import com.g96.ftms.service.markscheme.IMarkSchemeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkSchemeService implements IMarkSchemeService {
    private final MarkSchemeRepository markSchemeRepository;
    private final ModelMapper mapper;
    @Override
    public ApiResponse<List<SchemeResponse.SubjectSchemeInfo>> getSchemeInSubjectClass(SchemeRequest.SchemeClassSubjectRequest model) {
        List<MarkScheme> markSchemes = markSchemeRepository.findBySubject_SubjectId(model.getSubjectId());
        List<SchemeResponse.SubjectSchemeInfo> schemeList = mapper.map(markSchemes, new TypeToken<List<SchemeResponse.SubjectSchemeInfo>>() {
        }.getType());
        return new ApiResponse<List<SchemeResponse.SubjectSchemeInfo>>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), schemeList);

    }
}
