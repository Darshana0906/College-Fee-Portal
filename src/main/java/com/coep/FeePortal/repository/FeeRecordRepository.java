package com.coep.FeePortal.repository;

import com.coep.FeePortal.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    List<FeeRecord> findByStudentIdOrderBySequenceNumberAsc(Long studentId);
    Optional<FeeRecord> findByStudentIdAndYearOfStudy(Long studentId, Integer yearOfStudy);
}
