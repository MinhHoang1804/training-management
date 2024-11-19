package com.g96.ftms.util;

public class ClassCreateTemplate {
    public static String generateClassRequestEmailTemplate(String recipientName, String className, String detailLink, String senderEmail) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "<h2>Yêu cầu mở lớp</h2>" +
                "<p>Dear " + recipientName + ",</p>" +
                "<p>Lớp <strong>" + className + "</strong> đang chờ anh/chị review.</p>" +
                "<p>Để xem thông tin chi tiết lớp học, vui lòng <a href='" + detailLink + "'>bấm vào đây</a>.</p>" +
                "<br>" +
                "<p>Trân trọng,</p>" +
                "<p><strong>FAMS Team</strong></p>" +
                "<hr>" +
                "<p><small>Lưu ý: Đây là email thông báo từ hệ thống FAMS, vui lòng không phản hồi email này.</small></p>" +
                "<hr>" +
                "<h2>English Version:</h2>" +
                "<p>Dear " + recipientName + ",</p>" +
                "<p>The Class <strong>" + className + "</strong> has been pending for your reviewing.</p>" +
                "<p>To view the Class details, please <a href='" + detailLink + "'>click here</a>.</p>" +
                "<br>" +
                "<p>Sincerely,</p>" +
                "<p><strong>FAMS Team</strong></p>" +
                "<hr>" +
                "<p><small>Note: This is an auto-generated email, please do not reply.</small></p>" +
                "<p>This email was sent from: <strong>" + senderEmail + "</strong></p>" +
                "</body>" +
                "</html>";
    }


    public static String generateClassRequestAcceptEmailTemplate(String recipientName, String className, String detailLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<body style='font-family:Arial, sans-serif; line-height:1.6;'>" +
                "<p>Dear " + recipientName + ",</p>" +
                "<p>Lớp <strong>" + className + "</strong> đang chờ anh/chị review.</p>" +
                "<p>Để xem thông tin chi tiết lớp học, vui lòng <a href='" + detailLink + "' style='color:blue; text-decoration:none;'>bấm vào đây</a>.</p>" +
                "<br>" +
                "<p>Trân trọng,</p>" +
                "<p><strong>FAMS Team</strong></p>" +
                "</body>" +
                "</html>";
    }
}
