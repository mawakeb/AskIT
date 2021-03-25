package nl.tudelft.oopp.askit.data;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

public class Room {

    private final UUID id;

    private final String name;

    private final boolean isOpen;

    private final ZonedDateTime openTime;

    private final int slowModeSeconds;

    /**
     * Constructor for the room class.
     *
     * @param id              UUID of the room
     * @param name            Name of the room chosen by creator
     * @param isOpen          False if the room has been closed manually, true otherwise
     * @param openTime        Scheduled time after which the room opens
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

    public ZonedDateTime getLocalOpenTime() {
        return openTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
    }

    public int getSlowModeSeconds() {
        return slowModeSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return isOpen == room.isOpen
                && slowModeSeconds == room.slowModeSeconds
                && Objects.equals(id, room.id)
                && Objects.equals(name, room.name)
                && Objects.equals(openTime, room.openTime);
    }
}
