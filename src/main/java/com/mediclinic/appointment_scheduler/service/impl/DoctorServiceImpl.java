package com.mediclinic.appointment_scheduler.service.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;
import com.mediclinic.appointment_scheduler.repository.DoctorRepository;
import com.mediclinic.appointment_scheduler.repository.SpecialtyRepository;
import com.mediclinic.appointment_scheduler.service.DoctorService;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.mediclinic.appointment_scheduler.util.error.ResourceAlreadyExistsException;
import com.mediclinic.appointment_scheduler.util.error.ResourceNotFoundException;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public ResDoctorDTO createDoctor(Doctor doctorPM) {
        if (doctorRepository.existsByEmail(doctorPM.getEmail())) {
            throw new ResourceAlreadyExistsException("Email đã tồn tại");
        }
        doctorPM.setActive(true);
        if (doctorPM.getSpecialty() != null) {
            doctorPM.setSpecialty(this.specialtyRepository.findById(doctorPM.getSpecialty().getId()).orElse(null));
        }
        Doctor doctorDB = this.doctorRepository.save(doctorPM);
        return ResDoctorDTO.mapEntityDoctorToDTO(doctorDB);
    }

    @Override
    public ResDoctorDTO updateDoctor(Doctor doctorPM) {
        if (doctorPM.getId() < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Doctor doctorDB = this.doctorRepository.findById(doctorPM.getId())
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại bác sĩ này"));
        doctorDB.setFullName(doctorPM.getFullName());
        doctorDB.setEmail(doctorPM.getEmail());
        doctorDB.setPhone(doctorPM.getPhone());
        doctorDB.setDescription(doctorPM.getDescription());
        doctorDB.setImageUrl(doctorPM.getImageUrl());
        if (doctorPM.getSpecialty() != null) {
            doctorDB.setSpecialty(this.specialtyRepository.findById(doctorPM.getSpecialty().getId()).orElse(null));
        }
        this.doctorRepository.save(doctorDB);
        return ResDoctorDTO.mapEntityDoctorToDTO(doctorDB);
    }

    @Override
    public void deleteDoctor(Long id) {
        if (id < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Doctor doctorDB = this.doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại bác sĩ này"));
        this.doctorRepository.delete(doctorDB);
    }

    @Override
    public ResDoctorDTO fetchDoctorById(Long id) {
        if (id < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Doctor doctorDB = this.doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại bác sĩ này"));
        return ResDoctorDTO.mapEntityDoctorToDTO(doctorDB);
    }

    @Override
    public ResPaginationDTO fetchAllDoctors(Pageable pageable, Specification<Doctor> spec) {
        Page<Doctor> pageDoctors = this.doctorRepository.findAll(spec, pageable);
        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize((pageable.getPageSize()));
        meta.setPages(pageDoctors.getTotalPages());
        meta.setTotal((pageDoctors.getTotalElements()));
        res.setMeta(meta);
        List<ResDoctorDTO> resDoctorDTOs = pageDoctors.getContent().stream()
                .map(doctor -> ResDoctorDTO.mapEntityDoctorToDTO(doctor))
                .collect(Collectors.toList());
        res.setResult(resDoctorDTOs);
        return res;
    }

    @Override
    public List<Doctor> searchByFullNameOrSpecialty(String keyword) {
        return doctorRepository.searchByFullNameOrSpecialty(keyword);
    }
}
