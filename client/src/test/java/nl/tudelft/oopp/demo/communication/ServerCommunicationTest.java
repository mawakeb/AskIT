package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.data.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;


public class ServerCommunicationTest {

    private static Gson gson = new Gson();
    private ServerCommunication sc;
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

        sc = new ServerCommunication(client);
    }

    @Test
    void getStringHttpResponse() {
        when(response.statusCode()).thenReturn(200);

        ServerCommunication.getStringHttpResponse(request);

        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void testGetQuestions() {
        List<Question> expected = List.of(
                new Question(UUID.randomUUID(), "Q1", 4, UUID.randomUUID(), UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Q2", 5, UUID.randomUUID(), UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Q3", 6, UUID.randomUUID(), UUID.randomUUID()));
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);

        List<Question> actual = ServerCommunication.getQuestions();
        assertEquals(expected, actual);
    }

    @Test
    public void testSendQuestion() {
        when(response.statusCode()).thenReturn(200);
        String text = "Unit test question";

        ServerCommunication.sendQuestion(text);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        Question userQuestion = new Question(text, 0, null, null);
        String parsedQuestion = gson.toJson(userQuestion);
        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(parsedQuestion.length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void upvoteQuestion() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        UUID uuid = UUID.randomUUID();
        ServerCommunication.upvoteQuestion(uuid);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(uuid.toString().length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void createRoom() {
        List<String> expected = List.of("one", "two");
        String json = gson.toJson(expected);

        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        String name = "name";
        List<String> strings = ServerCommunication.createRoom(name);
        assertEquals("POST", request.method());

        assertEquals(strings.get(0),expected.get(0));
        assertEquals(strings.get(1),expected.get(1));
        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(name.toString().length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void closeRoom() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        // run with multiple randomly generated values,
        // to increase the credibility of comparing only content length
        for (int i = 0; i < 100; i++) {
            UUID roomID = UUID.randomUUID();
            ServerCommunication.closeRoom(roomID);
            assertEquals("POST", request.method());

            // check if a bodyPublisher was successfully included to transfer the value "123"
            assertTrue(request.bodyPublisher().isPresent());

            // bodyPublisher does not expose the contents directly, only length can be measured here
            assertEquals(roomID.toString().length(), request.bodyPublisher().get().contentLength());
        }
    }
}
