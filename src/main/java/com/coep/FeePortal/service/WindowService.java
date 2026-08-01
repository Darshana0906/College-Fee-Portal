package com.coep.FeePortal.service;

import com.coep.FeePortal.entity.*;
import com.coep.FeePortal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WindowService {

    @Autowired private PaymentWindowRepository windowRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private FeeStructureRepository feeStructureRepository;
    @Autowired private FeeRecordRepository feeRecordRepository;

    @Transactional
    public PaymentWindow openPaymentWindow(Integer yearOfStudy, String academicYear, LocalDate startDate, LocalDate endDate) {
        PaymentWindow window = new PaymentWindow(yearOfStudy, academicYear, startDate, endDate);
        window = windowRepository.save(window);

        // Parse the start year from academicYear (e.g., "2026-27" → 2026)
        int academicStartYear = Integer.parseInt(academicYear.split("-")[0]);

        List<Student> students = studentRepository.findAll();

        for (Student student : students) {
            // Compute this student's actual year of study based on their admission year
            int computedYearOfStudy = academicStartYear - student.getAdmissionYear() + 1;

            // Only create a FeeRecord if this student is actually in the target year of study
            if (computedYearOfStudy != yearOfStudy || computedYearOfStudy < 1) {
                continue;
            }

            Optional<FeeRecord> existing = feeRecordRepository.findByStudentIdAndYearOfStudy(student.getId(), yearOfStudy);
            if (existing.isEmpty()) {
                Optional<FeeStructure> feeStructOpt = feeStructureRepository.findByCategoryIdAndAcademicYearAndYearOfStudy(
                        student.getCategory().getId(), academicYear, yearOfStudy);

                if (feeStructOpt.isPresent()) {
                    FeeStructure feeStructure = feeStructOpt.get();
                    FeeRecord record = new FeeRecord(
                            student,
                            academicYear,
                            yearOfStudy,
                            yearOfStudy, // sequence number = year
                            feeStructure.getTotal(), // snapshot the computed total
                            window,
                            feeStructure  // store reference for breakdown display
                    );
                    feeRecordRepository.save(record);
                }
            }
        }
        return window;
    }
}
