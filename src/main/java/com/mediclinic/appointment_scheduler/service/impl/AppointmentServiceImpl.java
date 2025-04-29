package com.mediclinic.appointment_scheduler.service.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.domain.response.appointment.ResAppointmentDTO;
import com.mediclinic.appointment_scheduler.repository.AppointmentRepository;
import com.mediclinic.appointment_scheduler.repository.DoctorRepository;
import com.mediclinic.appointment_scheduler.repository.ScheduleRepository;
import com.mediclinic.appointment_scheduler.repository.UserRepository;
import com.mediclinic.appointment_scheduler.service.AppointmentService;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;
import com.mediclinic.appointment_scheduler.util.constant.ScheduleStatusEnum;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;

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
            throw new IdInvalidException("Lịch làm việc này đã có người đặt!");
        }

        Doctor doctor = this.doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        User user = this.userRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new IdInvalidException("không tồn tại người dùng với id này"));

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
}
