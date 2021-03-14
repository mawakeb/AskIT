package nl.tudelft.oopp.demo.entities;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private final UUID id;
    @Column(name = "roomId")
    private UUID roomId;
    @Column(name = "name")
    private String name;
    @Column(name = "banned")
    private boolean banned;
    @Column(name = "role")
    private String role;
    @Column(name = "roleId")
    private String roleId;

    /**
     * Creates the user object.
     *
     * @param id     identifies user
     * @param roomId room the user belongs to
     *               banned - is the user currently banned (not allowed to comment)
     * @param role   name of the users role
     * @param roleId secret Id that the user has, what gives him his privileges
     */
    public User(UUID id, UUID roomId, String name, String role, String roleId) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.banned = false;
        this.role = role;
        this.roleId = roleId;
    }

    /**
     * Creates the user object.
     *
     * @param roomId room the user belongs to
     *               banned - is the user currently banned (not allowed to comment)
     * @param role   name of the users role
     * @param roleId secret Id that the user has, what gives him his privileges
     */
    public User(UUID roomId, String role, String name, String roleId) {
        this.id = UUID.randomUUID();
        this.roomId = roomId;
        this.name = name;
        this.banned = false;
        this.role = role;
        this.roleId = roleId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return banned == user.banned
                && Objects.equals(id, user.id)
                && Objects.equals(roomId, user.roomId)
                && Objects.equals(role, user.role)
                && Objects.equals(roleId, user.roleId);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", roomId=" + roomId + ", banned=" + banned
                + ", role='" + role + '\'' + ", roleId='" + roleId + '\'' + '}';
    }
}
