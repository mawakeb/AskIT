package nl.tudelft.oopp.demo.entities;

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
    @Column(
            name = "id"
    )
    private UUID id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "staffAccess"
    )
    private String staff;
    @Column(
            name = "studentAccess"
    )
    private String student;

    @Column(name = "isOpen")
    private boolean isOpen;

    public Room() {
    }

    /**
     * Constructor for the Room class.
     *
     * @param id      unique room UUID.
     * @param name    title of the room chosen by lecturer.
     * @param staff   room access code for the staff role.
     * @param student room acces code for the student role.
     */
    public Room(UUID id, String name, String staff, String student) {
        this.id = id;
        this.name = name;
        this.staff = staff;
        this.student = student;
        this.isOpen = true;
    }

    /**
     * Constructor for the Room class without specifying id.
     *
     * @param name    title of the room chosen by lecturer.
     * @param staff   room access code for the staff role.
     * @param student room acces code for the student role.
     */
    public Room(String name, String staff, String student) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.staff = staff;
        this.student = student;
        this.isOpen = true;
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
        return this.isOpen;
    }

    public void close() {
        this.isOpen = false;
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