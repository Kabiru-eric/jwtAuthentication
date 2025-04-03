package com.accountscheduling.accounts_scheduler.jwtAuth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUserName())) {
            throw new RuntimeException("Username is already taken");
        }
        User user=new User();
        user.setUsername(registerDto.getUserName());
        user.setRole(registerDto.getRole());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));// Encrypt password
        return userRepository.save(user);
    }

    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }
    // Method to fetch the role of the user
    public Role getRole(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getRole() : null;  // Return the role of the user (e.g., "ADMIN", "USER")
    }
}
