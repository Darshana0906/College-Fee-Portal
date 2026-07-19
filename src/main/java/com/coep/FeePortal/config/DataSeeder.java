package com.coep.FeePortal.config;

import com.coep.FeePortal.entity.Category;
import com.coep.FeePortal.entity.FeeStructure;
import com.coep.FeePortal.repository.CategoryRepository;
import com.coep.FeePortal.repository.FeeStructureRepository;
import com.coep.FeePortal.service.WindowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    private static final BigDecimal EXAM_FEE  = new BigDecimal("2750.00");
    private static final BigDecimal MISC_FEE  = new BigDecimal("760.00");  // 750 + 10

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepo,
                                   FeeStructureRepository feeStructureRepo,
                                   WindowService windowService,
                                   com.coep.FeePortal.repository.StudentRepository studentRepository) {
        return args -> {
            
            // =====================================================================
            // STEP 1: Create all admission categories (Idempotent)
            // =====================================================================
            Category open    = getOrCreateCategory(categoryRepo, "General (Open)");
            Category scst    = getOrCreateCategory(categoryRepo, "SC/ST");
            Category vjnt    = getOrCreateCategory(categoryRepo, "VJ/NT/SBC/OBC (Valid NC)");
            Category ebc     = getOrCreateCategory(categoryRepo, "EBC/EWS/SEBC (Valid Certificate)");
            Category tfws    = getOrCreateCategory(categoryRepo, "TFWS");
            Category jkne    = getOrCreateCategory(categoryRepo, "JK Migrant / NE");
            Category pmsss   = getOrCreateCategory(categoryRepo, "PMSSS");
            Category ciwgc   = getOrCreateCategory(categoryRepo, "CIWGC");
            Category girls   = getOrCreateCategory(categoryRepo, "Girls OPEN/OBC/SBC/EWS/SEBC (Income < 8L)");
            Category jeeAI   = getOrCreateCategory(categoryRepo, "JEE All India Quota");
            Category spot    = getOrCreateCategory(categoryRepo, "Spot Round (All Category)");

            // =====================================================================
            // STEP 1.5: Seed a Default Admin User for testing
            // =====================================================================
            if (!studentRepository.existsByEmail("admin@college.edu")) {
                studentRepository.save(new com.coep.FeePortal.entity.Student(
                        "College Admin",
                        "admin@college.edu",
                        new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123"),
                        "Administration",
                        0,
                        open,
                        "ADMIN"
                ));
            }

            // =====================================================================
            // STEP 2: Seed fee structures for FY, SY, TY, Final Year
            // =====================================================================
            List<FeeStructure> allFees = feeStructureRepo.findAll();

            // ── FIRST YEAR (yearOfStudy = 1, AY: 2024-2025) ──────────────────
            seedYear(feeStructureRepo, allFees, open,  "2024-2025", 1, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, scst,  "2024-2025", 1,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, vjnt,  "2024-2025", 1,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, ebc,   "2024-2025", 1,  8500, 70700, 45030);
            seedYear(feeStructureRepo, allFees, tfws,  "2024-2025", 1,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jkne,  "2024-2025", 1, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, pmsss, "2024-2025", 1,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, ciwgc, "2024-2025", 1, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, girls, "2024-2025", 1,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jeeAI, "2024-2025", 1, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, spot,  "2024-2025", 1, 17000, 70700, 45030);

            // ── SECOND YEAR (yearOfStudy = 2, AY: 2025-2026) ──────────────────
            seedYear(feeStructureRepo, allFees, open,  "2025-2026", 2, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, scst,  "2025-2026", 2,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, vjnt,  "2025-2026", 2,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, ebc,   "2025-2026", 2,  8500, 70700, 45030);
            seedYear(feeStructureRepo, allFees, tfws,  "2025-2026", 2,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jkne,  "2025-2026", 2, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, pmsss, "2025-2026", 2,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, ciwgc, "2025-2026", 2, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, girls, "2025-2026", 2,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jeeAI, "2025-2026", 2, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, spot,  "2025-2026", 2, 17000, 70700, 45030);

            // ── THIRD YEAR (yearOfStudy = 3, AY: 2026-2027) ───────────────────
            seedYear(feeStructureRepo, allFees, open,  "2026-2027", 3, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, scst,  "2026-2027", 3,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, vjnt,  "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, ebc,   "2026-2027", 3,  8500, 70700, 45030);
            seedYear(feeStructureRepo, allFees, tfws,  "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jkne,  "2026-2027", 3, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, pmsss, "2026-2027", 3,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, ciwgc, "2026-2027", 3, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, girls, "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jeeAI, "2026-2027", 3, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, spot,  "2026-2027", 3, 17000, 70700, 45030);

            // ── FINAL YEAR (yearOfStudy = 4, AY: 2027-2028) ───────────────────
            seedYear(feeStructureRepo, allFees, open,  "2027-2028", 4, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, scst,  "2027-2028", 4,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, vjnt,  "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, ebc,   "2027-2028", 4,  8500, 70700, 45030);
            seedYear(feeStructureRepo, allFees, tfws,  "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jkne,  "2027-2028", 4, 17000, 70700, 45030);
            seedYear(feeStructureRepo, allFees, pmsss, "2027-2028", 4,     0,     0,     0);
            seedYear(feeStructureRepo, allFees, ciwgc, "2027-2028", 4, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, girls, "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, allFees, jeeAI, "2027-2028", 4, 17000, 91700, 45030);
            seedYear(feeStructureRepo, allFees, spot,  "2027-2028", 4, 17000, 70700, 45030);
        };
    }

    private Category getOrCreateCategory(CategoryRepository repo, String name) {
        return repo.findAll().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseGet(() -> repo.save(new Category(name)));
    }

    private void seedYear(FeeStructureRepository repo, List<FeeStructure> allFees, Category cat,
                          String academicYear, int yearOfStudy,
                          long tuition, long development, long other) {
        boolean exists = allFees.stream().anyMatch(fs -> 
                fs.getCategory().getId().equals(cat.getId()) &&
                fs.getAcademicYear().equals(academicYear) &&
                fs.getYearOfStudy().equals(yearOfStudy)
        );

        if (!exists) {
            FeeStructure fs = new FeeStructure(
                    cat,
                    academicYear,
                    yearOfStudy,
                    BigDecimal.valueOf(tuition),
                    BigDecimal.valueOf(development),
                    BigDecimal.valueOf(other),
                    EXAM_FEE,
                    MISC_FEE
            );
            repo.save(fs);
            allFees.add(fs); // Update our local cache to prevent duplicates if we accidentally have two identical lines
        }
    }
}
