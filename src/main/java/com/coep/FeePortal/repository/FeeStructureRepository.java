package com.coep.FeePortal.repository;

import com.coep.FeePortal.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    Optional<FeeStructure> findByCategoryIdAndAcademicYearAndYearOfStudy(Long categoryId, String academicYear, Integer yearOfStudy);
}
