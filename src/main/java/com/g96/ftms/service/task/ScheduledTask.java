package com.g96.ftms.service.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    // Chạy mỗi 15 phút
    @Scheduled(cron = "0 */15 * * * ?")
    public void runGradeTask() {
        System.out.println("Task running every 15 minutes: " + System.currentTimeMillis());
    }
}
