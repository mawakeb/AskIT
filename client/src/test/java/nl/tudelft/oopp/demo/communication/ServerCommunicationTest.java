package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import nl.tudelft.oopp.demo.data.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;


public class ServerCommunicationTest {

    private static final Gson gson = new Gson();

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    // request made by tests
    @Mock
    private HttpRequest mockRequest;

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
    void getStringHttpResponse() throws IOException, InterruptedException {
        // send an empty request
        HttpResponse<String> result = ServerCommunication.getStringHttpResponse(mockRequest);

        // the httpClient responds without errors by default in @BeforeEach
        // so the method should simple return the mocked response
        assertEquals(response, result);
    }

    @Test
    void getStringHttpResponseException() {
        // test the exception handling, getStringHttpResponse should not fail but just return null
        HttpResponse<String> result = null;
        try {
            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(IOException.class);
            result = ServerCommunication.getStringHttpResponse(mockRequest);
        } catch (Exception e) {
            // no exception should be thrown when initializing mock behaviour
        }

        assertNull(result);
    }

    @Test
    public void testGetQuestions() {

        UUID testId = UUID.randomUUID();

        List<Question> expected = List.of(
                new Question(UUID.randomUUID(), "Q1", 4, testId, UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Q2", 5, testId, UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Q3", 6, testId, UUID.randomUUID()));
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        List<Question> actual = ServerCommunication.getQuestions(testId.toString());
        assertEquals(expected, actual);
    }

    @Test
    public void testSendQuestion() {
        when(response.statusCode()).thenReturn(200);
        String text = "Unit test question";

        UUID testId = UUID.randomUUID();

        ServerCommunication.sendQuestion(text, testId.toString());
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the question
        assertTrue(request.bodyPublisher().isPresent());

        Question userQuestion = new Question(text, 0, testId, null);
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
        ZonedDateTime time = ZonedDateTime.now();
        List<String> strings = ServerCommunication.createRoom(name, time);
        assertEquals("POST", request.method());

        assertEquals(strings.get(0), expected.get(0));
        assertEquals(strings.get(1), expected.get(1));
        // check if a bodyPublisher was successfully included to transfer the room name
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        // 3 is for the '!@#' added
        int length = name.length() + time.toString().length() + 3;
        assertEquals(length, request.bodyPublisher().get().contentLength());
    }

    @Test
    void closeRoom() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        UUID roomId = UUID.randomUUID();
        ServerCommunication.closeRoom(roomId.toString());
        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the value "123"
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(roomId.toString().length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void getNotInitializedRoomStatus() {
        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        // should return false regardless of response,
        // because given string is null
        assertFalse(ServerCommunication.getRoomStatus(null));
    }

    @Test
    void getRoomStatus() {

        UUID testId = UUID.randomUUID();

        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        when(response.body()).thenReturn(Boolean.TRUE.toString());
        assertTrue(ServerCommunication.getRoomStatus(testId.toString()));

        when(response.body()).thenReturn(Boolean.FALSE.toString());
        assertFalse(ServerCommunication.getRoomStatus(testId.toString()));
    }
}
