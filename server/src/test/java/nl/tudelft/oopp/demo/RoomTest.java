package nl.tudelft.oopp.demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {
    Room testRoomA;
    Room testRoomB;
    Room testRoomC;

    @Test
    public void workingEmptyConstructorTest() {
        Room testRoom = new Room();
        assertNotNull(testRoom);
    }

    @Test
    public void workingFilledConstructorTest() {
        Room testRoom = new Room(UUID.randomUUID(), "Room N", "abc", "def");
        assertNotNull(testRoom);
    }

    @BeforeEach
    public void setup() {
        UUID dupe = UUID.randomUUID();
        testRoomA = new Room(dupe, "Room A", "abc", "def");
        testRoomB = new Room(UUID.randomUUID(), "Room B", "ace", "bdf");
        testRoomC = new Room(dupe, "Room C", "abc", "def");

        // Rooms are identified by their ID,
        // so rooms A & C are identical, regardless of the different room names.
    }

    @Test
    public void sameRoomObjectEqualsTest() {
        assertTrue(testRoomA.equals(testRoomA));
    }

    @Test
    public void differentRoomsSameAttributesEqualsTest() {
        assertTrue(testRoomA.equals(testRoomC));
    }

    @Test
    public void differentRoomsEqualsTest() {
        assertFalse(testRoomA.equals(testRoomB));
    }

    @Test
    public void catchesNullPointerTest() {
        Room testRoom = new Room();
        assertFalse(testRoom.equals(testRoomA));
    }
}
