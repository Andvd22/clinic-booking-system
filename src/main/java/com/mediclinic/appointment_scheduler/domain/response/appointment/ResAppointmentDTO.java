package com.mediclinic.appointment_scheduler.domain.response.appointment;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResAppointmentDTO {
    private Long id;
    private User user;
    private Doctor doctor;
    private Schedule schedule;
    private String reason;
    private AppointmentStatusEnum status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Doctor {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schedule {
        private Long id;
        private String timeSlot;
    }

    public static ResAppointmentDTO mapEntityAppointmentToDTO(Appointment appointment) {
        return new ResAppointmentDTO(
                appointment.getId(),
                new User(appointment.getUser().getId(), appointment.getUser().getName()),
                new Doctor(appointment.getDoctor().getId(), appointment.getDoctor().getFullName()),
                new Schedule(appointment.getSchedule().getId(), appointment.getSchedule().getTimeSlot()),
                appointment.getReason(),
                appointment.getStatus());
    }
}
