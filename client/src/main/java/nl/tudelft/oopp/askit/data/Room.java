package nl.tudelft.oopp.askit.data;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Room {

    private UUID id;

    private String name;

    private boolean isOpen;

    private ZonedDateTime openTime;

    private int slowModeSeconds;

    /**
     * Constructor for the room class.
     * @param id UUID of the room
     * @param name Name of the room chosen by creator
     * @param isOpen False if the room has been closed manually, true otherwise
     * @param openTime Scheduled time after which the room opens
     * @param slowModeSeconds Min. amount of seconds between questions per student
     */
    public Room(UUID id, String name, boolean isOpen, ZonedDateTime openTime, int slowModeSeconds) {
        this.id = id;
        this.name = name;
        this.isOpen = isOpen;
        this.openTime = openTime;
        this.slowModeSeconds = slowModeSeconds;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return this.isOpen && ZonedDateTime.now().isAfter(this.openTime);
    }

    public ZonedDateTime getOpenTime() {
        return openTime;
    }

    public int getSlowModeSeconds() {
        return slowModeSeconds;
    }
}
