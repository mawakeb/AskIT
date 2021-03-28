package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.SerializingControl;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
    private static final Gson gson = SerializingControl.getGsonObject();
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
        room = new Room(id, "test", "staff", "student", ZonedDateTime.now());
        rc = new RoomController(roomRepository);
    }


    @Test
    void createLink() {

        String info = "name!@#" + ZonedDateTime.now().toString();
        List<String> links = rc.createLink(info);
        // Sees if the room is saved
        verify(roomRepository, times(1)).save(any(Room.class));

        assertEquals(links.get(0).length(), 52);
        assertEquals(links.get(1).length(), 52);
    }

    @Test
    void testSizeIncrementCreateLink() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        int previous = room.getSize();
        String links = id.toString() + "/student";
        rc.joinLink(links);
        int current = room.getSize();

        // Check if it got incremented
        assertEquals(current - 1, previous);

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void closeRoom() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        rc.closeRoom(id.toString());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void closeNonExistentRoom() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(null);

        rc.closeRoom(id.toString());
        verify(roomRepository, times(0)).save(any(Room.class));

    }

    @Test
    void getRoomStatus() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        // note that non-transient attributes have been lost
        // this should not matter, as room.equals() only compares ID
        assertEquals(gson.toJson(room), rc.getRoomStatus(id.toString()));
    }

    @Test
    void getNonExistentRoomStatus() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> rc.getRoomStatus(id.toString()));
    }

    @Test
    void setSlowMode() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(room);

        rc.setSlowMode(id.toString(), 5);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void setSlowModeException() {
        // sets up a response
        when(roomRepository.findByid(id)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> rc.setSlowMode(id.toString(), 5));
    }
}