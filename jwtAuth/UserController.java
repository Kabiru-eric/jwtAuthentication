package com.accountscheduling.accounts_scheduler.jwtAuth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            User savedUser = userService.registerUser(registerDto);
            RegisterResponseDto registerResponseDto =new RegisterResponseDto();
            //registeredDto.getUserName()
            registerResponseDto.setUserName(savedUser.getUsername());
            registerResponseDto.setMessage("User saved successfully");
            return ResponseEntity.ok(registerResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration error: " + e.getMessage());
        }
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody  LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        String role = String.valueOf(loginRequestDto.getRole());
        boolean isAuthenticated = userService.validateUser(username, password);
        if (isAuthenticated) {

            final String jwt = jwtUtil.generateToken(username,role);
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            *//*response.put("username", username);
            response.put("role", role);*//*
           /// response.put("password", password); // Consider security implications of this practice
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }*/
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // Validate user credentials
        boolean isAuthenticated = userService.validateUser(username, password);

        if (isAuthenticated) {
            // Fetch the role of the user from the database
            Role role = userService.getRole(username);

            if (role != null) {
                // Generate JWT token with username and role
                final String jwt = jwtUtil.generateToken(username,role);  // Generate token with role

                // Prepare the response map
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
              /*  response.put("username", username);
                response.put("role", role);  // Include the role in the response*/

                return ResponseEntity.ok(response);  // Return JWT token and user details
            } else {
                return ResponseEntity.status(404).body("User role not found");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}

