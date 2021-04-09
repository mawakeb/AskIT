package nl.tudelft.oopp.askit.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {
    private Room testRoomA;
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

        // add offset time to prevent race condition in isOpen()
        time = ZonedDateTime.now().minus(1, ChronoUnit.MINUTES);

        testRoomA = new Room(dupe, name, staff, student, time);

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
        assertTrue(testRoomA.isOpen());
    }

    @Test
    void slowModeSeconds() {
        // value should be initialized as 0 in constructor
        assertEquals(0, testRoomA.getSlowModeSeconds());
        testRoomA.setSlowModeSeconds(42);
        assertEquals(42, testRoomA.getSlowModeSeconds());

    }

    @Test
    void close() {
        testRoomA.close();
        assertFalse(testRoomA.isOpen());
    }

    @Test
    public void sameRoomObjectEqualsTest() {
        assertEquals(testRoomA, testRoomA);
    }

    @Test
    void testEquals2() {
        assertNotEquals(testRoomA, new Object());
    }

    @Test
    void testEquals() {
        Room testRoomB = new Room(dupe, name, staff, student, time);
        assertEquals(testRoomA, testRoomB);
    }

    @Test
    public void catchesNullPointerTest() {
        Room testRoom = new Room();
        assertNotEquals(testRoomA, testRoom);
    }
}
