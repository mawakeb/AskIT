package nl.tudelft.oopp.askit.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

        // add offset time to prevent race condition in isOpen()
        openTime = ZonedDateTime.now().minus(1, ChronoUnit.MINUTES);

        slowModeSeconds = 5;
        room = new Room(id, name, isOpen, openTime, slowModeSeconds);
    }

    @Test
    void getId() {
        assertEquals(id, room.getId());
    }

    @Test
    void getName() {
        assertEquals(name, room.getName());
    }

    @Test
    void isOpen() {
        assertEquals(isOpen, room.isOpen());
    }

    @Test
    void getOpenTime() {
        assertEquals(openTime, room.getLocalOpenTime());
    }

    @Test
    void testGetOpenTime() {
        assertEquals(openTime, room.getOpenTime());
    }

    @Test
    void getSlowModeSeconds() {
        assertEquals(slowModeSeconds, room.getSlowModeSeconds());
    }

    @Test
    void testEquals() {
        Room room1 = new Room(id, name, isOpen, openTime, slowModeSeconds);
        assertEquals(room, room1);
    }

    @Test
    void testEquals1() {
        assertNotEquals(room, new Object());
    }

    @Test
    void testEquals2() {
        assertEquals(room, room);
    }
}