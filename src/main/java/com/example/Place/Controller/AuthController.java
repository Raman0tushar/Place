package com.example.Place.Controller;



import com.example.Place.Dto.LoginRequest;
import com.example.Place.Dto.RegisterRequest;
import com.example.Place.Entity.User;
import com.example.Place.Service.UserService;
import com.example.Place.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPassword(req.password());  // Will be encoded in service
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userService.findByEmail(req.email());

        if (user != null && userService.validatePassword(req.password(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());

            // MUST RETURN TOKEN LIKE THIS
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("name", user.getName());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }
}
