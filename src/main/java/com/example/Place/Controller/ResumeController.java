package com.example.Place.Controller;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("*")
public class ResumeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestParam("resume") MultipartFile file
    ) throws Exception {

        String resumeText = extractText(file).toLowerCase();
        Set<String> resumeKeywords = extractKeywords(resumeText);

        String apiUrl =
                "https://datasets-server.huggingface.co/rows" +
                        "?dataset=ANAS92074%2Fats_resume_scoring_dataset" +
                        "&config=default&split=train&offset=0&length=100";

        JSONArray rows = new JSONArray();

        try {
            String apiResponse = restTemplate.getForObject(apiUrl, String.class);
            JSONObject jsonObject = new JSONObject(apiResponse);
            rows = jsonObject.getJSONArray("rows");
        } catch (Exception e) {
            System.out.println("AI API FAILED");
        }

        double bestSimilarity = 0;
        int datasetAtsScore = 0;

        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            JSONObject data = row.getJSONObject("row");

            String datasetResume = data.toString().toLowerCase();

            double similarity = calculateSimilarity(resumeText, datasetResume);

            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                datasetAtsScore = extractScore(data);
            }
        }

        List<String> detectedSkills = detectSkills(resumeText);

        int keywordScore = Math.min(resumeKeywords.size() / 2, 35);
        int skillsScore = Math.min(detectedSkills.size() * 2, 20);

        int expScore = 0;
        if (resumeText.contains("developed")) expScore += 3;
        if (resumeText.contains("implemented")) expScore += 3;
        if (resumeText.contains("optimized")) expScore += 3;
        if (resumeText.matches(".*\\d+%.*")) expScore += 6;
        expScore = Math.min(expScore, 15);

        int educationScore =
                resumeText.contains("mca") ? 10 :
                        resumeText.contains("b.tech") ? 9 :
                                resumeText.contains("bca") ? 8 : 5;

        int formatScore = resumeText.length() > 1000 ? 10 : 7;

        int projectScore =
                (resumeText.contains("github") ? 2 : 0) +
                        (resumeText.contains("project") ? 2 : 0) +
                        (resumeText.contains("certification") ? 1 : 0);

        int contactScore = 0;

        if (resumeText.matches(".*[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}.*")) {
            contactScore += 2;
        }

        if (resumeText.matches(".*(\\+91)?[6-9]\\d{9}.*")) {
            contactScore += 2;
        }

        if (resumeText.contains("linkedin")) {
            contactScore += 1;
        }

        int aiSimilarityScore = (int) (bestSimilarity * 20);

        int totalScore =
                keywordScore + skillsScore + expScore +
                        educationScore + formatScore + projectScore +
                        contactScore + aiSimilarityScore;

        if (datasetAtsScore > 0) {
            totalScore = (totalScore + datasetAtsScore) / 2;
        }

        totalScore = Math.min(totalScore, 100);

        AtsResponse response = AtsResponse.builder()
                .atsScore(totalScore)
                .aiSimilarityScore(aiSimilarityScore)
                .datasetScore(datasetAtsScore)
                .keywordScore(keywordScore)
                .skillsScore(skillsScore)
                .experienceScore(expScore)
                .educationScore(educationScore)
                .formatScore(formatScore)
                .projectScore(projectScore)
                .contactScore(contactScore)
                .detectedSkills(detectedSkills)
                .strengths(List.of(
                        "Good technical skills",
                        "Relevant projects",
                        "AI semantic analysis completed"
                ))
                .improvements(List.of(
                        "Add measurable achievements",
                        "Add more technical skills",
                        "Improve project descriptions"
                ))
                .build();

        return ResponseEntity.ok(response);
    }

    // ================= TEXT EXTRACTION =================

    private String extractText(MultipartFile file) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        file.getInputStream().transferTo(baos);

        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBuffer(baos.toByteArray())
        )) {
            return new PDFTextStripper()
                    .getText(document)
                    .replaceAll("\\s+", " ")
                    .trim();
        }
    }

    // ================= KEYWORDS =================

    private Set<String> extractKeywords(String text) {

        Set<String> stopWords = Set.of(
                "the", "and", "with", "for", "from",
                "that", "this", "have", "will"
        );

        return Arrays.stream(text.split("\\W+"))
                .filter(w -> w.length() > 2)
                .filter(w -> !stopWords.contains(w))
                .map(this::normalize)
                .collect(Collectors.toSet());
    }

    // ================= SKILLS =================

    private List<String> detectSkills(String text) {

        List<String> skills = List.of(
                "java", "spring", "springboot", "react",
                "javascript", "python", "mysql", "mongodb",
                "docker", "aws", "html", "css", "node",
                "git", "github"
        );

        return skills.stream()
                .filter(text::contains)
                .collect(Collectors.toList());
    }

    private String normalize(String word) {
        return Map.of(
                "js", "javascript",
                "reactjs", "react",
                "nodejs", "node"
        ).getOrDefault(word, word);
    }

    // ================= SIMILARITY =================

    private double calculateSimilarity(String t1, String t2) {

        Set<String> set1 = extractKeywords(t1);
        Set<String> set2 = extractKeywords(t2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0 :
                (double) intersection.size() / union.size();
    }

    private int extractScore(JSONObject data) {

        String[] keys = {
                "ats_score", "score", "ATSScore", "resume_score"
        };

        for (String key : keys) {
            if (data.has(key)) {
                try {
                    return data.getInt(key);
                } catch (Exception ignored) {}
            }
        }
        return 0;
    }

    // ================= RESPONSE DTO =================

    @Data
    @Builder
    public static class AtsResponse {
        int atsScore;
        int aiSimilarityScore;
        int datasetScore;
        int keywordScore;
        int skillsScore;
        int experienceScore;
        int educationScore;
        int formatScore;
        int projectScore;
        int contactScore;

        List<String> detectedSkills;
        List<String> strengths;
        List<String> improvements;
    }
}