package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository;

import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam save(Exam exam);
    List<Exam> findAll();
    Exam findByName(String name);
}
