package com.g96.ftms.service.trainee;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.TraineeResponse;

public interface ITraineeService {
    ApiResponse<PagedResponse<TraineeResponse.TraineeInfoDTO>> search(TraineeRequest.TraineePagingRequest model);
}
