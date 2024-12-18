package com.g96.ftms.service.trainee;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.TraineeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITraineeService {
    ApiResponse<PagedResponse<TraineeResponse.TraineeInfoDTO>> search(TraineeRequest.TraineePagingRequest model);

    ApiResponse<?> importExcelFile(MultipartFile file, Long classId);

    ApiResponse<?> removeTrainee(TraineeRequest.TraineeRemoveRequest model);

    ApiResponse<List<TraineeResponse.TraineeInfoDTO>> getTraineeNotInClass();
}
