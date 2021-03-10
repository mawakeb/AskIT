package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GettingControllerTest {

    private GettingController gc;

    @Mock
    private QuestionRepository repo;

    private List<Question> questionList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's

        // mock repo.getAllStrings()
        questionList = List.of(
                new Question(0,"Q1"),
                new Question(1,"Q2"),
                new Question(3,"Q3"));
        when(repo.findAll()).thenReturn(questionList);

        gc = new GettingController(repo);
    }

    @Test
    void testGetQuestions() {
        List<Question> actual = gc.getQuestions();
        assertEquals(questionList,actual);
    }
}