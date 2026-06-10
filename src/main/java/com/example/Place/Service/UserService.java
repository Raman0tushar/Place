package com.example.Place.Service;

import com.example.Place.Entity.User;
import com.example.Place.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    // Register User
    public User saveUser(User user) {

        user.setPassword(
                encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Find User by Email
    public User findByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    // Validate Password
    public boolean validatePassword(
            String rawPassword,
            String encodedPassword) {

        return encoder.matches(
                rawPassword,
                encodedPassword);
    }

    // Find User by ID
    public User findById(String userId) {

        return userRepository
                .findById(userId)
                .orElse(null);
    }

    // Total Interviews
    public int getInterviewCount(String userId) {

        User user = findById(userId);

        return user != null
                ? user.getTotalInterviews()
                : 0;
    }

    // Average Score
    public double getAverageScore(String userId) {

        User user = findById(userId);

        return user != null
                ? user.getAvgScore()
                : 0.0;
    }

    // Strong Topics (score >= 7)
    public List<String> getStrongTopics(String userId) {

        User user = findById(userId);

        if (user == null) {
            return List.of();
        }

        return user.getTopicScores()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 7)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Weak Topics (score < 5)
    public List<String> getWeakTopics(String userId) {

        User user = findById(userId);

        if (user == null) {
            return List.of();
        }

        return user.getTopicScores()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() < 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Get Interview History
    public List<String> getInterviewHistory(String userId) {

        User user = findById(userId);

        return user != null
                ? user.getInterviewHistory()
                : List.of();
    }

    // Update User
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete User
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}