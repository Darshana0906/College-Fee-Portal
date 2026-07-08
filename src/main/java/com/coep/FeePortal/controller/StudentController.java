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
import com.coep.FeePortal.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FeeService feeService;

    @Autowired
    private PaymentWindowRepository windowRepository;

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private FeeRecordRepository feeRecordRepository;


    @GetMapping("/{id}/fee-records")
    public ResponseEntity<List<FeeRecord>> getStudentFeeRecords(@PathVariable Long id) {
        return ResponseEntity.ok(feeService.getStudentFeeRecords(id));
    }
}
