package com.g96.ftms.service.feedback;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.FeedBackResponse;
import com.g96.ftms.entity.FeedBack;

public interface IFeedBackService {
    ApiResponse<PagedResponse<FeedBackResponse.FeedBackInfoDTO>> search(FeedBackRequest.FeedBackPagingRequest model);

    ApiResponse<FeedBackResponse.FeedBackFormDTO> getFeedBackFormDetail(FeedBackRequest.FeedBackDetailFormRequest model);

    ApiResponse<FeedBack> createFeedBack(FeedBackRequest.FeedBackAddRequest model);

    ApiResponse<FeedBack> updateFeedBack(FeedBackRequest.FeedBackEditRequest model);
}
