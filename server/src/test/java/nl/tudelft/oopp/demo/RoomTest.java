package nl.tudelft.oopp.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {
    private Room testRoomA;
    private Room testRoomB;
    private Room testRoomC;
    private UUID dupe;
    private String name;
    private String staff;
    private String student;
    private ZonedDateTime time;

    @Test
    public void workingEmptyConstructorTest() {
        Room testRoom = new Room();
        assertNotNull(testRoom);
    }

    @Test
    public void workingFilledConstructorTest() {
        Room testRoom = new Room(UUID.randomUUID(), "Room N", "abc", "def", ZonedDateTime.now());
        assertNotNull(testRoom);
    }

    @BeforeEach
    public void setup() {
        dupe = UUID.randomUUID();
        name = "Room A";
        staff = "abc";
        student = "def";
        time = ZonedDateTime.now();

        testRoomA = new Room(dupe, name, staff, student, time);
        testRoomB = new Room(UUID.randomUUID(), "Room B", "ace", "bdf", time);
        testRoomC = new Room(dupe, "Room C", "abc", "def", time);

        // Rooms are identified by their ID,
        // so rooms A & C are identical, regardless of the different room names.
    }

    @Test
    void getId() {
        assertEquals(testRoomA.getId(), dupe);
    }

    @Test
    void getName() {
        assertEquals(testRoomA.getName(), name);
    }

    @Test
    void getStaff() {
        assertEquals(testRoomA.getStaff(), staff);
    }

    @Test
    void getStudent() {
        assertEquals(testRoomA.getStudent(), student);
    }

    @Test
    void getOpenTime() {
        assertEquals(testRoomA.getOpenTime(), time);
    }

    @Test
    void isOpen() {
        assertEquals(testRoomA.isOpen(), true);
    }

    @Test
    void close() {
        testRoomA.close();
        assertEquals(testRoomA.isOpen(),false);
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
