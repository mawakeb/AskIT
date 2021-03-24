package nl.tudelft.oopp.askit.data;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

    private Room room;
    private UUID id;
    private String name;
    private boolean isOpen;
    private ZonedDateTime openTime;
    private int slowModeSeconds;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        name = "Room Name";
        isOpen = true;
        openTime = ZonedDateTime.now();
        slowModeSeconds = 5;
        room = new Room(id,name,isOpen,openTime,slowModeSeconds);
    }

    @Test
    void getId() {
        assertEquals(id,room.getId());
    }

    @Test
    void getName() {
        assertEquals(name,room.getName());
    }

    @Test
    void isOpen() {
        assertEquals(isOpen,room.isOpen());
    }

    @Test
    void getOpenTime() {
        assertEquals(openTime,room.getOpenTime());
    }

    @Test
    void getSlowModeSeconds() {
        assertEquals(slowModeSeconds,room.getSlowModeSeconds());
    }
}