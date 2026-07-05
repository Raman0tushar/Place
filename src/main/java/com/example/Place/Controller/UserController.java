package com.example.Place.Controller;

import com.example.Place.Entity.User;
import com.example.Place.Service.UserService;
import com.example.Place.Util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://divyanshutiwari02.github.io/Placementor-FrontEnd/")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {

        try {

            // Validate token
            if (authHeader == null ||
                    !authHeader.startsWith("Bearer ")) {

                return ResponseEntity
                        .status(401)
                        .body("Missing or Invalid Token");
            }

            // Extract token
            String token = authHeader.substring(7);

            // Extract email
            String email = jwtUtil.extractEmail(token);

            // Find user
            User user = userService.findByEmail(email);

            if (user == null) {

                return ResponseEntity
                        .status(404)
                        .body("User not found");
            }

            // Dynamic Response
            HashMap<String, Object> response =
                    new HashMap<>();

            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("branch", user.getBranch());

            // Dynamic Interview Data
            response.put("interviews",
                    user.getTotalInterviews());

            response.put("avgScore",
                    user.getAvgScore());

            response.put("interviewHistory",
                    user.getInterviewHistory());

            // Dynamic Strong Topics
            response.put("strongTopics",
                    userService.getStrongTopics(user.getId()));

            // Dynamic Weak Topics
            response.put("weakTopics",
                    userService.getWeakTopics(user.getId()));

            // Topic Scores
            response.put("topicScores",
                    user.getTopicScores());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid or Expired Token");
        }
    }
}
