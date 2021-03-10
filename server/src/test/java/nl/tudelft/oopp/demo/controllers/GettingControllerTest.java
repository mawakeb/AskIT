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

    private List<String> questionStrings;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's

        // mock repo.getAllStrings()
        questionStrings = List.of("Question 1","Question 2","Question 3");
        when(repo.getAllStrings()).thenReturn(questionStrings);

        gc = new GettingController(repo);
    }

    @Test
    void testGetQuestions() {
        List<String> actual = gc.getQuestions();
        assertEquals(questionStrings,actual);
    }
}