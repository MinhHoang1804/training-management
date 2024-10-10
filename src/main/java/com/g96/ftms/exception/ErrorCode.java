package com.g96.ftms.exception; // Thay đổi theo cấu trúc gói của bạn

import lombok.Getter;
import lombok.Setter;

@Getter

public enum ErrorCode {
    OK("ERR010", "Thành công. "),
    CREATED("ERR011", "Đã được tạo thành công."),
    BAD_REQUEST("ERR012", "Yêu cầu không hợp lệ. Vui lòng kiểm tra lại thông tin."),
    UNAUTHORIZED("ERR013", "Yêu cầu xác thực. "),
    FORBIDDEN("ERR014", "Bạn không có quyền thực hiện hành động này. "),
    NOT_FOUND("ERR015", "Không tìm thấy tài nguyên. "),

    USER_NOT_FOUND("ERR001", "Invalid input provided."),
    INVALID_CREDENTIALS("ERR002", "Tên đăng nhập hoặc mật khẩu không hợp lệ."),
    ACCOUNT_ALREADY_EXISTS("ERR003", "Tài khoản đã tồn tại."),
    PASSWORD_TOO_WEAK("ERR004", "Mật khẩu quá yếu."),
    INVALID_INPUT("ERR005","Invalid input provided"),
    OLD_PASSWORD_INCORRECT("ERR006", "Mật khẩu cũ không chính xác"),
    CONFIRM_PASSWORD_MISMATCH("ERR007", "Mật khẩu xác nhận không khớp với mật khẩu mới"),
    PASSWORD_CHANGE_ERROR("ERR008","PASSWORD_CHANGE_ERROR"),
    ACCESS_DENIED("ERR009", "Tài khoản không có quyền truy cập."),
    UNKNOWN_ERROR("ERR999", "Đã xảy ra lỗi không xác định.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
