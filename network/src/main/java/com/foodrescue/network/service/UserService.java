package com.foodrescue.network.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.foodrescue.network.dto.UserRegistrationDTO;
import com.foodrescue.network.model.User;
import com.foodrescue.network.model.UserRole;
import com.foodrescue.network.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already registered!");
        }

        User newUser = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .build();

        return userRepository.save(newUser);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;
    }

    public List<User> getUsersByRole(UserRole role) {
        if (role != null) {
            return userRepository.findByRole(role);
        }
        return userRepository.findAll();
    }
}
