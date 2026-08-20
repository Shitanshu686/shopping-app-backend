package com.shitanshu.shopping.service;
import com.shitanshu.shopping.exception.ResourceAlreadyExistsException;
import com.shitanshu.shopping.model.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.shitanshu.shopping.repository.UserRepository;
import com.shitanshu.shopping.dto.UserRequestDTO;
import com.shitanshu.shopping.dto.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.shitanshu.shopping.dto.LoginRequestDTO;
import java.util.Optional;
import com.shitanshu.shopping.exception.InvalidCredentialsException;
import com.shitanshu.shopping.security.JwtUtil;
import com.shitanshu.shopping.dto.LoginResponseDTO;
import com.shitanshu.shopping.dto.ChangePasswordRequestDTO;
import com.shitanshu.shopping.exception.BadRequestException;
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtUtil jwtUtil;
	public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
		if (userRepository.existsByEmail(userRequestDTO.getEmail())) {

		    throw new ResourceAlreadyExistsException(
		            "User with email '" + userRequestDTO.getEmail() + "' already exists");

		}
		User user = new User();
		user.setName(userRequestDTO.getName());

		user.setEmail(userRequestDTO.getEmail());

		user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

		user.setRole("USER");
		  userRepository.save(user);
		  UserResponseDTO response = new UserResponseDTO();

		    response.setId(user.getId());
		    response.setName(user.getName());
		    response.setEmail(user.getEmail());
		    response.setRole(user.getRole());
		    response.setDarkMode(user.getDarkMode());
		    return response;
	}
	public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
		Optional<User> optionalUser =
		        userRepository.findByEmail(loginRequestDTO.getEmail());
		if (optionalUser.isEmpty()) {

			throw new InvalidCredentialsException("Invalid Email or Password");

		}
		User user = optionalUser.get();
		if (!passwordEncoder.matches(
		        loginRequestDTO.getPassword(),
		        user.getPassword())) {

		    throw new InvalidCredentialsException("Invalid Email or Password");

		}
		UserResponseDTO response = new UserResponseDTO();

		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setDarkMode(user.getDarkMode());
		String token =
			    jwtUtil.generateToken(
			        user.getEmail(),
			        user.getRole()
			    );
		return new LoginResponseDTO(token, response);
	}
	public void changePassword(
	        String email,
	        ChangePasswordRequestDTO request) {

	    // ======================
	    // FIND USER
	    // ======================

	    Optional<User> optionalUser =
	            userRepository.findByEmail(email);

	    if (optionalUser.isEmpty()) {

	        throw new InvalidCredentialsException(
	                "User not found"
	        );

	    }

	    User user =
	            optionalUser.get();


	    // ======================
	    // VERIFY CURRENT PASSWORD
	    // ======================

	    if (!passwordEncoder.matches(
	            request.getCurrentPassword(),
	            user.getPassword())) {

	        throw new InvalidCredentialsException(
	                "Current password is incorrect"
	        );

	    }


	    // ======================
	    // CONFIRM NEW PASSWORD
	    // ======================

	    if (!request.getNewPassword().equals(
	            request.getConfirmPassword())) {

	    	throw new BadRequestException(
	    	        "New password and confirm password do not match"
	    	);

	    }


	    // ======================
	    // PREVENT SAME PASSWORD
	    // ======================

	    if (passwordEncoder.matches(
	            request.getNewPassword(),
	            user.getPassword())) {

	    	throw new BadRequestException(
	    	        "New password must be different from current password"
	    	);

	    }


	    // ======================
	    // HASH NEW PASSWORD
	    // ======================

	    String encodedPassword =
	            passwordEncoder.encode(
	                    request.getNewPassword()
	            );


	    // ======================
	    // UPDATE PASSWORD
	    // ======================

	    user.setPassword(
	            encodedPassword
	    );

	    userRepository.save(user);

	}
	public void updateDarkMode(
	        String email,
	        Boolean darkMode) {

	    Optional<User> optionalUser =
	            userRepository.findByEmail(email);

	    if (optionalUser.isEmpty()) {

	        throw new InvalidCredentialsException(
	                "User not found"
	        );

	    }

	    User user =
	            optionalUser.get();

	    user.setDarkMode(darkMode);

	    userRepository.save(user);
	}
}