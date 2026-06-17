package com.ai.resumeanalyzer.controller;

import com.ai.resumeanalyzer.service.PdfService;
import com.ai.resumeanalyzer.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ai.resumeanalyzer.repository.AnalysisHistoryRepository;
import com.ai.resumeanalyzer.entity.AnalysisHistory;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayInputStream;
import com.ai.resumeanalyzer.entity.User;
import com.ai.resumeanalyzer.repository.UserRepository;



@Controller
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private AnalysisHistoryRepository historyRepository;

    @Autowired
    private PdfService pdfService;
    @Autowired
    private UserRepository userRepository;

    // =========================
    // HOME PAGES
    // =========================
    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("history", historyRepository.findAll());
        return "dashboard";
    }

    @GetMapping("/downloadReport")
    public ResponseEntity<byte[]> downloadReport() {

        AnalysisHistory history =
                historyRepository.findTopByOrderByIdDesc();

        ByteArrayInputStream bis =
                pdfService.generatePdf(history);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ResumeReport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/deleteHistory/{id}")
    public String deleteHistory(@PathVariable Long id){
        historyRepository.deleteById(id);

        return "redirect:/dashboard";
    }

    // =========================
    // MAIN UPLOAD API
    // =========================
    @PostMapping("/register")
    public String registerUser(User user,Model model){
        userRepository.save(user);
        model.addAttribute("successMessage","Registered Successfully! Please login.");
        return "login";
    }


    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file,
                               Model model) {

        try {

            System.out.println("🔥 CONTROLLER CALLED");

            String text = resumeService.extractTextFromPdf(file);
            System.out.println("🔥 TEXT LENGTH = " + (text == null ? 0 : text.length()));

            List<String> detectedSkills = resumeService.extractSkills(text);
            System.out.println("🔥 SKILLS = " + detectedSkills);

            int atsScore = resumeService.calculateATSScore(text, detectedSkills);

            // Save History
            AnalysisHistory history = new AnalysisHistory();
            history.setFileName(file.getOriginalFilename());
            history.setAtsScore(atsScore);
            history.setUploadedAt(LocalDateTime.now());



            historyRepository.save(history);

            // Send data to result page
            model.addAttribute("fileName", file.getOriginalFilename());
            model.addAttribute("atsScore", atsScore);
            model.addAttribute("level", resumeService.getLevel(atsScore));
            model.addAttribute("detectedSkills", detectedSkills);
            model.addAttribute("missingSkills", new ArrayList<>());
            model.addAttribute("suggestions",
                    resumeService.generateSuggestions(atsScore, new ArrayList<>()));

            return "result";

        } catch (Exception e) {

            System.out.println("❌ ERROR: " + e.getMessage());
            model.addAttribute("error", e.getMessage());

            return "result";
        }
    }


}