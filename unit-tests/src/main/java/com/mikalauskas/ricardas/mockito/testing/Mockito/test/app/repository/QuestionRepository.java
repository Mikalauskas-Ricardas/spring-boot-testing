package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository;

import java.util.List;

public interface QuestionRepository {
    void saveQuestions(List<String> questions);
    List<String> findQuestionByExamId(Long id);
}
