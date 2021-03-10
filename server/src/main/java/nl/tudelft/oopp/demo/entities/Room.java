package nl.tudelft.oopp.demo.entities;

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
    private long id;
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

    public Room() {
    }

    /** Constructor for the Room class.
     *
     * @param id unique room UUID.
     * @param name title of the room chosen by lecturer.
     * @param staff room access code for the staff role.
     * @param student room acces code for the student role.
     */
    public Room(long id, String name, String staff, String student) {
        this.id = id;
        this.name = name;
        this.staff = staff;
        this.student = student;
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