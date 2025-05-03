package com.mediclinic.appointment_scheduler.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediclinic.appointment_scheduler.domain.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    @Query("SELECT s FROM Schedule s WHERE s.doctor.id = :doctorId AND s.workDate = :workDate ")
    List<Schedule> findAllSchedulesByQuery(@Param("doctorId") Long doctorId, @Param("workDate") LocalDate workDate);

    @Query("SELECT s FROM Schedule s WHERE s.doctor.id = :doctorId AND s.workDate = :workDate ORDER BY s.timeSlot ASC")
    Page<Schedule> findSchedulesByDoctorAndDate(@Param("doctorId") Long doctorId,
            @Param("workDate") LocalDate workDate, Pageable pageable);
}