package com.g96.ftms.service.classes;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ClassReponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.entity.Subject;

public interface IClassService {
    ApiResponse<PagedResponse<ClassReponse.ClassInforDTO>> search(ClassRequest.ClassPagingRequest model);

    ApiResponse<ClassReponse.ClassInforDTO> getClassDetail(Long classId);

}
