package nl.tudelft.oopp.demo.entities;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "rooms"
)
public class Room {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "staffAccess")
    private String staff;

    @Column(name = "studentAccess")
    private String student;

    @Column(name = "isOpen")
    private boolean isOpen;

    @Column(name = "openTime")
    private ZonedDateTime openTime;

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
    }

    /**
     * Constructor for the Room class without specifying id.
     *
     * @param name    title of the room chosen by lecturer.
     * @param staff   room access code for the staff role.
     * @param student room acces code for the student role.
     */
    public Room(String name, String staff, String student, LocalDateTime openTime) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.staff = staff;
        this.student = student;
        this.isOpen = true;
        this.openTime = openTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Room room = (Room) o;
            return this.id == room.id;
        } else {
            return false;
        }
    }
}