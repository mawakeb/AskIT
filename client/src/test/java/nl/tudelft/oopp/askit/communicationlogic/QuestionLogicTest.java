package nl.tudelft.oopp.askit.communicationlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.communication.ServerCommunication;
import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.methods.TimeControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

class QuestionLogicTest {

    private static final Gson gson = new Gson();

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    // stores request sent by SUT
    private HttpRequest request;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        // clear request that might be left from earlier tests
        request = null;

        // supply response mock for calls to client.send(request, bodyHandler)
        // also stores the corresponding request for access during tests
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    request = (HttpRequest) invocation.getArguments()[0];
                    return response;
                });

        ServerCommunication.setHttpClient(client);
    }

    @Test
    public void testGetQuestions() {

        UUID testId = UUID.randomUUID();

        List<Question> expected = List.of(
                new Question(UUID.randomUUID(), "Q1", 4, testId, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q2", 5, testId, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q3", 6, testId, UUID.randomUUID(), 5));
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        List<Question> actual = QuestionLogic.getQuestions(testId.toString());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnsweredQuestions() {

        UUID testId = UUID.randomUUID();

        List<Question> expected = List.of(
                new Question(UUID.randomUUID(), "Q1", 4, testId, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q2", 5, testId, UUID.randomUUID(), 5),
                new Question(UUID.randomUUID(), "Q3", 6, testId, UUID.randomUUID(), 5));
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        List<Question> actual = QuestionLogic.getQuestions(testId.toString());
        assertEquals(expected, actual);
    }

    @Test
    public void testSendQuestion() {
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("SUCCESS");
        String text = "Unit test question";

        UUID testId = UUID.randomUUID();
        UUID testUserId = UUID.randomUUID();
        ZonedDateTime roomTimeTest = ZonedDateTime.now();

        QuestionLogic.sendQuestion(text, testId, testUserId, roomTimeTest);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the question
        assertTrue(request.bodyPublisher().isPresent());

        Question userQuestion = new Question(text, 0, testId, testUserId,
                TimeControl.getMilisecondsPassed(roomTimeTest));
        String parsedQuestion = gson.toJson(userQuestion);
        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(parsedQuestion.length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void upvoteQuestion() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        UUID uuid = UUID.randomUUID();
        QuestionLogic.upvoteQuestion(uuid);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(uuid.toString().length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void answerQuestion() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        UUID uuid = UUID.randomUUID();
        QuestionLogic.answerQuestion(uuid);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(uuid.toString().length(), request.bodyPublisher().get().contentLength());
    }

}