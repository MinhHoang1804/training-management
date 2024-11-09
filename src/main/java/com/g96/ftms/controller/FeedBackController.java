package com.g96.ftms.controller;

import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.feedback.IFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/feedback-management")
@RequiredArgsConstructor
public class FeedBackController {
    private final IFeedBackService feedBackService;
    @PostMapping("/search")
    public ApiResponse<?> getFeedbackList(@RequestBody FeedBackRequest.FeedBackPagingRequest model) {
        return feedBackService.search(model);
    }
    @PostMapping("/detail")
    public ApiResponse<?> getFeedBackFormDetail(@PathVariable FeedBackRequest.FeedBackDetailFormRequest model) {
        return feedBackService.getFeedBackFormDetail(model);
    }
}
