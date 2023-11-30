package com.example.todo.service;

import com.example.todo.Exception.ApiRequestException;
import com.example.todo.dto.AuthLoginDto;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String saveUser(User credential) {
        if (repository.findByUsername(credential.getUsername()).isPresent()) {
            throw new ApiRequestException("Username already exists");
        }

        if (repository.findByEmail(credential.getEmail()).isPresent()) {
            throw new ApiRequestException("Email already exists");
        }

        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "User added to the system";
    }


    public Map<String, String> loginUser(AuthLoginDto userCredential){
        Map<String, String> json = new HashMap<>();
        String email = userCredential.getEmail();
        String password = userCredential.getPassword();
        User userCredentialUserId = repository.findByEmail(email).get();
        json.put("userId", String.valueOf(userCredentialUserId.getUserId()));
        System.out.println("User ID after put: " + userCredentialUserId.getUserId()); // Update this line
        json.put("accessToken", "Bearer " + getAccessToken(email, password));
        return json;
    }




    public String generateToken(String email,Long userId) {
        return jwtService.generateToken(email,userId);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }

    public String getAccessToken(String email, String password) {
        Optional<User> userCredentialOptional = repository.findByEmail(email);

        if (userCredentialOptional.isPresent()) {
            User user = userCredentialOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtService.generateToken(user.getEmail(),user.getUserId());
            } else {
                throw new ApiRequestException("Invalid password");
            }
        } else {
            throw new ApiRequestException("User with email " + email + " not found");
        }
    }
}
