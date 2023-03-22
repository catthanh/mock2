package com.example.mock2.security;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.security.dto.request.LoginRequest;
import com.example.mock2.security.dto.request.RefreshRequest;
import com.example.mock2.security.dto.request.RegisterRequest;
import com.example.mock2.security.dto.response.LoginResponse;
import com.example.mock2.user.UserService;
import com.example.mock2.user.dto.request.UserRequest;
import com.example.mock2.user.dto.response.UserResponse;
import com.example.mock2.user.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest request) {
        return Response.success(authService.login(request));
    }

    @PostMapping("/register")
    public Response<UserResponse> create(
            @Valid
            @RequestBody UserRequest userRequest) {
        User user = userService.create(userRequest);
        return Response.success(UserResponse.of(user));
    }
    @PostMapping("/refresh")
    public Response refresh(@RequestBody RefreshRequest request) {
        return Response.success(authService.refresh(request));
    }

}
