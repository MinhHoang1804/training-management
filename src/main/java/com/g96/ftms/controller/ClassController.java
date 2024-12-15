package com.g96.ftms.controller;

import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.request.ScheduleRequest;
import com.g96.ftms.dto.request.TrainerRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.classes.IClassService;
import com.g96.ftms.service.schedule.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class-management")
@RequiredArgsConstructor
public class ClassController {
    private final IClassService classService;
    private final IScheduleService scheduleService;

    @PostMapping("/search")
    public ApiResponse<?> getClassList(@RequestBody ClassRequest.ClassPagingRequest model) {
        return classService.search(model);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<?> getClass(@PathVariable("id") Long classId) {
        return classService.getClassDetail(classId);
    }

    @GetMapping("/get-trainee-in-class/{id}")
    public ApiResponse<?> getTraineeInClass(@PathVariable("id") Long classId) {
        return classService.getTraineeForClass(classId);
    }


    @PostMapping("/info-trainee/{id}")
    public ApiResponse<?> getInfoForTrainee(@PathVariable("id") Long classId) {
        return classService.getClassForTrainee(classId);
    }

    @PostMapping("/add")
    public ApiResponse<?> addClass(@RequestBody ClassRequest.ClassAddRequest model) {
        return classService.addClass(model);
    }

    @PostMapping("/get-trainer-for-class")
    public ApiResponse<?> getTrainerForClass(@RequestBody TrainerRequest.TrainerForClassRequest model) {
        return classService.getTrainerForClass(model);
    }
    //s

    @PostMapping("/update-class-by-admin")
    public ApiResponse<?> updateClassByAdmin(@RequestBody ClassRequest.UpdateClassByAdminForm model) {
        return classService.updateClassByAdmin(model);
    }

    @PostMapping("/get-time-table-session")
    public ApiResponse<?> getTimeTableSession(@RequestBody ScheduleRequest.ScheduleGenerateDto model) {
        return scheduleService.generateTimeTable(model.getStartDate(),model.getSessions());
    }
    @PostMapping("/accep-class")
    public ApiResponse<?> getTimeTableSession(ClassRequest.UpdateStatusClassFrom model) {
        return classService.acceptClass(model);
    }

    @PostMapping("/get-class-user/{userId}")
    public ApiResponse<?> getClassUser(@PathVariable("userId") Long userId) {
        return classService.getClassUser(userId);
    }


}
