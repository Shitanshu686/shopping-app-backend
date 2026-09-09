package com.shitanshu.userservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shitanshu.userservice.dto.UserRequestDTO;
import com.shitanshu.userservice.dto.UserResponseDTO;
import com.shitanshu.userservice.exception.ResourceAlreadyExistsException;
import com.shitanshu.userservice.exception.UserNotFoundException;
import com.shitanshu.userservice.model.User;
import com.shitanshu.userservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return mapToResponse(user);
    }

    public UserResponseDTO addUser(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setDarkMode(request.getDarkMode());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO request) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());
        existingUser.setRole(request.getRole());
        existingUser.setDarkMode(request.getDarkMode());

        User updatedUser = userRepository.save(existingUser);

        return mapToResponse(updatedUser);
    }

    public void deleteUser(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserResponseDTO mapToResponse(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getDarkMode()
        );
    }
}