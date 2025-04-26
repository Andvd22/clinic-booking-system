package com.mediclinic.appointment_scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mediclinic.appointment_scheduler.domain.Specialty;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long>, JpaSpecificationExecutor<Specialty> {
    boolean existsByName(String name);
}
