package com.mediclinic.appointment_scheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mediclinic.appointment_scheduler.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);

    boolean existsByApiPathAndMethodAndModuleAndIdNot(String apiPath, String method, String module, Long id);

    List<Permission> findByIdIn(List<Long> id);
}
