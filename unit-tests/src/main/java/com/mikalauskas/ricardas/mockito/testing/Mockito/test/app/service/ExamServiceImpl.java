package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.service;

import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model.Exam;
import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository.ExamRepository;
import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    @Override
    public Exam findExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(exam -> exam.getName().contains(name))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        var exam = examRepository.findByName(name);
        if(exam != null) {
            List<String> questions = questionRepository.findQuestionByExamId(exam.getId());
            exam.setQuestions(questions);
            return exam;
        }

        return null;
    }

    @Override
    public Exam saveExam(Exam exam) {
        if(!exam.getQuestions().isEmpty()) {
            questionRepository.saveQuestions(exam.getQuestions());
        }
        return examRepository.save(exam);
    }


}
