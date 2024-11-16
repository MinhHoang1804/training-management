package com.g96.ftms.service.session.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SessionResponse;
import com.g96.ftms.entity.Session;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.SubjectRepository;
import com.g96.ftms.service.session.ISessionService;
import com.g96.ftms.util.ExcelUltil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService implements ISessionService {
    private final SubjectRepository subjectRepository;
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

    @Override
    public ApiResponse<List<Session>> getSessionBySubjectId(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subject.getSessionsList());
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


    public ResponseEntity<byte[]> exportSessionsToExcel(Long subjectId) {
        // Lấy danh sách Session theo Subject ID
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        List<Session> sessions = subject.getSessionsList();

        // Tạo Workbook và Sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sessions");

            // Tạo tiêu đề cột
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Lesson", "Description", "Order"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Điền dữ liệu vào Sheet
            for (int i = 0; i < sessions.size(); i++) {
                Session session = sessions.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(session.getLesson());
                row.createCell(1).setCellValue(session.getDescription());
                row.createCell(2).setCellValue(session.getSessionOrder());
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi dữ liệu ra mảng byte
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            // Trả file Excel dưới dạng ResponseEntity
            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.add("Content-Disposition", "attachment; filename=sessions.xlsx");
            return ResponseEntity
                    .ok()
                    .headers(headersResponse)
                    .body(excelBytes);

        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.EXPORT_FAILED);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
