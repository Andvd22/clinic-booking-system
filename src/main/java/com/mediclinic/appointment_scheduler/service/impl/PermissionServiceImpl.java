package com.mediclinic.appointment_scheduler.service.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Permission;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.repository.PermissionRepository;
import com.mediclinic.appointment_scheduler.service.PermissionService;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.mediclinic.appointment_scheduler.util.error.ResourceNotFoundException;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(apiPath, method, module);
    }

    @Override
    public Permission findById(Long id) {
        return this.permissionRepository.findById(id).orElseThrow(() -> new IdInvalidException("id không tồn tại"));
    }

    @Override
    public Permission handleCreateAPermission(Permission permissionPM) {
        if (this.existsByApiPathAndMethodAndModule(permissionPM.getApiPath(), permissionPM.getMethod(),
                permissionPM.getModule()))
            throw new ResourceNotFoundException("permission này đã tồn tại");
        return this.permissionRepository.save(permissionPM);
    }

    @Override
    public Permission handleUpdateAPermission(Permission permissionPM) {
        Permission permissionDB = this.findById(permissionPM.getId());
        boolean exists = permissionRepository.existsByApiPathAndMethodAndModuleAndIdNot(
                permissionPM.getApiPath(),
                permissionPM.getMethod(),
                permissionPM.getModule(),
                permissionPM.getId() // Không tính permission hiện tại
        );

        if (exists) {
            throw new ResourceNotFoundException("Permission này đã tồn tại");
        }
        permissionDB.setName(permissionPM.getName());
        permissionDB.setApiPath(permissionPM.getApiPath());
        permissionDB.setMethod(permissionPM.getMethod());
        permissionDB.setMethod(permissionPM.getMethod());
        return this.permissionRepository.save(permissionDB);
    }

    @Override
    public ResPaginationDTO handleFetchAllPermission(Specification spec, Pageable pageable) {
        ResPaginationDTO rp = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());
        rp.setMeta(meta);
        rp.setResult(pagePermission.getContent());
        return rp;
    }

    @Override
    public void handleDeleteAPermission(Long id) {
        Permission permissionDB = this.findById(id);
        permissionDB.getRoles().stream()
                .peek(role -> role.getPermissions().remove(permissionDB))
                .collect(Collectors.toList());
        this.permissionRepository.delete(permissionDB);
    }
}
