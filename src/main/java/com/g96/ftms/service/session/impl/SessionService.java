package com.g96.ftms.service.session.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SessionResponse;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.service.session.ISessionService;
import com.g96.ftms.util.ExcelUltil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService implements ISessionService {

    @Override
    public ApiResponse<List<SessionResponse.SessionInfoDTO>> importExcelFile(MultipartFile file) {
        try {
            List<SessionResponse.SessionInfoDTO> sessionInfoDTOS = readExcelFile(file);
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), sessionInfoDTOS);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new AppException(HttpStatus.BAD_REQUEST,ErrorCode.FILE_WRONG_FORMAT);
    }

    private List<SessionResponse.SessionInfoDTO> readExcelFile(MultipartFile file) throws IOException {
        List<SessionResponse.SessionInfoDTO> list = new ArrayList<>();

        // Mở file Excel
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String lesson = ExcelUltil.getCellValueAsString(row.getCell(0));  // cột lesson
                String description = ExcelUltil.getCellValueAsString(row.getCell(1));  // cột description
                String sessionOrder = ExcelUltil.getCellValueAsString(row.getCell(2));  // cột sessionOrder
                if(lesson!=null||description!=null||sessionOrder!=null){
                    SessionResponse.SessionInfoDTO item= SessionResponse.SessionInfoDTO.builder()
                            .sessionOrder(Integer.valueOf(sessionOrder))
                            .lesson(lesson)
                            .description(description)
                            .build();
                    list.add(item);

                }
            }
        }
        workbook.close();
        return list;
    }
}
