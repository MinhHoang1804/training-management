package com.g96.ftms.service.trainee.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.TraineeResponse;
import com.g96.ftms.entity.Class;
import com.g96.ftms.entity.User;
import com.g96.ftms.entity.UserClassRelation;
import com.g96.ftms.entity.UserClassRelationId;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.ClassRepository;
import com.g96.ftms.repository.UserClassRelationRepository;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.service.trainee.ITraineeService;
import com.g96.ftms.util.ExcelUltil;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TraineeSerivce implements ITraineeService {
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final ModelMapper mapper;
    private final UserClassRelationRepository userClassRelationRepository;
    @Override
    public ApiResponse<PagedResponse<TraineeResponse.TraineeInfoDTO>> search(TraineeRequest.TraineePagingRequest model) {
        Page<User> pages = userRepository.findUsersByClassId(model.getClassId(), model.getPageable());
        List<TraineeResponse.TraineeInfoDTO> list = mapper.map(pages.getContent(), new TypeToken<List<TraineeResponse.TraineeInfoDTO>>() {
        }.getType());
        PagedResponse<TraineeResponse.TraineeInfoDTO> response = new PagedResponse<>(list, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<?> importExcelFile(MultipartFile file, Long classId) {
        try {
            List<TraineeRequest.TraineeAddRequest> list = readExcelFile(file);
            Class c = classRepository.findById(classId).orElseThrow(() ->
                    new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));

             if(list.isEmpty()) throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.FILE_EMPTY);
            List<UserClassRelation>userClassRelationList=new ArrayList<>();
            for (TraineeRequest.TraineeAddRequest item:list){
                User user = userRepository.findByAccount(item.getAccount());
                if(user!=null&& user.getStatus() &&(user.getUserClassRelationList().isEmpty())){ //user not in class
                    UserClassRelation userClassRelation= UserClassRelation.builder()
                            .user(user)
                            .classs(c)
                            .id(new UserClassRelationId(user.getUserId(),classId))
                            .build();
                    userClassRelationList.add(userClassRelation);
                }
            }
            userClassRelationRepository.saveAll(userClassRelationList);
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
        }catch (Exception e){
            throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.FILE_WRONG_FORMAT);
        }
    }

    @Override
    @Transactional
    public ApiResponse<?> removeTrainee(TraineeRequest.TraineeRemoveRequest model) {
        List<UserClassRelationId> list = model.getListUserIds().stream().map(s -> {
            return UserClassRelationId.builder().classId(model.getClassId()).userId(s).build();
        }).toList();
        userClassRelationRepository.deleteAllById(list);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
    }

    @Override
    public ApiResponse<List<TraineeResponse.TraineeInfoDTO>> getTraineeNotInClass() {
        List<User> users = userRepository.findUsersNotInAnyClass();
        List<TraineeResponse.TraineeInfoDTO> list = mapper.map(users, new TypeToken<List<TraineeResponse.TraineeInfoDTO>>() {
        }.getType());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), list);
    }

    private List<TraineeRequest.TraineeAddRequest> readExcelFile(MultipartFile file) throws IOException {
        List<TraineeRequest.TraineeAddRequest> list = new ArrayList<>();

        // Mở file Excel
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String account = ExcelUltil.getCellValueAsString(row.getCell(0));  // Cột account
                TraineeRequest.TraineeAddRequest item= TraineeRequest.TraineeAddRequest.builder()
                        .account(account)
                        .build();
                list.add(item);
            }
        }
        workbook.close();
        return list;
    }
}
