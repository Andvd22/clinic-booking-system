package com.mediclinic.appointment_scheduler.domain.response.specialty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mediclinic.appointment_scheduler.domain.Specialty;
import com.mediclinic.appointment_scheduler.domain.response.doctor.ResDoctorDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResSpecialtyDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private List<SpecialtyDoctor> doctors;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDoctor {
        private Long id;
        private String name;
    }

    public static ResSpecialtyDTO mapEntitySpecialtyToDTO(Specialty s) {
        return new ResSpecialtyDTO(
                s.getId(),
                s.getName(),
                s.getDescription(),
                s.getImageUrl(),
                s.getDoctors() != null ? s.getDoctors().stream()
                        .map(doctor -> new SpecialtyDoctor(doctor.getId(), doctor.getFullName()))
                        .collect(Collectors.toList())
                        : new ArrayList<>()

        );
    }
}
