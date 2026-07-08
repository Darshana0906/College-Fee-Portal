package com.coep.FeePortal.service;

import com.coep.FeePortal.entity.FeeRecord;
import com.coep.FeePortal.repository.FeeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    public List<FeeRecord> getStudentFeeRecords(Long studentId) {
        return feeRecordRepository.findByStudentIdOrderBySequenceNumberAsc(studentId);
    }
}
