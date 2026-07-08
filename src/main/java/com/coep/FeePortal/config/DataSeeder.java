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
import java.time.LocalDate;

/**
 * Seeds the database with fee structure data.
 *
 * Source: College fee chart for Programs — S.Y (AY:2025-26), T.Y (AY:2026-27) and Final Year (AY:2027-28)
 *
 * Fee Components:
 *   1. Tuition Fee
 *   2. Development Fee
 *   3. Other Fees
 *   4. Examination Fee  — Rs. 2,750 (same for all categories)
 *   5. Miscellaneous Fee — Rs. 760 (Alumni Association Rs. 750 + Disaster Relief Fund Rs. 10)
 *
 * Note: '*' in the chart means the fee is covered by the Government/Scholarship — stored as Rs. 0 here.
 * Note: NRI/FN/OCI/PIO category has USD-based fees; excluded from this portal (INR-only system).
 * Note: FY fee structure is NOT shown in the provided chart — fill those below when available.
 */
@Configuration
public class DataSeeder {

    // Common fees — same across all categories
    private static final BigDecimal EXAM_FEE  = new BigDecimal("2750.00");
    private static final BigDecimal MISC_FEE  = new BigDecimal("760.00");  // 750 + 10
    private static final BigDecimal ZERO      = BigDecimal.ZERO;

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepo,
                                   FeeStructureRepository feeStructureRepo,
                                   WindowService windowService,
                                   com.coep.FeePortal.repository.StudentRepository studentRepository) {
        return args -> {
            if (categoryRepo.count() > 0) return; // Skip if already seeded

            // =====================================================================
            // STEP 1: Create all admission categories
            // =====================================================================
            Category open    = categoryRepo.save(new Category("General (Open)"));
            Category scst    = categoryRepo.save(new Category("SC/ST"));
            Category vjnt    = categoryRepo.save(new Category("VJ/NT/SBC/OBC (Valid NC)"));
            Category ebc     = categoryRepo.save(new Category("EBC/EWS/SEBC (Valid Certificate)"));
            Category tfws    = categoryRepo.save(new Category("TFWS"));
            Category jkne    = categoryRepo.save(new Category("JK Migrant / NE"));
            Category pmsss   = categoryRepo.save(new Category("PMSSS"));
            Category ciwgc   = categoryRepo.save(new Category("CIWGC"));
            Category girls   = categoryRepo.save(new Category("Girls OPEN/OBC/SBC/EWS/SEBC (Income < 8L)"));
            Category jeeAI   = categoryRepo.save(new Category("JEE All India Quota"));
            Category spot    = categoryRepo.save(new Category("Spot Round (All Category)"));

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
            // STEP 2: Seed fee structures for SY, TY, Final Year
            // Constructor: (category, academicYear, yearOfStudy,
            //               tuitionFee, developmentFee, otherFee, examFee, miscFee)
            // =====================================================================
            // ── FIRST YEAR (yearOfStudy = 1, AY: 2024-2025) ──────────────────
            seedYear(feeStructureRepo, open,  "2024-2025", 1, 17000, 70700, 45030);
            seedYear(feeStructureRepo, scst,  "2024-2025", 1,     0,     0,     0);  // Govt-covered
            seedYear(feeStructureRepo, vjnt,  "2024-2025", 1,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, ebc,   "2024-2025", 1,  8500, 70700, 45030);
            seedYear(feeStructureRepo, tfws,  "2024-2025", 1,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, jkne,  "2024-2025", 1, 17000, 70700, 45030);
            seedYear(feeStructureRepo, pmsss, "2024-2025", 1,     0,     0,     0);  // Govt-covered
            seedYear(feeStructureRepo, ciwgc, "2024-2025", 1, 17000, 91700, 45030);
            seedYear(feeStructureRepo, girls, "2024-2025", 1,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, jeeAI, "2024-2025", 1, 17000, 91700, 45030);
            seedYear(feeStructureRepo, spot,  "2024-2025", 1, 17000, 70700, 45030);

            // ── SECOND YEAR (yearOfStudy = 2, AY: 2025-2026) ──────────────────
            seedYear(feeStructureRepo, open,  "2025-2026", 2, 17000, 70700, 45030);
            seedYear(feeStructureRepo, scst,  "2025-2026", 2,     0,     0,     0);  // Govt-covered
            seedYear(feeStructureRepo, vjnt,  "2025-2026", 2,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, ebc,   "2025-2026", 2,  8500, 70700, 45030);
            seedYear(feeStructureRepo, tfws,  "2025-2026", 2,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, jkne,  "2025-2026", 2, 17000, 70700, 45030);
            seedYear(feeStructureRepo, pmsss, "2025-2026", 2,     0,     0,     0);  // Govt-covered
            seedYear(feeStructureRepo, ciwgc, "2025-2026", 2, 17000, 91700, 45030);
            seedYear(feeStructureRepo, girls, "2025-2026", 2,     0, 70700, 45030);  // Tuition govt-covered
            seedYear(feeStructureRepo, jeeAI, "2025-2026", 2, 17000, 91700, 45030);
            seedYear(feeStructureRepo, spot,  "2025-2026", 2, 17000, 70700, 45030);

            // ── THIRD YEAR (yearOfStudy = 3, AY: 2026-2027) ───────────────────
            seedYear(feeStructureRepo, open,  "2026-2027", 3, 17000, 70700, 45030);
            seedYear(feeStructureRepo, scst,  "2026-2027", 3,     0,     0,     0);
            seedYear(feeStructureRepo, vjnt,  "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, ebc,   "2026-2027", 3,  8500, 70700, 45030);
            seedYear(feeStructureRepo, tfws,  "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, jkne,  "2026-2027", 3, 17000, 70700, 45030);
            seedYear(feeStructureRepo, pmsss, "2026-2027", 3,     0,     0,     0);
            seedYear(feeStructureRepo, ciwgc, "2026-2027", 3, 17000, 91700, 45030);
            seedYear(feeStructureRepo, girls, "2026-2027", 3,     0, 70700, 45030);
            seedYear(feeStructureRepo, jeeAI, "2026-2027", 3, 17000, 91700, 45030);
            seedYear(feeStructureRepo, spot,  "2026-2027", 3, 17000, 70700, 45030);

            // ── FINAL YEAR (yearOfStudy = 4, AY: 2027-2028) ───────────────────
            seedYear(feeStructureRepo, open,  "2027-2028", 4, 17000, 70700, 45030);
            seedYear(feeStructureRepo, scst,  "2027-2028", 4,     0,     0,     0);
            seedYear(feeStructureRepo, vjnt,  "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, ebc,   "2027-2028", 4,  8500, 70700, 45030);
            seedYear(feeStructureRepo, tfws,  "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, jkne,  "2027-2028", 4, 17000, 70700, 45030);
            seedYear(feeStructureRepo, pmsss, "2027-2028", 4,     0,     0,     0);
            seedYear(feeStructureRepo, ciwgc, "2027-2028", 4, 17000, 91700, 45030);
            seedYear(feeStructureRepo, girls, "2027-2028", 4,     0, 70700, 45030);
            seedYear(feeStructureRepo, jeeAI, "2027-2028", 4, 17000, 91700, 45030);
            seedYear(feeStructureRepo, spot,  "2027-2028", 4, 17000, 70700, 45030);

            // ── FIRST YEAR (yearOfStudy = 1) ───────────────────────────────────
            // TODO: Add FY fee structure when available.
            // Use the same seedYear() helper below.
            // Example: seedYear(feeStructureRepo, open, "2024-2025", 1, 17000, 70700, 45030);

            // =====================================================================
            // STEP 3: Open the initial payment window for FY (adjust dates as needed)
            // =====================================================================
            // Uncomment and configure once FY fee structures are seeded above:
            // windowService.openPaymentWindow(1, "2024-2025",
            //         LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
        };
    }

    /**
     * Helper to save a FeeStructure row.
     * examFee and miscFee are constant across all categories (Rs. 2750 and Rs. 760).
     */
    private void seedYear(FeeStructureRepository repo, Category cat,
                          String academicYear, int yearOfStudy,
                          long tuition, long development, long other) {
        repo.save(new FeeStructure(
                cat,
                academicYear,
                yearOfStudy,
                BigDecimal.valueOf(tuition),
                BigDecimal.valueOf(development),
                BigDecimal.valueOf(other),
                EXAM_FEE,
                MISC_FEE
        ));
    }
}
