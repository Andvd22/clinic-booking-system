package com.mediclinic.appointment_scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public User deleteUser(long id) {
        Optional<User> userOp = this.userRepository.findById(id);
        if (userOp.isPresent()) {
            User user = userOp.get();
            this.userRepository.deleteById(id);
            return user;
        }
        return null;
    }

    public User fetchUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public ResPaginationDTO fetchAllUser(Pageable pageable, Specification<User> spec) {
        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        res.setMeta(meta);
        res.setResult(pageUser.getContent());
        return res;
    }

    public User updateUser(User user) {
        User userUD = this.fetchUserById(user.getId());
        if (userUD != null) {
            userUD.setName(user.getName());
            userUD.setGender(user.getGender());
            userUD.setAge(user.getAge());
            userUD.setAddress(user.getAddress());
            userUD.setPhone(user.getPhone());
            userUD = this.userRepository.save(userUD);
        }
        return userUD;
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
