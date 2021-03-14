package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    private static final Gson gson = new Gson();
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
        ServerCommunication.setCurrentRoomId(null);
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
        ServerCommunication.sendQuestion("Unit test question");
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
    void closeRoom() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        // run with multiple randomly generated values,
        // to increase the credibility of comparing only content length
        for (int i = 0; i < 100; i++) {
            ServerCommunication.closeRoom();
            assertEquals("POST", request.method());

            // check if a bodyPublisher was successfully included to transfer the value "123"
            assertTrue(request.bodyPublisher().isPresent());

            UUID roomID = UUID.randomUUID();
            // bodyPublisher does not expose the contents directly, only length can be measured here
            assertEquals(roomID.toString().length(), request.bodyPublisher().get().contentLength());
        }
    }

    @Test
    void getNotInitializedRoomStatus() {
        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        // should return false regardless of response,
        // because currentRoomId hasn't been initialized
        assertFalse(ServerCommunication.getRoomStatus());
    }

    @Test
    void getRoomStatus() {
        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        // initialize currentRoomId to be non-null
        // not actually used by mock objects
        ServerCommunication.setCurrentRoomId(UUID.randomUUID());

        when(response.body()).thenReturn(Boolean.TRUE.toString());
        assertTrue(ServerCommunication.getRoomStatus());

        when(response.body()).thenReturn(Boolean.FALSE.toString());
        assertFalse(ServerCommunication.getRoomStatus());
    }
}
