package com.g96.ftms.service.task;

import com.g96.ftms.entity.Class;
import com.g96.ftms.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ScheduledTask {
    private final ClassRepository classRepository;
    // Chạy mỗi 15 phút
    @Scheduled(cron = "0 */15 * * * ?")
    public void runGradeTask() {
        System.out.println("Task running every 15 minutes: " + System.currentTimeMillis());
    }
    // Chạy mỗi 15 phút
    @Scheduled(cron = "0 */1 * * * ?")
    public void runClassTask() {
        log.info("=====================Class Task running======================");
        List<Class> closeClass = classRepository.findCloseClass(LocalDateTime.now());
        closeClass.forEach(s->s.setStatus(false));
        classRepository.saveAll(closeClass);
        log.info("====================={} closed ===================", closeClass.size());
    }
}
