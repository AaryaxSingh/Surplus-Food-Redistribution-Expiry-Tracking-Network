package com.foodrescue.network.service;

import org.springframework.stereotype.Service;

import com.foodrescue.network.dto.UserRegistrationDTO;
import com.foodrescue.network.model.User;
import com.foodrescue.network.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already registered!");
        }

        User newUser = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .build();

        return userRepository.save(newUser);
    }

}
