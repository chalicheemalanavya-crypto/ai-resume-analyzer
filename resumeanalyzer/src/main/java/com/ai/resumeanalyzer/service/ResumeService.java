package com.ai.resumeanalyzer.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {

    // PDF Text Extraction
    public String extractTextFromPdf(MultipartFile file) {

        try (InputStream is = file.getInputStream();
             PDDocument document = PDDocument.load(is)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (Exception e) {

            throw new RuntimeException("PDF extraction failed");
        }
    }

    // Skill Detection
    public List<String> extractSkills(String text) {

        List<String> skills = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            return skills;
        }

        String t = text.toLowerCase();

        if (t.contains("java")) skills.add("Java");
        if (t.contains("python")) skills.add("Python");
        if (t.contains("javascript")) skills.add("JavaScript");
        if (t.contains("spring")) skills.add("Spring Boot");
        if (t.contains("react")) skills.add("React.js");
        if (t.contains("html")) skills.add("HTML");
        if (t.contains("css")) skills.add("CSS");
        if (t.contains("mysql")) skills.add("MySQL");
        if (t.contains("mongodb")) skills.add("MongoDB");
        if (t.contains("aws")) skills.add("AWS");
        if (t.contains("docker")) skills.add("Docker");
        if (t.contains("git")) skills.add("Git");

        return skills;
    }

    // ATS Score Calculation
    public int calculateATSScore(String text, List<String> skills) {

        if (text == null || text.isEmpty()) {
            return 0;
        }

        int baseScore = 40;
        int skillScore = skills.size() * 5;
        int lengthScore = Math.min(text.length() / 200, 30);

        int finalScore = baseScore + skillScore + lengthScore;

        return Math.min(finalScore, 100);
    }

    // Resume Level
    public String getLevel(int score) {

        if (score >= 80) {
            return "Excellent";
        } else if (score >= 60) {
            return "Good";
        } else if (score >= 40) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    // Suggestions
    public List<String> generateSuggestions(int score,
                                            List<String> missingSkills) {

        List<String> suggestions = new ArrayList<>();

        if (score < 50) {
            suggestions.add("Improve resume structure.");
        }

        if (missingSkills != null && !missingSkills.isEmpty()) {
            suggestions.add("Learn: " + String.join(", ", missingSkills));
        }

        if (score >= 80) {
            suggestions.add("Excellent Resume!");
        }

        return suggestions;
    }
    public int calculateJobMatch(String text, String jd) {

        if (text == null || jd == null || jd.isEmpty()) {
            return 0;
        }

        String resumeText = text.toLowerCase();
        String[] words = jd.toLowerCase().split(" ");

        int count = 0;

        for (String word : words) {

            if (word.length() > 2 && resumeText.contains(word)) {
                count++;
            }
        }

        return Math.min(count * 2, 100);
    }
}