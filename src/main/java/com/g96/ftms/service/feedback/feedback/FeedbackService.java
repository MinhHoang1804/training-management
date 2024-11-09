package com.g96.ftms.service.feedback.feedback;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.FeedBackResponse;
import com.g96.ftms.entity.FeedBack;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.FeedBackRepository;
import com.g96.ftms.service.feedback.IFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedBackService {
    private final FeedBackRepository feedBackRepository;
    @Override
    public ApiResponse<PagedResponse<FeedBackResponse.FeedBackInfoDTO>> search(FeedBackRequest.FeedBackPagingRequest model) {
        Page<FeedBack> pages = feedBackRepository.searchFilter(model.getKeyword(), model.getUserId(), model.getSubjectId(), model.getClassId(), model.getPageable());
        List<FeedBackResponse.FeedBackInfoDTO> list = pages.getContent().stream().map(f -> {
            return FeedBackResponse
                    .FeedBackInfoDTO.builder().feedbackId(f.getFeedbackId())
                    .userId(f.getUser().getUserId()).traineeName(f.getUser().getFullName())
                    .subjectCode(f.getSubject().getSubjectCode()).avgRating(f.getAvgRating())
                    .openDate(f.getOpenTime()).lastUpdate(f.getLastUpdateTime())
                    .build();
        }).collect(Collectors.toList());
        PagedResponse<FeedBackResponse.FeedBackInfoDTO> response = new PagedResponse<>(list, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }
}
