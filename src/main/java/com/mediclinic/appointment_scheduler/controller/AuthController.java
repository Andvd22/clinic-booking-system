package com.mediclinic.appointment_scheduler.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.appointment_scheduler.domain.User;
import com.mediclinic.appointment_scheduler.domain.request.ReqLoginDTO;
import com.mediclinic.appointment_scheduler.domain.response.ResLoginDTO;
import com.mediclinic.appointment_scheduler.service.UserService;
import com.mediclinic.appointment_scheduler.util.SecurityUtil;
import com.mediclinic.appointment_scheduler.util.annotation.ApiMessage;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // set thông tin người dùng lưu vào context của spring security và có thể truy
        // cập ở bất cứu đâu để lấy ra thông tin( có thể dùng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }

        // String access_token =
        // this.securityUtil.createAccessToken(authentication,res);an
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);

        // update user
        this.userService.updateUserToken(refresh_token, loginDto.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUserDB != null) {
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setId(currentUserDB.getId());
            userLogin.setName(currentUserDB.getName());
            userLogin.setRole(currentUserDB.getRole());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("get new refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken) throws IdInvalidException {
        // if (refreshToken.equals("abc")) {
        // throw new IdInvalidException("Bạn không có refreshToken ở cookie");
        // }
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();
        // check user by token + email
        User currentUser = this.userService.fetchUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("refreshToken không hợp lệ");
        }
        // issue new token/ set refreshToken as cookies
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }

        // String access_token =
        // this.securityUtil.createAccessToken(authentication,res);an
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        // update user
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logOut() throws IdInvalidException {
        String emailOfCurrUser = this.securityUtil.getCurrentUserLogin().orElse(null);
        if (emailOfCurrUser == null) {
            throw new IdInvalidException("Nguời dùng này hiện đang không đăng nhập");
        }
        // set refreshToken =""
        if (this.userService.handleGetUserByUsername(emailOfCurrUser).getRefreshToken() == null) {
            throw new IdInvalidException("người dùng này đã log out");
        }
        this.userService.updateRefreshTokenLogOut(emailOfCurrUser);
        // this.userService.updateUserToken(null, emailOfCurrUser);
        // set cookies
        ResponseCookie deleteCookies = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookies.toString()).body(null);
    }

    // @PostMapping("/auth/register")

    // }

}