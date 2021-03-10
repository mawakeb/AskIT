package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

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
}