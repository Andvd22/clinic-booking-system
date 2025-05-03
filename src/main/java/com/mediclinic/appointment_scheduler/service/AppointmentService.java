package com.mediclinic.appointment_scheduler.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.appointment.ResAppointmentDTO;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;

public interface AppointmentService {
    ResAppointmentDTO createAppointment(Appointment appointment);

    ResAppointmentDTO updateAppointment(Appointment appointment);

    void deleteAppointment(Long id);

    ResAppointmentDTO getAppointmentById(Long id);

    List<ResAppointmentDTO> getAppointmentsByUser(Long userId);

    List<ResAppointmentDTO> getAppointmentsByDoctor(Long doctorId);

    List<ResAppointmentDTO> getAppointmentsByStatus(AppointmentStatusEnum status);

    ResPaginationDTO fetchAllAppointments(Pageable pageable, Specification<Appointment> specification);

    boolean isScheduleAvailable(Long scheduleId);

    void cancelAppointment(Long appointmentId, String reason);
}
