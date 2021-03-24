package nl.tudelft.oopp.askit.communicationlogic;

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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.communication.ServerCommunication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

class RoomLogicTest {

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
    void createRoom() {
        List<String> expected = List.of("one", "two");
        String json = gson.toJson(expected);

        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        String name = "name";
        ZonedDateTime time = ZonedDateTime.now();
        List<String> strings = RoomLogic.createRoom(name, time);
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
        RoomLogic.closeRoom(roomId.toString());
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
        assertFalse(RoomLogic.getRoomStatus(null));
    }

    @Test
    void getRoomStatus() {

        UUID testId = UUID.randomUUID();

        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        when(response.body()).thenReturn(Boolean.TRUE.toString());
        assertTrue(RoomLogic.getRoomStatus(testId.toString()));

        when(response.body()).thenReturn(Boolean.FALSE.toString());
        assertFalse(RoomLogic.getRoomStatus(testId.toString()));
    }

}