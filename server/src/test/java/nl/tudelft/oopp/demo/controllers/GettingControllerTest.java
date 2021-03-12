package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GettingControllerTest {

    private GettingController gc;

    @Mock
    private QuestionRepository repo;

    private List<Question> questionList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's

        // mock repo.getAllStrings()
        UUID dupe = UUID.randomUUID();
        questionList = List.of(
                new Question("Q1", dupe, dupe),
                new Question("Q2", dupe, dupe),
                new Question("Q3", dupe, dupe));
        when(repo.findAll()).thenReturn(questionList);

        gc = new GettingController(repo);
    }

    @Test
    void testGetQuestions() {
        List<Question> actual = gc.getQuestions();
        assertEquals(questionList, actual);
    }
}