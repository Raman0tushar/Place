package com.example.Place.Controller;

import com.example.Place.Dto.InterviewRequest;
import com.example.Place.Util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://placementor-front-end.vercel.app"
})
public class InterviewController {

    private final JwtUtil jwtUtil;

    public InterviewController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/mock")
    public ResponseEntity<?> mockInterview(@RequestBody InterviewRequest req) {

        if (!jwtUtil.validateToken(req.token())) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        return ResponseEntity.ok(
                Map.of(
                        "question", req.question(),
                        "answer", req.answer()
                )
        );
    }
}
