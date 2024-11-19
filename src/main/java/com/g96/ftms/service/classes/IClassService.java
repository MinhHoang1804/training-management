package com.g96.ftms.service.classes;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.request.TrainerRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ClassReponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.dto.response.TrainerResponse;
import com.g96.ftms.entity.Subject;

import java.util.List;

public interface IClassService {
    ApiResponse<PagedResponse<ClassReponse.ClassInforDTO>> search(ClassRequest.ClassPagingRequest model);

    ApiResponse<ClassReponse.ClassInforDTO> getClassDetail(Long classId);
    ApiResponse<ClassReponse.ClassInforDTO> getClassForTrainee(Long classId);

    ApiResponse<?> addClass(ClassRequest.ClassAddRequest model);

    ApiResponse<List<TrainerResponse.TrainerInfoDTO>> getTrainerForClass(TrainerRequest.TrainerForClassRequest model);

    ApiResponse<?> updateClassByAdmin(ClassRequest.UpdateClassByAdminForm model);

    ApiResponse<?> acceptClass(ClassRequest.UpdateStatusClassFrom model);
}
