package nl.tudelft.oopp.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.controllers.RoomController;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
    @Mock
    RoomRepository roomRepository;
    @Mock
    private Room room;

    private UUID id;

    private RoomController rc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        id = UUID.randomUUID();
        room = new Room(id, "test", "staff", "student", LocalDateTime.now());
        rc = new RoomController(roomRepository);
    }

    @Test
    public void checkIfRandomStringGetsCreated() {
        String randString = RoomController.randomString();

        assertEquals(randString.length(), 15);
        assertNotNull(randString);
    }

    @Test
    void createLink() {
        List<String> links = rc.createLink(room.getName());
        // Sees if the room is saved
        verify(roomRepository, times(1)).save(any(Room.class));

        assertEquals(links.get(0).length(), 52);
        assertEquals(links.get(1).length(), 52);
    }

    @Test
    void closeRoom() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        rc.closeRoom(id.toString());

        verify(roomRepository, times(1)).save(any(Room.class));

    }

    @Test
    void getRoomStatus() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        // room is initialized as open
        assertTrue(rc.getRoomStatus(id.toString()));

        room.close();
        assertFalse(rc.getRoomStatus(id.toString()));
    }

    @Test
    void getNonExistentRoomStatus() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(null);
        assertFalse(rc.getRoomStatus(id.toString()));
    }
}