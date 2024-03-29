package nl.tudelft.oopp.askit.communicationlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import nl.tudelft.oopp.askit.communication.ServerCommunication;
import nl.tudelft.oopp.askit.data.Room;
import nl.tudelft.oopp.askit.methods.SerializingControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

class RoomLogicTest {

    private static final Gson gson = SerializingControl.getGsonObject();

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    // stores request sent by SUT
    private HttpRequest request;

    private Room room;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        room = new Room(UUID.randomUUID(),
                "Room Name",
                true,
                ZonedDateTime.now(),
                5);

        // clear request that might be left from earlier tests
        request = null;

        // supply response mock for calls to client.send(request, bodyHandler)
        // also stores the corresponding request for access during tests
        when(client.send(any(HttpRequest.class), ArgumentMatchers.any()))
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
        // Used this list to simulate the length
        List<String> sendList = List.of(
                name,
                time.toString()
        );
        String parsedList = gson.toJson(sendList);

        int length = parsedList.length();
        assertEquals(length, request.bodyPublisher().get().contentLength());
    }

    @Test
    void joinRoom() {
        String roomName = "room name";
        String roleId = "role";
        String openTime = ZonedDateTime.now().toString();
        List<String> expected = List.of(roomName, roleId, openTime);
        String json = gson.toJson(expected);

        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);

        List<String> strings = RoomLogic.joinRoom("epic/link");
        assertEquals("GET", request.method());

        assertNotNull(strings);
        assertEquals(strings.get(0), expected.get(0));
        assertEquals(strings.get(1), expected.get(1));
        assertEquals(strings.get(2), expected.get(2));

    }

    @Test
    void closeRoom() {
        // void type endpoint, so only mock response status code and not content
        when(response.statusCode()).thenReturn(200);

        UUID roomId = UUID.randomUUID();
        String roleId = "staff";

        RoomLogic.closeRoom(roomId.toString(), roleId);
        assertEquals("POST", request.method());

        // Simulated request list, to obtain the length
        assertTrue(request.bodyPublisher().isPresent());
        List<String> list = List.of(
                roomId.toString(),
                roleId
        );
        String parsedList = gson.toJson(list);
        // bodyPublisher does not expose the contents directly, only length can be measured here
        assertEquals(parsedList.length(), request.bodyPublisher().get().contentLength());
    }

    @Test
    void getNonExistentRoomStatus() {
        // mock boolean endpoint
        when(response.statusCode()).thenReturn(200);

        // should return null regardless of response,
        // because given string is null
        assertNull(RoomLogic.getRoomStatus(null));
    }

    @Test
    void getRoomStatus() {
        UUID testId = UUID.randomUUID();
        String roomJson = gson.toJson(room);

        // mock endpoint
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(roomJson);

        assertEquals(room, RoomLogic.getRoomStatus(testId.toString()));
    }

    @Test
    void setSlowMode() {
        UUID testId = UUID.randomUUID();
        when(response.statusCode()).thenReturn(200);
        RoomLogic.setSlowMode(testId.toString(), 42, "staff");

        // assert that a request has been sent.
        assertNotNull(request);
    }

    @Test
    void setSlowModeNullRoom() {
        RoomLogic.setSlowMode(null, 42, "staff");

        // assert that no request has been sent.
        assertNull(request);
    }
}