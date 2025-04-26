package com.mediclinic.appointment_scheduler.domain.response.doctor;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.Specialty;
import com.mediclinic.appointment_scheduler.domain.response.specialty.ResSpecialtyDTO;

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
    private SpecialtyDoctor specialty;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDoctor {
        private Long id;
        private String name;
    }

    public static ResDoctorDTO mapEntityDoctorToDTO(Doctor doctor) {
        return new ResDoctorDTO(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getDescription(),
                doctor.getImageUrl(),
                doctor.isActive(),

                doctor.getSpecialty() != null
                        ? new SpecialtyDoctor(doctor.getSpecialty().getId(), doctor.getSpecialty().getName())
                        : null);
    }
}
