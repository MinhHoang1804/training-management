package com.g96.ftms.service.curriculum;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.CurriculumRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.CurriculumnResponse;
import com.g96.ftms.dto.response.SubjectResponse;
import com.g96.ftms.entity.Curriculum;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.CurriculumRepository;
import com.g96.ftms.repository.SubjectRepository;
import com.g96.ftms.util.SqlBuilderUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements ICurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final ModelMapper mapper;

//    @Override
//    public CurriculumDTO  updateCurriculum(CurriculumDTO curriculumDTO) {
//        Curriculum curriculum = curriculumRepository.findById(curriculumDTO.getCurriculumId())
//                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CURRICULUM_NOT_FOUND));
//
//        // Cập nhật thông tin
//        curriculum.setCurriculumName(curriculumDTO.getCurriculumName());
//        curriculum.setDescriptions(curriculumDTO.getDescriptions());
//        curriculum.setStatus(curriculumDTO.getStatus());
//
//        // Lưu lại thay đổi
//        curriculumRepository.save(curriculum);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = curriculum.getCreatedDate().format(formatter);
//
//
//        return CurriculumDTO.builder()
//                .curriculumId(curriculum.getCurriculumId())
//                .curriculumName(curriculum.getCurriculumName())
//                .descriptions(curriculum.getDescriptions())
//                .createdDate(formattedDate) // Kiểm tra giá trị ở đây
//                .status(curriculum.getStatus())
//                .build();
//
//    }
//    @Override
//    public Map<String, Object> getPagedCurriculums(Pageable pageable) {
//        Page<Curriculum> curriculumPage = curriculumRepository.findAll(pageable);
//
//        List<CurriculumDTO> curriculumDTOs = curriculumPage.stream()
//                .map(curriculum -> CurriculumDTO.builder()
//                        .curriculumId(curriculum.getCurriculumId())
//                        .curriculumName(curriculum.getCurriculumName())
//                        .descriptions(curriculum.getDescriptions())
//                        .createdDate(curriculum.getCreatedDate().toString())
//                        .status(curriculum.getStatus())
//                        .build())
//                .collect(Collectors.toList());
//
//        return Map.of(
//                "curriculums", curriculumDTOs,
//                "currentPage", curriculumPage.getNumber(),
//                "totalItems", curriculumPage.getTotalElements(),
//                "totalPages", curriculumPage.getTotalPages()
//        );
//    }

    @Override
    public ApiResponse<PagedResponse<Curriculum>> search(CurriculumRequest.CurriculumPagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<Curriculum> pages = curriculumRepository.searchFilter(keywordFilter, model.getStatus(), model.getPageable());
        PagedResponse<Curriculum> response = new PagedResponse<>(pages.getContent(), pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<CurriculumnResponse.CurriculumInfoDTO> getCurriculumDetail(Long id) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CURRICULUM_NOT_FOUND));
        // Map each subject in CurriculumSubjectRelation to SubjectDTO
        List<SubjectResponse.SubjectInfoDTO> subjectDTOs = curriculum.getCurriculumSubjectRelationList().stream()
                .map(relation -> {
                    Subject subject = relation.getSubject();
                    // Map Subject entity to SubjectDTO
                    return SubjectResponse.SubjectInfoDTO.builder()
                            .subjectId(subject.getSubjectId())
                            .subjectCode(subject.getSubjectCode())
                            .subjectName(subject.getSubjectName())
                            .documentLink(subject.getDocumentLink())
                            .descriptions(subject.getDescriptions())
                            .status(subject.isStatus())
                            .weightPercentage(relation.getWeightPercentage())
                            .createdDate(subject.getCreatedDate().toString())
                            // Skip mapping curriculums to avoid circular reference
                            .build();
                }).toList();

        // Build response
        CurriculumnResponse.CurriculumInfoDTO response = CurriculumnResponse.CurriculumInfoDTO.builder()
                .curriculumId(curriculum.getCurriculumId())
                .curriculumName(curriculum.getCurriculumName())
                .descriptions(curriculum.getDescriptions())
                .createdDate(curriculum.getCreatedDate().toString())
                .status(curriculum.getStatus())
                .subjects(subjectDTOs) // Add subject DTOs
                .build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<Curriculum> createCurriculum(CurriculumRequest.CurriculumAddRequest model) {
        // check curriculum name exist
        if(curriculumRepository.existsByCurriculumName(model.getCurriculumName())){
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_CURRICULUM_NAME);
        }
        Curriculum map = mapper.map(model, Curriculum.class);
        curriculumRepository.save(map);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), map);
    }
}
