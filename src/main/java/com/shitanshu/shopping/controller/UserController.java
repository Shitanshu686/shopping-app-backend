package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shitanshu.shopping.dto.LoginRequestDTO;
import com.shitanshu.shopping.dto.UserRequestDTO;
import com.shitanshu.shopping.dto.UserResponseDTO;
import com.shitanshu.shopping.dto.LoginResponseDTO;
import com.shitanshu.shopping.dto.ChangePasswordRequestDTO;
import com.shitanshu.shopping.dto.DarkModeRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;


    // =========================================================
    // REGISTER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(
            @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO response =
                userService.registerUser(userRequestDTO);

        ApiResponse<UserResponseDTO> apiResponse =
                new ApiResponse<>(
                        true,
                        "User registered successfully",
                        response,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(apiResponse);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginUser(
            @RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResponseDTO response =
                userService.loginUser(loginRequestDTO);

        ApiResponse<LoginResponseDTO> apiResponse =
                new ApiResponse<>(
                        true,
                        "Login Successful",
                        response,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(apiResponse);
    }


    // =========================================================
    // PROFILE
    // =========================================================

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<String>> profile() {

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "JWT Authentication Working",
                        "User is authenticated",
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(apiResponse);
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO request,
            Authentication authentication) {

        String email =
                authentication.getName();

        userService.changePassword(
                email,
                request
        );

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Password changed successfully",
                        null,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(apiResponse);
    }


    // =========================================================
    // DARK MODE
    // =========================================================

    @PutMapping("/preferences/dark-mode")
    public ResponseEntity<ApiResponse<Boolean>> updateDarkMode(
            @Valid @RequestBody DarkModeRequestDTO request,
            Authentication authentication) {

        String email =
                authentication.getName();

        userService.updateDarkMode(
                email,
                request.getDarkMode()
        );

        ApiResponse<Boolean> apiResponse =
                new ApiResponse<>(
                        true,
                        "Dark mode preference updated successfully",
                        request.getDarkMode(),
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(apiResponse);
    }


    // =========================================================
    // ADMIN USER MANAGEMENT
    // =========================================================


    // =========================
    // GET ALL USERS
    // =========================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {

        List<UserResponseDTO> users =
                userService.getAllUsers();

        ApiResponse<List<UserResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Users fetched successfully",
                        users,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // GET USER BY ID
    // =========================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable Integer userId) {

        UserResponseDTO user =
                userService.getUserById(userId);

        ApiResponse<UserResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "User fetched successfully",
                        user,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // UPDATE USER ROLE
    // =========================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/admin/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUserRole(
            @PathVariable Integer userId,
            @RequestParam String role,
            Authentication authentication) {

        UserResponseDTO user =
                userService.updateUserRole(
                        userId,
                        role,
                        authentication
                );

        ApiResponse<UserResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "User role updated successfully",
                        user,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // DELETE USER
    // =========================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/admin/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Integer userId,
            Authentication authentication) {

        userService.deleteUser(
                userId,
                authentication
        );

        ApiResponse<String> response =
                new ApiResponse<>(
                        true,
                        "User deleted successfully",
                        null,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }

}