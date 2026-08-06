package com.shitanshu.shopping.controller;
import com.shitanshu.shopping.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.shitanshu.shopping.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.shitanshu.shopping.dto.LoginRequestDTO;
import java.time.LocalDateTime;
import com.shitanshu.shopping.dto.UserRequestDTO;
import com.shitanshu.shopping.dto.UserResponseDTO;
import com.shitanshu.shopping.dto.LoginResponseDTO;
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(
	        @RequestBody UserRequestDTO userRequestDTO) {

	    UserResponseDTO response = userService.registerUser(userRequestDTO);

	    ApiResponse<UserResponseDTO> apiResponse =
	            new ApiResponse<>(
	                    true,
	                    "User registered successfully",
	                    response,
	                    java.time.LocalDateTime.now());

	    return ResponseEntity.ok(apiResponse);
	}
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
	                    LocalDateTime.now());

	    return ResponseEntity.ok(apiResponse);

	}
}