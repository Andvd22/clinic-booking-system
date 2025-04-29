package com.mediclinic.appointment_scheduler.controller;

import java.time.LocalDate;

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
import com.mediclinic.appointment_scheduler.domain.Specialty;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.service.SpecialtyService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @PostMapping()
    @ApiMessage("Thêm chuyên khoa thành công")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody Specialty specialty) {
        return ResponseEntity.ok(this.specialtyService.createSpecialty(specialty));
    }

    @PutMapping()
    @ApiMessage("Cập nhật chuyên khoa thành công")
    public ResponseEntity<?> updateSchedule(@Valid @RequestBody Specialty specialty) {
        return ResponseEntity.ok(this.specialtyService.updateSpecialty(specialty));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chuyên khoa thành công")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") Long id) {
        this.specialtyService.deleteSpecialty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy 1 chuyên khoa thành công")
    public ResponseEntity<?> getScheduleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.specialtyService.fetchSpecialtyById(id));
    }

    @GetMapping()
    @ApiMessage("Lấy all chuyên khoa thành công")
    public ResponseEntity<?> fetchAllSchedules(Pageable pageable, @Filter Specification<Specialty> spec) {
        return ResponseEntity.ok().body(this.specialtyService.fetchAllSpecialty(pageable, spec));
    }

}
