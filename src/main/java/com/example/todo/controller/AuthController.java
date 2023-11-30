package com.example.todo.controller;

import com.example.todo.cofig.CustomUserDetails;
import com.example.todo.dto.AuthLoginDto;
import com.example.todo.dto.AuthRequestDto;
import com.example.todo.entity.User;
import com.example.todo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody User userCredential){
        return authService.saveUser(userCredential);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequestDto authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return authService.generateToken(authRequest.getName(),authRequest.getId());
        } else {
            throw new RuntimeException("invalid access");
        }

    }

    @GetMapping("/validate")
    public String validate(@RequestParam String token){
        authService.validateToken(token);
        return "Token is valid";
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody AuthLoginDto userCredential){
        return authService.loginUser(userCredential);
    }

}
