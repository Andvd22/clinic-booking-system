package com.mediclinic.appointment_scheduler.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.domain.response.appointment.ResAppointmentDTO;
import com.mediclinic.appointment_scheduler.service.AppointmentService;
import com.mediclinic.appointment_scheduler.service.impl.AppointmentServiceImpl;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ResAppointmentDTO> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(this.appointmentService.createAppointment(appointment));
    }

    @PutMapping
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<?>> getAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByUser(userId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<?>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<?>> getAppointmentsByStatus(@PathVariable AppointmentStatusEnum status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/schedule/{scheduleId}/available")
    public ResponseEntity<Boolean> isScheduleAvailable(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(appointmentService.isScheduleAvailable(scheduleId));
    }
}
