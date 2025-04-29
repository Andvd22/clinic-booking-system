package com.mediclinic.appointment_scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.Permission;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.service.PermissionService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create a")
    public ResponseEntity<Permission> createAPermission(@Valid @RequestBody Permission permissionPM) {
        Permission permissionDB = this.permissionService.handleCreateAPermission(permissionPM);
        return ResponseEntity.ok().body(permissionDB);
    }

    @PutMapping("/permissions")
    @ApiMessage("update a")
    public ResponseEntity<Permission> updateAPermission(@RequestBody Permission permissionPM) {
        Permission permissionDB = this.permissionService.handleUpdateAPermission(permissionPM);
        return ResponseEntity.ok().body(permissionDB);
    }

    @GetMapping("/permissions")
    @ApiMessage("fetch all")
    public ResponseEntity<ResPaginationDTO> fetchAllPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.handleFetchAllPermission(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> deleteARole(@PathVariable("id") Long id) {
        this.permissionService.handleDeleteAPermission(id);

        return ResponseEntity.ok().body(null);
    }

}
