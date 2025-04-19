// package com.mediclinic.appointment_scheduler.domain;

// import java.time.Instant;
// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.mediclinic.appointment_scheduler.util.SecurityUtil;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Table(name = "specialties")
// @Getter
// @Setter
// public class Specialty {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// private String name;

// @Column(columnDefinition = "MEDIUMTEXT")
// private String description;

// private String imageUrl;
// private boolean active = true;

// private Instant createdAt;
// private Instant updatedAt;
// private String createdBy;
// private String updatedBy;

// @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
// @JsonIgnore
// private List<Doctor> doctors;

// @PrePersist
// public void handleBeforeCreate() {
// this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
// ? SecurityUtil.getCurrentUserLogin().get()
// : "";
// this.createdAt = Instant.now();
// }

// @PreUpdate
// public void handleBeforeUpdate() {
// this.updatedAt = Instant.now();
// this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
// ? SecurityUtil.getCurrentUserLogin().get()
// : "";
// }
// }