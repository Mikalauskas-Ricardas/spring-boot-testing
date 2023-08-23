package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.fixtures;

import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model.Exam;

import java.util.List;
import java.util.Optional;

public class ExamFixtures {
    public static List<Exam> getExamsList() {
        return List.of(
                new Exam(1L, "Mathematics", List.of("2 + 2?")),
                new Exam(2L, "English", List.of("What's your name?")));
    }

    public static Exam getExam(String examName) {
        return new Exam(2L, examName, getQuestions());
    }

    public static List<String> getQuestions() {
        return List.of("What time is it?", "What date is it?");
    }
}
