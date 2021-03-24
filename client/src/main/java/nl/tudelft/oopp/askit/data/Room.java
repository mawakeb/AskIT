package nl.tudelft.oopp.askit.data;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Room {

    private UUID id;

    private String name;

    private boolean isOpen;

    private ZonedDateTime openTime;

    private int slowModeSeconds;

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
        return isOpen;
    }

    public ZonedDateTime getOpenTime() {
        return openTime;
    }

    public int getSlowModeSeconds() {
        return slowModeSeconds;
    }
}
