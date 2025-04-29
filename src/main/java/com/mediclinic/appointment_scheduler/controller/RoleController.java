package com.mediclinic.appointment_scheduler.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.Role;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.service.RoleService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a role")
    public ResponseEntity<Role> createARole(@Valid @RequestBody Role rolePM) {
        return ResponseEntity.ok().body(this.roleService.handleCreateARole(rolePM));
    }

    @PutMapping("/roles")
    @ApiMessage("update a role")
    public ResponseEntity<Role> updateARole(@RequestBody Role rolePM) {
        return ResponseEntity.ok().body(this.roleService.handleUpdateARole(rolePM));
    }

    @GetMapping("/roles")
    @ApiMessage("fetch all")
    public ResponseEntity<ResPaginationDTO> fetchAllRole(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.handleFetchAllRole(spec, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> deleteARole(@PathVariable("id") String id) {
        for (char c : id.toCharArray()) {
            if (Character.isAlphabetic(c))
                throw new IdInvalidException("Id không hợp lệ");
        }
        Long idL = Long.parseLong(id);
        this.roleService.handleDeleteARole(idL);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.roleService.findById(id));
    }

}
