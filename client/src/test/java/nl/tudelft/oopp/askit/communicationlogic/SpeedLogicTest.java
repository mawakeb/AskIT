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
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.communication.ServerCommunication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

class SpeedLogicTest {

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
    void sendSpeed() {
        when(response.statusCode()).thenReturn(200);
        int speed = 6;
        UUID userId = UUID.randomUUID();
        String roomId = UUID.randomUUID().toString();
        List<String> sendList = List.of(
                Integer.toString(speed),
                userId.toString(),
                roomId
        );
        String parsedList = gson.toJson(sendList);
        when(response.body()).thenReturn(parsedList);

        SpeedLogic.sendSpeed(speed, userId, roomId);

        assertEquals("POST", request.method());

        // check if a bodyPublisher was successfully included to transfer the question
        assertTrue(request.bodyPublisher().isPresent());

        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(parsedList.length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void getSpeed() {
        int speed = 3;

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(gson.toJson(speed));

        int actualSpeed = SpeedLogic.getSpeed(UUID.randomUUID().toString());
        assertEquals(actualSpeed, speed);
    }
}