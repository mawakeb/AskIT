package nl.tudelft.oopp.askit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(
        name = "rooms",
        indexes = @Index(columnList = "id")
        )
public class Room {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "staffAccess")
    private String staff;

    @JsonIgnore
    @Column(name = "studentAccess")
    private String student;

    @Column(name = "isOpen")
    private boolean isOpen;

    @Column(name = "openTime")
    private ZonedDateTime openTime;

    @Column(name = "studentSize")
    private int studentSize;

    @Column(name = "slowModeSeconds")
    private int slowModeSeconds;

    public Room() {
    }

    /**
     * Constructor for the Room class.
     *
     * @param id      unique room UUID.
     * @param name    title of the room chosen by lecturer.
     * @param staff   room access code for the staff role.
     * @param student room access code for the student role.
     */
    public Room(UUID id, String name, String staff, String student, ZonedDateTime openTime) {
        this.id = id;
        this.name = name;
        this.staff = staff;
        this.student = student;
        this.isOpen = true;
        this.openTime = openTime;
        this.slowModeSeconds = 0;
        this.studentSize = 0;
    }

    public int getSize() {
        return studentSize;
    }

    public void incrementSize() {
        this.studentSize++;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getStaff() {
        return this.staff;
    }

    public String getStudent() {
        return this.student;
    }

    public boolean isOpen() {
        return this.isOpen && ZonedDateTime.now().isAfter(this.openTime);
    }

    public void close() {
        this.isOpen = false;
    }

    public ZonedDateTime getOpenTime() {
        return this.openTime;
    }

    public void setSlowModeSeconds(int slowModeSeconds) {
        this.slowModeSeconds = slowModeSeconds;
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
                && studentSize == room.studentSize
                && slowModeSeconds == room.slowModeSeconds
                && Objects.equals(id, room.id)
                && Objects.equals(name, room.name)
                && Objects.equals(staff, room.staff)
                && Objects.equals(student, room.student)
                && Objects.equals(openTime, room.openTime);
    }

}