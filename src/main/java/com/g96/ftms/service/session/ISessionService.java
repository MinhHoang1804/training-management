package com.g96.ftms.service.session;


import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SessionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISessionService {
    ApiResponse<List<SessionResponse.SessionInfoDTO>> importExcelFile(MultipartFile file);
}
