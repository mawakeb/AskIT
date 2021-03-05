package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class ServerCommunicationTest {

    @Test
    public void testRandomQuote() {
        assertNotNull(ServerCommunication.getQuote());
    }

    @Test
    public void testGetQuestions() {
        assertNotNull(ServerCommunication.getQuestions());
    }

    @Test
    public void testSendQuestion() {
        ServerCommunication.sendQuestion("Unit test question");
    }
}
