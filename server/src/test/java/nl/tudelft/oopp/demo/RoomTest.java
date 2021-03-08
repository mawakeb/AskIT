package nl.tudelft.oopp.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import nl.tudelft.oopp.demo.entities.Room;

class RoomTest {
    Room testRoomA;
    Room testRoomB;
    Room testRoomC;
    Room testRoomD;

    @Test
    public void workingEmptyConstructorTest(){
        Room testRoom = new Room();
        assertNotNull(testRoom);
    }

    @Test
    public void workingFilledConstructorTest(){
        Room testRoom = new Room(14, "Room N", "abc", "def");
        assertNotNull(testRoom);
    }

    @BeforeEach
    public void setup(){
        testRoomA = new Room(1, "Room A", "abc", "def");
        testRoomB = new Room(2, "Room B", "ace", "bdf");
        testRoomC = new Room(1, "Room C", "abc", "def");

        // Rooms are identified by their ID,
        // so rooms A & C are identical, regardless of the different room names.
    }

    @Test
    public void sameRoomObjectEqualsTest(){
        assertTrue(testRoomA.equals(testRoomA));
    }

    @Test
    public void differentRoomsSameAttributesEqualsTest(){
        assertTrue(testRoomA.equals(testRoomC));
    }

    @Test
    public void differentRoomsEqualsTest(){
        assertFalse(testRoomA.equals(testRoomB));
    }

    @Test
    public void catchesNullPointerTest(){
        Room testRoom = new Room();
        assertFalse(testRoom.equals(testRoomA));
    }
}
