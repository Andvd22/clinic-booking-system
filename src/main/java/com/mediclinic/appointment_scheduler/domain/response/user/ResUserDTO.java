package com.mediclinic.appointment_scheduler.domain.response.user;

import java.time.Instant;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.util.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {

    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private RoleUser role;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }

    public static ResUserDTO mapEntityUserToDTO(User user) {
        return new ResUserDTO(user.getId(), user.getEmail(), user.getName(), user.getGender(), user.getAddress(),
                user.getAge(), user.getCreatedAt(), new RoleUser(user.getRole().getId(), user.getRole().getName()));
    }

}