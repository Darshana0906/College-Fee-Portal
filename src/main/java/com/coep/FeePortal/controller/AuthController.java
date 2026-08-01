package com.coep.FeePortal.controller;

import com.coep.FeePortal.entity.Category;
import com.coep.FeePortal.entity.FeeRecord;
import com.coep.FeePortal.entity.FeeStructure;
import com.coep.FeePortal.entity.PaymentWindow;
import com.coep.FeePortal.entity.Student;
import com.coep.FeePortal.repository.CategoryRepository;
import com.coep.FeePortal.repository.FeeRecordRepository;
import com.coep.FeePortal.repository.FeeStructureRepository;
import com.coep.FeePortal.repository.PaymentWindowRepository;
import com.coep.FeePortal.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PaymentWindowRepository windowRepository;
    @Autowired private FeeStructureRepository feeStructureRepository;
    @Autowired private FeeRecordRepository feeRecordRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", sc);

            Student student = studentRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", student.getId());
            response.put("name", student.getName());
            response.put("email", student.getEmail());
            response.put("role", student.getRole());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Student student = new Student(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), // Hash the password!
                request.getCourse(),
                request.getAdmissionYear(),
                category,
                "STUDENT"
        );
        student = studentRepository.save(student);

        // Auto-generate fee record if an active window exists for this student's computed year of study
        Optional<PaymentWindow> activeWindow = windowRepository.findByYearOfStudyAndIsActiveTrue(1);

        // Check all possible year-of-study windows (1 through 4)
        for (int yr = 1; yr <= 4; yr++) {
            Optional<PaymentWindow> windowOpt = windowRepository.findByYearOfStudyAndIsActiveTrue(yr);
            if (windowOpt.isPresent()) {
                PaymentWindow window = windowOpt.get();
                int academicStartYear = Integer.parseInt(window.getAcademicYear().split("-")[0]);
                int computedYear = academicStartYear - request.getAdmissionYear() + 1;

                if (computedYear == yr && computedYear > 0) {
                    Optional<FeeStructure> feeStructOpt = feeStructureRepository.findByCategoryIdAndAcademicYearAndYearOfStudy(
                            category.getId(), window.getAcademicYear(), computedYear);

                    if (feeStructOpt.isPresent()) {
                        FeeRecord record = new FeeRecord(
                                student,
                                window.getAcademicYear(),
                                computedYear, computedYear,
                                feeStructOpt.get().getTotal(),
                                window,
                                feeStructOpt.get()
                        );
                        feeRecordRepository.save(record);
                    }
                    break; // A student can only be in one year at a time
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", student.getId());
        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    // DTOs
    static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String course;
        private Integer admissionYear;
        private Long categoryId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getCourse() { return course; }
        public void setCourse(String course) { this.course = course; }
        public Integer getAdmissionYear() { return admissionYear; }
        public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    }
}
