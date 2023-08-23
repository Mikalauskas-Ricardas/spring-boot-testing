package com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.service;

import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.model.Exam;
import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository.ExamRepository;
import com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.mikalauskas.ricardas.mockito.testing.Mockito.test.app.fixtures.ExamFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// This annotation initializes the Mockito annotations (such as @Mock, @Spy, or @InjectMocks)
@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {
    @InjectMocks
    ExamServiceImpl examService;

    @Mock
    ExamRepository examRepository;

    @Mock
    QuestionRepository questionRepository;

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void findExamByName() {
        when(examRepository.findAll()).thenReturn(getExamsList());
        var exam = examService.findExamByName("English");

        assertEquals(2L, exam.getId());
        verify(examRepository).findAll();
    }

    @Test
    void findExamByNameWithQuestions() {
        // given
        final String examName = "English";

        // when
        when(examRepository.findByName(anyString())).thenReturn(getExam(examName));
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(getQuestions());
        var exam = examService.findExamByNameWithQuestions(examName);

        // then
        verify(examRepository).findByName(anyString());
        verify(questionRepository).findQuestionByExamId(anyLong());
        assertEquals(examName, exam.getName());

    }

    @Test
    void saveExam() {
        // given
        var exam = getExam("English");

        // when
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        var result = examService.saveExam(exam);

        verify(questionRepository).saveQuestions(anyList());
        verify(examRepository).save(any(Exam.class));

    }

    @Test
    void manageExceptions() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(anyLong())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> examService.findExamByNameWithQuestions("English"));
        verify(examRepository).findByName(anyString());
    }

    @Test
    void testArgumentMatchers() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(eq(2L))).thenReturn(getQuestions());
        var exam = examService.findExamByNameWithQuestions("English");

        verify(questionRepository).findQuestionByExamId(argThat(arg -> arg != null && arg > 1L));
    }

    @Test
    void testArgumentMatchers2() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(eq(2L))).thenReturn(getQuestions());
        var exam = examService.findExamByNameWithQuestions("English");

        verify(questionRepository).findQuestionByExamId(argThat(new MyArgsMatchers()));
    }


    public static class MyArgsMatchers implements ArgumentMatcher<Long> {
        @Override
        public boolean matches(Long argument) {
            return argument != null && argument > 0L;
        }

        @Override
        public String toString() {
            return "Argument validation failed";
        }
    }

    @Test
    void argumentsCaptor() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(getQuestions());
        var exam = examService.findExamByNameWithQuestions("English");

        verify(questionRepository).findQuestionByExamId(captor.capture());

        assertEquals(2L, captor.getValue());
    }

    @Test
    void doThrow() {
        Mockito.doThrow(IllegalArgumentException.class).when(questionRepository).saveQuestions(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            examService.saveExam(getExam("English"));
        });
    }

    @Test
    void doAnswer() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(getQuestions());

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 2L ? getQuestions(): Collections.emptyList();
        }).when(questionRepository).findQuestionByExamId(anyLong());

        var exam = examService.findExamByNameWithQuestions("English");
        assertEquals(2L, exam.getId());
    }

    @Test
    // Its not possible to call abstract class method's because they don't have any implementation
    void doCallRealMethod() {
        when(examRepository.findByName(anyString())).thenReturn(getExam("English"));
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(getQuestions());
        // Mockito.doCallRealMethod().when(questionRepository).findQuestionByExamId(anyLong());

        var exam = examService.findExamByNameWithQuestions("English");

        assertEquals(2L, exam.getId());
    }


    @Test
    @Disabled
    void testSpy() {
        // Spy classes will call the real methods but its also possible to mock those methods.
        // Class with spy annotation cannot be abstract, it has to have implementation;
        ExamRepository spyExamRepository = spy(ExamRepository.class);
        QuestionRepository spyQuestionRepository = spy(QuestionRepository.class);
        ExamService spyExamenService = new ExamServiceImpl(spyExamRepository, spyQuestionRepository);

        // To mock any spy methods its necessary to use doReturn
        doReturn(getQuestions()).when(spyQuestionRepository).findQuestionByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("English");
        assertEquals(2L, exam.getId());

        verify(examRepository).findByName(anyString());
        verify(questionRepository).findQuestionByExamId(anyLong());
    }
}