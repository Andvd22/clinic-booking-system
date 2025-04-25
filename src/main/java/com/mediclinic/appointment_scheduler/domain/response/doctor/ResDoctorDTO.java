package com.mediclinic.appointment_scheduler.domain.response.doctor;

import com.mediclinic.appointment_scheduler.domain.Doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResDoctorDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String description;
    private String imageUrl;
    private boolean active;

    public static ResDoctorDTO mapEntityDoctorToDTO(Doctor doctor) {
        return new ResDoctorDTO(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getDescription(),
                doctor.getImageUrl(),
                doctor.isActive());
    }
}
