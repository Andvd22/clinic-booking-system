package com.mediclinic.appointment_scheduler.domain.response.schedule;

import java.time.LocalDate;

import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;
import com.mediclinic.appointment_scheduler.util.constant.ScheduleStatusEnum;

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
    private ScheduleDoctor doctor;
    private LocalDate workDate;
    private String timeSlot;
    private ScheduleStatusEnum status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleDoctor {
        Long id;
        String name;
    }

    public static ResScheduleDTO mapEntityScheduleToDTO(Schedule schedule) {
        return new ResScheduleDTO(
                schedule.getId(),
                new ScheduleDoctor(schedule.getDoctor().getId(), schedule.getDoctor().getFullName()),
                schedule.getWorkDate(),
                schedule.getTimeSlot(),
                schedule.getStatus());
    }
}
