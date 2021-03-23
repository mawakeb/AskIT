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
    private UUID dupe;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's

        // mock repo.getAllStrings()
        dupe = UUID.randomUUID();
        questionList = List.of(
                new Question(UUID.randomUUID(),"Q1", dupe, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q2", dupe, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q3", dupe, UUID.randomUUID(), 5));
        when(repo.getAllRoomQuestions(dupe)).thenReturn(questionList);

        gc = new GettingController(repo);
    }

    @Test
    void testGetQuestions() {
        List<Question> actual = gc.getQuestions(dupe.toString());
        assertEquals(questionList, actual);
    }

    @Test
    void testGetAnsweredQuestions() {
        List<Question> actual = gc.getAnswered(dupe.toString());
        assertEquals(List.of(), actual);
    }
}