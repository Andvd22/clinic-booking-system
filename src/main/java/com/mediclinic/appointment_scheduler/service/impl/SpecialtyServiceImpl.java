package com.mediclinic.appointment_scheduler.service.impl;

import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Specialty;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.specialty.ResSpecialtyDTO;
import com.mediclinic.appointment_scheduler.repository.SpecialtyRepository;
import com.mediclinic.appointment_scheduler.service.SpecialtyService;
import com.mediclinic.appointment_scheduler.util.error.ResourceAlreadyExistsException;
import com.mediclinic.appointment_scheduler.util.error.ResourceNotFoundException;
import com.turkraft.springfilter.boot.Filter;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public ResSpecialtyDTO createSpecialty(Specialty specialty) {
        if (specialtyRepository.existsByName(specialty.getName())) {
            throw new ResourceAlreadyExistsException("Tên chuyên khoa đã tồn tại");
        }

        Specialty savedSpecialty = this.specialtyRepository.save(specialty);
        return ResSpecialtyDTO.mapEntitySpecialtyToDTO(savedSpecialty);
    }

    @Override
    public ResSpecialtyDTO updateSpecialty(Specialty specialty) {
        Specialty existingSpecialty = this.specialtyRepository.findById(specialty.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa"));

        if (!existingSpecialty.getName().equals(specialty.getName())
                && this.specialtyRepository.existsByName(specialty.getName())) {
            throw new ResourceAlreadyExistsException("Tên chuyên khoa đã tồn tại");
        }

        existingSpecialty.setName(specialty.getName());
        existingSpecialty.setDescription(specialty.getDescription());

        Specialty updatedSpecialty = this.specialtyRepository.save(existingSpecialty);
        return ResSpecialtyDTO.mapEntitySpecialtyToDTO(updatedSpecialty);
    }

    @Override
    public void deleteSpecialty(Long id) {
        Specialty specialty = this.specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa"));

        // if (!specialty.getDoctors().isEmpty()) {
        // throw new ResourceAlreadyExistsException("Không thể xóa chuyên khoa đang có
        // bác sĩ");
        // }

        specialtyRepository.delete(specialty);
    }

    @Override
    public ResSpecialtyDTO fetchSpecialtyById(Long id) {
        Specialty specialty = this.specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa"));
        return ResSpecialtyDTO.mapEntitySpecialtyToDTO(specialty);
    }

    @Override
    public ResPaginationDTO fetchAllSpecialty(Pageable pageable, @Filter Specification<Specialty> spec) {
        Page<Specialty> pageSpecialties = this.specialtyRepository.findAll(spec, pageable);

        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize((pageable.getPageSize()));
        meta.setPages(pageSpecialties.getTotalPages());
        meta.setTotal(pageSpecialties.getTotalElements());
        res.setMeta(meta);

        res.setResult(pageSpecialties.getContent().stream()
                .map(ResSpecialtyDTO::mapEntitySpecialtyToDTO)
                .collect(Collectors.toList()));

        return res;
    }
}
