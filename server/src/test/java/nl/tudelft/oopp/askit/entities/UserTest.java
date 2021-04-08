package nl.tudelft.oopp.askit.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
    private static User user;
    private static UUID id;
    private static UUID roomId;
    private static String name;
    private static String role;
    private static String roleId;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        roomId = UUID.randomUUID();
        name = "name";
        role = "role";
        roleId = "roleId";
        user = new User(id, roomId, name, role, roleId);
    }

    @Test
    void getId() {
        assertEquals(id, user.getId());
    }

    @Test
    void getRoomId() {
        assertEquals(roomId, user.getRoomId());
    }

    @Test
    void setRoomId() {
        UUID different = UUID.randomUUID();
        user.setRoomId(different);
        assertEquals(different, user.getRoomId());
    }

    @Test
    void getName() {
        assertEquals(name, user.getName());
    }

    @Test
    void setName() {
        String different = "namess";
        user.setName(different);
        assertEquals(different, user.getName());
    }

    @Test
    void isBanned() {
        assertFalse(user.isBanned());
    }

    @Test
    void setBanned() {
        user.setBanned(true);
        assertTrue(user.isBanned());
    }

    @Test
    void getRole() {
        assertEquals(role, user.getRole());
    }

    @Test
    void setRole() {
        String different = "dif";
        user.setRole(different);
        assertEquals(different, user.getRole());
    }

    @Test
    void getRoleId() {
        assertEquals(roleId, user.getRoleId());
    }

    @Test
    void setRoleId() {
        String different = "dif";
        user.setRoleId(different);
        assertEquals(different, user.getRoleId());
    }

    @Test
    void testEquals() {
        User user1 = new User(id, roomId, name, role, roleId);
        assertEquals(user1, user);
    }
    @Test
    void testEquals1() {
        assertEquals(user, user);
    }
    @Test
    void testEquals2() {
        assertNotEquals(user, new Object());
    }

    @Test
    void testToString() {
        assertEquals(user.toString(), "User{" + "id=" + id
                + ", roomId="
                + roomId + ", name='"
                + name + '\'' + ", banned="
                + "false" + ", role='"
                + role + '\'' + ", roleId='"
                + roleId + '\'' + '}');
    }

}