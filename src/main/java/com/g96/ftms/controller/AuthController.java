package com.g96.ftms.controller;

import com.g96.ftms.dto.JwtResponeDTO;
import com.g96.ftms.dto.LoginDTO;
import com.g96.ftms.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponeDTO> login(@RequestBody LoginDTO loginDTO) {
        JwtResponeDTO jwtResponse = authService.authenticateUser(loginDTO);
        return ResponseEntity.ok(jwtResponse);
    }
}
