package com.example.mock2.security;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.security.dto.request.LoginRequest;
import com.example.mock2.security.dto.request.RefreshRequest;
import com.example.mock2.security.dto.request.RegisterRequest;
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

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request) {
        return Response.success(authService.login(request));
    }

    @PostMapping("/register")
    public Response register(@RequestBody RegisterRequest request) {
        return Response.success(authService.register(request));
    }

    @PostMapping("/refresh")
    public Response refresh(@RequestBody RefreshRequest request) {
        return Response.success(authService.refresh(request));
    }

}
