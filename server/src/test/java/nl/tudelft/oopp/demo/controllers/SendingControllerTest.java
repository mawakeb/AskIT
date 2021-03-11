package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SendingControllerTest {

    private SendingController sc;

    @Mock
    private QuestionRepository repo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        sc = new SendingController(repo);
    }

    @Test
    void sendQuestion() {
        sc.sendQuestion("SendingControllerTest question");
    }

    @Test
    void upvoteQuestion() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe);
        when(repo.findById(uuid)).thenReturn(question);
        sc.upvoteQuestion(uuid.toString());
        assertEquals(1, question.getUpvotes());
    }
}