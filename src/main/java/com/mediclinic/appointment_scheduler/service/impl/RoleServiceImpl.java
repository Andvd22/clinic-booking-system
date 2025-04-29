package com.mediclinic.appointment_scheduler.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Permission;
import com.mediclinic.appointment_scheduler.domain.Role;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.repository.PermissionRepository;
import com.mediclinic.appointment_scheduler.repository.RoleRepository;
import com.mediclinic.appointment_scheduler.service.RoleService;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.mediclinic.appointment_scheduler.util.error.ResourceAlreadyExistsException;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean existsByNameRole(String nameRole) {
        return this.roleRepository.existsByName(nameRole);
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElseThrow(() -> new IdInvalidException("id không hợp lệ"));
    }

    @Override
    public Role handleCreateARole(Role rolePM) {
        if (this.existsByNameRole(rolePM.getName())) {
            throw new ResourceAlreadyExistsException("role đã tồn tại");
        }
        List<Permission> permissionsPM = rolePM.getPermissions().stream()
                .map(permission -> permission.getId())
                .distinct()
                .map(id -> this.permissionRepository.findById(id))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .collect(Collectors.toList());
        rolePM.setPermissions(permissionsPM);
        return this.roleRepository.save(rolePM);
    }

    @Override
    public Role handleUpdateARole(Role rolePM) {
        Role roleDB = this.findById(rolePM.getId());
        if (!rolePM.getName().equals(roleDB.getName())) {
            if (this.existsByNameRole(rolePM.getName())) {
                throw new ResourceAlreadyExistsException("role đã tồn tại");
            }
        }
        roleDB.setName(rolePM.getName());
        roleDB.setActive(rolePM.isActive());
        roleDB.setDescription(rolePM.getDescription());
        List<Permission> permissionsPM = rolePM.getPermissions().stream()
                .map(permission -> permission.getId())
                .distinct()
                .map(id -> this.permissionRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        roleDB.setPermissions(permissionsPM);
        return this.roleRepository.save(roleDB);
    }

    @Override
    public ResPaginationDTO handleFetchAllRole(Specification spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResPaginationDTO rp = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageRole.getTotalPages());
        meta.setTotal(pageRole.getTotalElements());
        rp.setMeta(meta);
        rp.setResult(pageRole.getContent());
        return rp;

    }

    @Override
    public void handleDeleteARole(Long id) {
        Role roleDB = this.findById(id);
        this.roleRepository.delete(roleDB);
    }
}
