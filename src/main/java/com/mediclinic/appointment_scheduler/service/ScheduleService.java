package com.mediclinic.appointment_scheduler.service;

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

    boolean isExistsTimeSlotBuildQuery(Schedule schedule);
}
