package com.g96.ftms.exception; // Thay đổi theo cấu trúc gói của bạn

import lombok.Getter;
import lombok.Setter;

@Getter

public enum ErrorCode {

    USER_NOT_FOUND("ERR001", "Người dùng không tồn tại."),
    INVALID_CREDENTIALS("ERR002", "Tên đăng nhập hoặc mật khẩu không hợp lệ."),
    ACCOUNT_ALREADY_EXISTS("ERR003", "Tài khoản đã tồn tại."),
    PASSWORD_TOO_WEAK("ERR004", "Mật khẩu quá yếu."),
    INVALID_INPUT("ERR004","Invalid input provided"),

    UNKNOWN_ERROR("ERR999", "Đã xảy ra lỗi không xác định.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
