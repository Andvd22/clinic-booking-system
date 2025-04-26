package com.mediclinic.appointment_scheduler.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.schedule.ResScheduleDTO;

public interface ScheduleService {
    ResScheduleDTO createSchedule(Schedule chedule);

    ResScheduleDTO updateSchedule(Schedule schedule);

    void deleteSchedule(Long id);

    ResScheduleDTO fetchScheduleById(Long id);

    ResPaginationDTO fetchAllSchedules(Pageable pageable, Specification<Schedule> spec);

    List<ResScheduleDTO> getSchedulesByDoctorAndDate(Long doctorId, LocalDate workDate);

    boolean isExistsTimeSlotBuildQuery(Schedule schedule);
}
