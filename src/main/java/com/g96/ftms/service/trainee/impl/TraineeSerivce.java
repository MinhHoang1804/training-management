package com.g96.ftms.service.trainee.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.TraineeResponse;
import com.g96.ftms.entity.User;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.service.trainee.ITraineeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TraineeSerivce implements ITraineeService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public ApiResponse<PagedResponse<TraineeResponse.TraineeInfoDTO>> search(TraineeRequest.TraineePagingRequest model) {
        Page<User> pages = userRepository.findUsersByClassId(model.getClassId(), model.getPageable());
        List<TraineeResponse.TraineeInfoDTO> list = mapper.map(pages.getContent(), new TypeToken<List<TraineeResponse.TraineeInfoDTO>>() {
        }.getType());
        ;
        PagedResponse<TraineeResponse.TraineeInfoDTO> response = new PagedResponse<>(list, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }
}
