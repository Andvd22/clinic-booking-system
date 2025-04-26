package com.mediclinic.appointment_scheduler.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.response.schedule.ResScheduleDTO;
import com.mediclinic.appointment_scheduler.service.ScheduleService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping()
    @ApiMessage("Thêm lịch làm việc thành công")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody Schedule schedule) {
        return ResponseEntity.ok(this.scheduleService.createSchedule(schedule));
    }

    @PutMapping()
    @ApiMessage("Cập nhật lịch làm việc thành công")
    public ResponseEntity<?> updateSchedule(@Valid @RequestBody Schedule schedule) {
        return ResponseEntity.ok(this.scheduleService.updateSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa lịch làm việc thành công")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") Long id) {
        this.scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy 1 lịch làm việc thành công")
    public ResponseEntity<?> getScheduleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.scheduleService.fetchScheduleById(id));
    }

    @GetMapping()
    public ResponseEntity<?> fetchAllSchedules(Pageable pageable, @Filter Specification<Schedule> spec) {
        return ResponseEntity.ok().body(this.scheduleService.fetchAllSchedules(pageable, spec));
    }

    @GetMapping("/doctor-schedule")
    public ResponseEntity<List<ResScheduleDTO>> getSchedulesByDoctorAndDate(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate workDate) {
        List<ResScheduleDTO> schedules = scheduleService.getSchedulesByDoctorAndDate(doctorId, workDate);
        return ResponseEntity.ok(schedules);
    }
}
