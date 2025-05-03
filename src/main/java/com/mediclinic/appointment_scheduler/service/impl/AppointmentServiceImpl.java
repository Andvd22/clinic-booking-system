package com.mediclinic.appointment_scheduler.service.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.appointment.ResAppointmentDTO;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;
import com.mediclinic.appointment_scheduler.repository.AppointmentRepository;
import com.mediclinic.appointment_scheduler.repository.DoctorRepository;
import com.mediclinic.appointment_scheduler.repository.ScheduleRepository;
import com.mediclinic.appointment_scheduler.repository.UserRepository;
import com.mediclinic.appointment_scheduler.service.AppointmentService;
import com.mediclinic.appointment_scheduler.util.SecurityUtil;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;
import com.mediclinic.appointment_scheduler.util.constant.ScheduleStatusEnum;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.mediclinic.appointment_scheduler.util.error.ResourceNotFoundException;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository,
            DoctorRepository doctorRepository, ScheduleRepository scheduleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ResAppointmentDTO createAppointment(Appointment appointment) {
        Schedule scheduleDB = this.scheduleRepository.findById(appointment.getSchedule().getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy lịch làm việc!"));

        if (!scheduleDB.getDoctor().getId().equals(appointment.getDoctor().getId())) {
            throw new IdInvalidException("Lịch làm việc không thuộc về bác sĩ này!");
        }

        if (this.appointmentRepository.existsBySchedule(appointment.getSchedule())) {
            Appointment appointmentDB = this.appointmentRepository.findBySchedule(appointment.getSchedule())
                    .orElseThrow(() -> new IdInvalidException(
                            "không tìm thấy lịch hẹn trong khi đã tồn tại lịch làm việc trong db???"));
            if (!appointmentDB.getStatus().equals(AppointmentStatusEnum.CANCELLED))
                throw new IdInvalidException("Lịch làm việc này đã có người đặt!");
        }

        Doctor doctor = this.doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        User user = this.userRepository.findById(appointment.getUser().getId())
                .orElseThrow(() -> new IdInvalidException("không tồn tại người dùng với id này"));
        appointment.setStatus(AppointmentStatusEnum.PENDING);
        scheduleDB.setStatus(ScheduleStatusEnum.BOOKED);
        appointment.setDoctor(doctor);
        appointment.setUser(user);
        appointment.setSchedule(scheduleDB);
        Appointment appointmentDB = this.appointmentRepository.save(appointment);
        return ResAppointmentDTO.mapEntityAppointmentToDTO(appointmentDB);
    }

    @Override
    public ResAppointmentDTO updateAppointment(Appointment appointment) {
        Appointment existing = this.appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy lịch hẹn!"));
        existing.setReason(appointment.getReason());
        existing.setStatus(appointment.getStatus());
        Appointment appointmentDB = this.appointmentRepository.save(existing);
        return ResAppointmentDTO.mapEntityAppointmentToDTO(appointmentDB);
    }

    @Override
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("lịch không tồn tại"));
        if (appointment.getStatus().equals("COMPLETED")) {
            throw new IdInvalidException("lịch hẹn cảu bạn đã được khám");
        }
        appointment.setStatus(AppointmentStatusEnum.CANCELLED);
        appointment.setReason(reason);
        Schedule schedule = appointment.getSchedule();
        schedule.setStatus(ScheduleStatusEnum.AVAILABLE);
        Appointment appointmentDB = this.appointmentRepository.save(appointment);
        this.scheduleRepository.save(schedule);
        this.appointmentRepository.delete(appointmentDB);
    }

    @Override
    public void deleteAppointment(Long id) {
        this.appointmentRepository.deleteById(id);
    }

    @Override
    public ResAppointmentDTO getAppointmentById(Long id) {
        Appointment appointmentDB = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy lịch hẹn!"));
        return ResAppointmentDTO.mapEntityAppointmentToDTO(appointmentDB);
    }

    @Override
    public List<ResAppointmentDTO> getAppointmentsByUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy user!"));
        List<Appointment> appointments = this.appointmentRepository.findByUser(user);
        return appointments.stream().map(appointment -> ResAppointmentDTO.mapEntityAppointmentToDTO(appointment))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResAppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = this.doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy bác sĩ!"));
        List<Appointment> appointments = this.appointmentRepository.findByDoctor(doctor);
        return appointments.stream().map(appointment -> ResAppointmentDTO.mapEntityAppointmentToDTO(appointment))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResAppointmentDTO> getAppointmentsByStatus(AppointmentStatusEnum status) {
        List<Appointment> appointments = this.appointmentRepository.findByStatus(status);
        return appointments.stream().map(appointment -> ResAppointmentDTO.mapEntityAppointmentToDTO(appointment))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isScheduleAvailable(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy lịch làm việc!"));
        return !this.appointmentRepository.existsBySchedule(schedule);
    }

    @Override
    public ResPaginationDTO fetchAllAppointments(Pageable pageable, Specification<Appointment> spec) {
        Page<Appointment> pageAppointments = this.appointmentRepository.findAll(spec, pageable);
        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize((pageable.getPageSize()));
        meta.setPages(pageAppointments.getTotalPages());
        meta.setTotal((pageAppointments.getTotalElements()));
        res.setMeta(meta);
        List<ResAppointmentDTO> ResAppointmentDTOs = pageAppointments.getContent().stream()
                .map(appointment -> ResAppointmentDTO.mapEntityAppointmentToDTO(appointment))
                .collect(Collectors.toList());
        res.setResult(ResAppointmentDTOs);
        return res;
    }
}
