package com.g96.ftms.exception; // Thay đổi theo cấu trúc gói của bạn

import com.g96.ftms.entity.Subject;
import lombok.Getter;
import lombok.Setter;

@Getter

public enum ErrorCode {
    USER_NOT_FOUND("ERR001", "Invalid input provided."),
    INVALID_CREDENTIALS("ERR002", "Tên đăng nhập hoặc mật khẩu không hợp lệ."),
    ACCOUNT_ALREADY_EXISTS("ERR003", "Tài khoản đã tồn tại."),
    PASSWORD_TOO_WEAK("ERR004", "Mật khẩu quá yếu."),
    INVALID_INPUT("ERR005","Invalid input provided"),
    OLD_PASSWORD_INCORRECT("ERR006", "Mật khẩu cũ không chính xác"),
    CONFIRM_PASSWORD_MISMATCH("ERR007", "Mật khẩu xác nhận không khớp với mật khẩu mới"),
    PASSWORD_CHANGE_ERROR("ERR008","Lỗi! Không thể đổi mật khẩu"),
    ACCESS_DENIED("ERR009", "Tài khoản không có quyền truy cập."),
    OK("ERR010", "Thành công. "),
    CREATED("ERR011", "Đã được tạo thành công."),
    BAD_REQUEST("ERR012", "Yêu cầu không hợp lệ. Vui lòng kiểm tra lại thông tin."),
    UNAUTHORIZED("ERR013", "Yêu cầu xác thực. "),
    FORBIDDEN("ERR014", "Bạn không có quyền thực hiện hành động này. "),
    NOT_FOUND("ERR015", "Không tìm thấy tài nguyên. "),
    UNKNOWN_ERROR("ERR016", "Đã xảy ra lỗi không xác định."),
    INVALID_VERIFICATION_CODE("ERR017","Mã xác thực không đúng" ),
    EMPTY_INPUT("ERR018", "Không được để trống"),
    INVALID_EMAIL("ERR019","Email không hợp lệ" ),
    SUBJECT_NOT_FOUND("ERR020","Không tìm thấy môn học"),
    CURRICULUM_NOT_FOUND("ERR021","Không tìm thấy Khung chương trình"),
    DUPLICATE_SUBJECT_NAME("ERR022","Tên môn học đã tồn tại"),
    DUPLICATE_SUBJECT_CODE("ERR023","Mã môn học đã tồn tại"),
    DUPLICATE_CURRICULUM_NAME("ERR024","Tên chương trình học đã tồn tại"),
    ROOM_NOT_FOUND("ERR025","Không tìm thấy tên phòng"),
    GENERATION_NAME_SETTING_EXIST("ERR026","Setting cho generation này đã tồn tại"),
    SETTING_NOTFOUND("ERR027","Không tìm thấy setting"),
    GENERATION_NOT_FOUND("ERR028","Generation này không tồn tại"),
    DUPLICATE_SETTING("ERR029","Setting đã tồn tại"),
    CLASS_NOT_FOUND("ERR030","Không tìm thấy lớp học");
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
