package com.g96.ftms.service.curriculum;

import com.g96.ftms.dto.CurriculumDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface  CurriculumService {
    CurriculumDTO getCurriculumById(Long curriculumId);  // Lấy thông tin curriculum
    CurriculumDTO  updateCurriculum(CurriculumDTO curriculumDTO);  // Cập nhật curriculum
    CurriculumDTO createCurriculum(CurriculumDTO curriculumDTO);
    Map<String, Object> getPagedCurriculums(Pageable pageable);

}
