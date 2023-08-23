package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.service;

import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model.Exam;

public interface ExamService {
    Exam findExamByName(String name);
    Exam findExamByNameWithQuestions(String name);
    Exam saveExam(Exam exam);
}
