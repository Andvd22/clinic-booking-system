package com.mediclinic.appointment_scheduler.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.user.ResUserDTO;
import com.mediclinic.appointment_scheduler.repository.RoleRepository;
import com.mediclinic.appointment_scheduler.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResUserDTO handleCreateUser(User user) {

        if (user.getRole() != null) {
            user.setRole(this.roleRepository.findById(user.getRole().getId()).orElse(null));
        }
        User userDB = this.userRepository.save(user);
        return ResUserDTO.mapEntityUserToDTO(userDB);
    }

    public void deleteUser(long id) {
        Optional<User> userOp = this.userRepository.findById(id);
        if (userOp.isPresent()) {
            User user = userOp.get();
            this.userRepository.deleteById(id);
        }
    }

    public User fetchUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public ResPaginationDTO fetchAllUser(Pageable pageable, Specification<User> spec) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResPaginationDTO.Meta mt = new ResPaginationDTO.Meta();
        ResPaginationDTO rp = new ResPaginationDTO();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rp.setMeta(mt);
        List<ResUserDTO> list = pageUser.getContent()
                .stream()
                .map(item -> ResUserDTO.mapEntityUserToDTO(item))
                .collect(Collectors.toList());
        rp.setResult(list);
        return rp;
    }

    public ResUserDTO updateUser(User user) {
        User userUD = this.fetchUserById(user.getId());
        if (userUD != null) {
            userUD.setName(user.getName());
            userUD.setGender(user.getGender());
            userUD.setAge(user.getAge());
            userUD.setAddress(user.getAddress());
            userUD = this.userRepository.save(userUD);
            if (user.getRole() != null) {
                userUD.setRole(this.roleRepository.findById(user.getRole().getId()).orElse(null));
            }
        }
        return ResUserDTO.mapEntityUserToDTO(userUD);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User fetchUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    public void updateRefreshTokenLogOut(String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(null);
            this.userRepository.save(currentUser);
        }
    }

}
