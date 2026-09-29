package com.foodrescue.network.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodrescue.network.dto.UserRegistrationDTO;
import com.foodrescue.network.model.User;
import com.foodrescue.network.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDTO dto) {
        User registerUser = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }
}
