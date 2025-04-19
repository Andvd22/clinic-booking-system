package com.mediclinic.appointment_scheduler.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.service.UserService;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api/v1")

public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<User> createNewUser(
            @Valid @RequestBody User postManUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postManUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPashword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPashword);
        User bbuser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(bbuser);
    }

    @DeleteMapping("/users/{xoatheoid}")
    @ApiMessage("Delete user")
    public ResponseEntity<User> deleteUser(
            @PathVariable("xoatheoid") long andeptrai) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(andeptrai);
        if (currentUser == null) {
            throw new IdInvalidException("User với id " + andeptrai + " không tồn tại");
        }
        User bbuser = this.userService.deleteUser(andeptrai);
        // return ResponseEntity.ok(bbuser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bbuser);
    }

    @GetMapping("/users/{idofuser}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<User> getUserById(
            @PathVariable("idofuser") long id) {
        User bbuser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bbuser);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<List<User>> getAllUser(Pageable pageable, @Filter Specification<User> spec) {
        List<User> bbusers = this.userService.fetchAllUser(pageable, spec);
        return ResponseEntity.status(HttpStatus.OK).body(bbusers);
    }

    @PutMapping("/users")
    @ApiMessage("update user")
    public ResponseEntity<User> updateUser(@RequestBody User postManUDUser) {
        User bbuser = this.userService.updateUser(postManUDUser);
        return ResponseEntity.status(HttpStatus.OK).body(bbuser);
    }

}
