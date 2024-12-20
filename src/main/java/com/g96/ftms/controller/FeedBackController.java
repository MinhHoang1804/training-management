package com.g96.ftms.controller;

import com.g96.ftms.dto.request.FeedBackRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.feedback.IFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedback-management")
@RequiredArgsConstructor
public class FeedBackController {
    private final IFeedBackService feedBackService;

    @PostMapping("/search")
    public ApiResponse<?> getFeedbackList(@RequestBody FeedBackRequest.FeedBackPagingRequest model) {
        return feedBackService.search(model);
    }

    @PostMapping("/detail")
    public ApiResponse<?> getFeedBackFormDetail(@RequestBody FeedBackRequest.FeedBackDetailFormRequest model) {
        return feedBackService.getFeedBackFormDetail(model);
    }

    @PostMapping("/create")
    public ApiResponse<?> createFeedBack(@RequestBody FeedBackRequest.FeedBackAddRequest model) {
        return feedBackService.createFeedBack(model);
    }

    @PostMapping("/check-feedback-subject")
    public ApiResponse<?> checkFeedBackSubject(@RequestBody FeedBackRequest.FeedBackCheckRequest model) {
        return feedBackService.checkFeedBackSubject(model);
    }

    @PostMapping("/update")
    public ApiResponse<?> updateFeedback(@RequestBody FeedBackRequest.FeedBackEditRequest model) {
        return feedBackService.updateFeedBack(model);
    }
}
