package com.mediclinic.appointment_scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.service.DoctorService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping()
    @ApiMessage("Thêm bác sĩ thành công")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody Doctor doctorPM) {
        return ResponseEntity.ok(this.doctorService.createDoctor(doctorPM));
    }

    @PutMapping()
    @ApiMessage("Cập nhật bác sĩ thành công")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody Doctor doctorPM) {
        return ResponseEntity.ok(this.doctorService.updateDoctor(doctorPM));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa bác sĩ thành công")
    public ResponseEntity<?> updateDoctor(@PathVariable("id") Long id) {
        this.doctorService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy 1 bác sĩ thành công")
    public ResponseEntity<?> getDoctorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.doctorService.fetchDoctorById(id));
    }

    @GetMapping()
    public ResponseEntity<?> detchAllDoctor(Pageable pageable, @Filter Specification<Doctor> spec) {
        return ResponseEntity.ok().body(this.doctorService.fetchAllDoctors(pageable, spec));
    }

}
