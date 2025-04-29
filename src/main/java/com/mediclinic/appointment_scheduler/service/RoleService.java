package com.mediclinic.appointment_scheduler.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Role;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;

public interface RoleService {
    boolean existsByNameRole(String nameRole);

    Role findById(Long id);

    Role handleCreateARole(Role rolePM);

    Role handleUpdateARole(Role rolePM);

    ResPaginationDTO handleFetchAllRole(Specification spec, Pageable pageable);

    void handleDeleteARole(Long id);
}
