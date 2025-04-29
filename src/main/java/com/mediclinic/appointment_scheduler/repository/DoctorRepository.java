package com.mediclinic.appointment_scheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d " +
            "LEFT JOIN d.specialty s " +
            "WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchByFullNameOrSpecialty(@Param("keyword") String keyword);
}