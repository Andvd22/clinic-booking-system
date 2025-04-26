package com.mediclinic.appointment_scheduler.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Specialty;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.specialty.ResSpecialtyDTO;

public interface SpecialtyService {
    ResSpecialtyDTO createSpecialty(Specialty specialty);

    ResSpecialtyDTO updateSpecialty(Specialty specialty);

    void deleteSpecialty(Long id);

    ResSpecialtyDTO fetchSpecialtyById(Long id);

    ResPaginationDTO fetchAllSpecialty(Pageable pageable, Specification<Specialty> spec);

}
