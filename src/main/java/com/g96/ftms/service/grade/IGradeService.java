package com.g96.ftms.service.grade;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.GradeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.GradeResponse;

public interface IGradeService {
    ApiResponse<PagedResponse<GradeResponse.GradeInfoDTO>> search(GradeRequest.GradePagingRequest model);

    ApiResponse<GradeResponse.GradeSubjectSchemeDetail> getSubjectGradeDetail(GradeRequest.GradedSubjectRequest model);

    ApiResponse<?> saveGradeSetting(GradeRequest.GradeSettingUpdateRequest model);

    ApiResponse<?> addGradeForTrainee(GradeRequest.GradedSubjectAddRequest model);
}
