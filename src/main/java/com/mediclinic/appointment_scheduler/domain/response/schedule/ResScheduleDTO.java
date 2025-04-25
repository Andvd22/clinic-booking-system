package com.mediclinic.appointment_scheduler.domain.response.schedule;

import java.time.LocalDate;

import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;
import com.mediclinic.appointment_scheduler.util.constant.ScheduleStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResScheduleDTO {
    private Long id;
    private ResDoctorDTO doctor;
    private LocalDate workDate;
    private String timeSlot;
    private ScheduleStatus status;

    public static ResScheduleDTO mapEntityScheduleToDTO(Schedule schedule) {
        return new ResScheduleDTO(
                schedule.getId(),
                ResDoctorDTO.mapEntityDoctorToDTO(schedule.getDoctor()),
                schedule.getWorkDate(),
                schedule.getTimeSlot(),
                schedule.getStatus());
    }
}
