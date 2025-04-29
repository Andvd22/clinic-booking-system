package com.mediclinic.appointment_scheduler.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mediclinic.appointment_scheduler.domain.Permission;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;

public interface PermissionService {
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);

    Permission findById(Long id);

    Permission handleCreateAPermission(Permission permissionPM);

    Permission handleUpdateAPermission(Permission permissionPM);

    ResPaginationDTO handleFetchAllPermission(Specification spec, Pageable pageable);

    void handleDeleteAPermission(Long id);
}
