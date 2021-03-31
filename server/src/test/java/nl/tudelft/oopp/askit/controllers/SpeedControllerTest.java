package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.SpeedMethods;
import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SpeedControllerTest {
    private static final Gson gson = new Gson();

    @Mock
    RoomRepository roomRepository;
    @Mock
    private Room room;

    private UUID id;
    private UUID userId;

    private SpeedController sc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        id = UUID.randomUUID();
        userId = UUID.randomUUID();
        room = new Room(id, "test", "staff", "student", ZonedDateTime.now());
        sc = new SpeedController(roomRepository);
    }

    @Test
    void getSpeed() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);
        // Sets up the hashmap
        SpeedController.getRoomSpeed().put(id, List.of(
                0,
                1,
                0,
                0,
                0
        ));
        room.incrementSize();
        int result = sc.getSpeed(id.toString());
        assertEquals(result, 1);
    }

    @Test
    void sendSpeedFirstTime() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        int prevVote = 4;
        List<String> list = List.of(
                Integer.toString(prevVote),
                userId.toString(),
                id.toString()
        );
        String parsed = gson.toJson(list);

        sc.sendSpeed(parsed);

        // Checks if users speed got registered
        int userVote = SpeedController.getUserVote().get(userId);
        assertEquals(userVote, prevVote);

        // Checks if the value got incremented for the room
        int incremented = SpeedController.getRoomSpeed().get(id).get(userVote);
        assertEquals(incremented, 1);

    }

    @Test
    void sendSpeedSecondTime() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        // Sets a vote that should be previously stored
        int prevVote = 4;
        SpeedController.getUserVote().put(userId, prevVote);

        // Sets up a speedlist that should exist
        List<Integer> speedList = SpeedMethods.generateList();
        speedList.set(prevVote, 1);
        SpeedController.getRoomSpeed().put(id, speedList);

        // sets up the list that needs to be processed
        int changedVote = 3;
        List<String> list = List.of(
                Integer.toString(changedVote),
                userId.toString(),
                id.toString()
        );
        String parsed = gson.toJson(list);

        sc.sendSpeed(parsed);

        // Checks if users speed got replaced
        int userVote = SpeedController.getUserVote().get(userId);
        assertEquals(userVote, changedVote);

        // Check if the list is changed correclty
        List<Integer> changedList = SpeedController.getRoomSpeed().get(id);
        assertEquals(changedList, List.of(
                0,
                0,
                0,
                1,
                0
        ));

    }

    @Test
    void getUserVote() {
        assertNotNull(SpeedController.getUserVote());
    }

    @Test
    void getRoomSpeed() {
        assertNotNull(SpeedController.getRoomSpeed());

    }
}