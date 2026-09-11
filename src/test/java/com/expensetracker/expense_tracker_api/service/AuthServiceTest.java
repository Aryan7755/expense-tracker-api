package com.expensetracker.expense_tracker_api.service;

import com.expensetracker.expense_tracker_api.dto.AuthRequest;
import com.expensetracker.expense_tracker_api.dto.AuthResponse;
import com.expensetracker.expense_tracker_api.dto.RegisterRequest;
import com.expensetracker.expense_tracker_api.entity.User;
import com.expensetracker.expense_tracker_api.repository.UserRepository;
import com.expensetracker.expense_tracker_api.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;

    @InjectMocks private AuthService authService;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Aryan");
        request.setEmail("aryan@example.com");
        request.setPassword("pass123");

        when(userRepository.findByEmail("aryan@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(jwtUtils.generateToken("aryan@example.com")).thenReturn("mockedJwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mockedJwtToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("aryan@example.com");
        request.setPassword("wrongPass");

        User user = User.builder().email("aryan@example.com").password("encodedPass").build();

        when(userRepository.findByEmail("aryan@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}