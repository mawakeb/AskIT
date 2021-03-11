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
    public void testGetQuestions() {
        List<Question> expected = List.of(
                new Question(0, "Q1", 4),
                new Question(1, "Q2", 5),
                new Question(3, "Q3", 6));
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);

        List<Question> actual = sc.getQuestions();
        assertEquals(expected, actual);
    }

    @Test
    public void testSendQuestion() {
        sc.sendQuestion("Unit test question");
    }

    @Test
    void upvoteQuestion() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        ServerCommunication.upvoteQuestion(123);
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(3, request.bodyPublisher().get().contentLength());
    }
}
