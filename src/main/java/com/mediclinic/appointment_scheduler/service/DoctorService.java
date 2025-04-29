package com.mediclinic.appointment_scheduler.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;

public interface DoctorService {
    ResDoctorDTO createDoctor(Doctor doctor);

    ResDoctorDTO updateDoctor(Doctor doctor);

    void deleteDoctor(Long id);

    ResDoctorDTO fetchDoctorById(Long id);

    ResPaginationDTO fetchAllDoctors(Pageable pageable, Specification<Doctor> specification);

    List<Doctor> searchByFullNameOrSpecialty(String keyword);
}
