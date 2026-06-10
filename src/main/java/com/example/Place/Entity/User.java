package com.example.Place.Entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

public class User {

    @JsonTypeId
    private String id;

    private String email;
    private String password;
    private String name;
    private String branch = "MCA";

    private List<String> interviewHistory = new ArrayList<>();
    private double avgScore = 7.8;
    private int totalInterviews = 12;

    private Map<String,Integer> topicScores = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<String> getInterviewHistory() {
        return interviewHistory;
    }

    public void setInterviewHistory(List<String> interviewHistory) {
        this.interviewHistory = interviewHistory;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public int getTotalInterviews() {
        return totalInterviews;
    }

    public void setTotalInterviews(int totalInterviews) {
        this.totalInterviews = totalInterviews;
    }

    public Map<String, Integer> getTopicScores() {
        return topicScores;
    }

    public void setTopicScores(Map<String, Integer> topicScores) {
        this.topicScores = topicScores;
    }
}