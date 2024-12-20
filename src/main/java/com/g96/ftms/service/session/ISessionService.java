package com.g96.ftms.service.session;


import com.g96.ftms.dto.request.SessionRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SessionResponse;
import com.g96.ftms.entity.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISessionService {
    ApiResponse<List<SessionResponse.SessionInfoDTO>> importExcelFile(MultipartFile file);

    ApiResponse<List<Session>> getSessionBySubjectId(Long SubjectId);

    ResponseEntity<byte[]> exportSessionsToExcel(Long subjectId);

    ResponseEntity<byte[]> exportTemplate();

    ApiResponse<?> updateSessionUpdateSession(SessionRequest.SessionEditRequest model);

    ApiResponse<?> createSessionUpdateSession(SessionRequest.SessionAddRequest model);
}
