package com.g96.ftms.service.feedback;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.FeedBackResponse;

public interface IFeedBackService
{
    ApiResponse<PagedResponse<FeedBackResponse.FeedBackInfoDTO>> search(FeedBackRequest.FeedBackPagingRequest model);
}
