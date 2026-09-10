package com.expensetracker.expense_tracker_api.controller;

import com.expensetracker.expense_tracker_api.dto.AuthRequest;
import com.expensetracker.expense_tracker_api.dto.AuthResponse;
import com.expensetracker.expense_tracker_api.dto.RegisterRequest;
import com.expensetracker.expense_tracker_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}