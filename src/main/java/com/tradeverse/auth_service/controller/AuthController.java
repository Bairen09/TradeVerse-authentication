package com.tradeverse.auth_service.controller;

import com.tradeverse.auth_service.dto.LoginRequest;
import com.tradeverse.auth_service.dto.LoginResponse;
import com.tradeverse.auth_service.model.User;
import com.tradeverse.auth_service.security.JwtService;
import com.tradeverse.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws Exception{
        try{
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
        String token= jwtService.generateToken(request.getUsername());
        return new LoginResponse(token, "Bearer");
    }
    @GetMapping("/test")
    public String test(){
        return "Protected endpoint config";
    }
}
