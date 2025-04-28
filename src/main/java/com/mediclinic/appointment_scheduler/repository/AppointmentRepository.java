package com.mediclinic.appointment_scheduler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mediclinic.appointment_scheduler.domain.Appointment;
import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.util.constant.AppointmentStatusEnum;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByUser(User user);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByStatus(AppointmentStatusEnum status);

    Optional<Appointment> findBySchedule(Schedule schedule);

    boolean existsBySchedule(Schedule schedule);

}