package nl.tudelft.oopp.askit.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("send")
public class UserController {
    private static final Gson gson = new Gson();
    private static Set<UUID> bannedUsers = new HashSet<>();

    private final RoomRepository roomRepo;

    /**
     * Constructor for UserController, autowired for JPA repositories.
     *
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public UserController(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    /**
     * Prevent a user from sending new questions, using their id.
     *
     * @param ids contains userId, roleId and roomId
     */
    @PostMapping("ban")
    @ResponseBody
    public void banUser(@RequestBody String ids) {
        List<String> list = gson.fromJson(ids, new TypeToken<List<String>>() {
        }.getType());
        UUID userId = UUID.fromString(list.get(0));
        String roleId = list.get(1);
        UUID roomId = UUID.fromString(list.get(2));

        Room room = roomRepo.findByid(roomId);
        if (room == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Room not found");
        }

        // Checks if moderator
        if (!room.getStaff().equals(roleId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not a moderator of this room");
        }

        bannedUsers.add(userId);
    }

    public static void setBannedUsers(Set<UUID> set) {
        bannedUsers = set;
    }

    public static Set<UUID> getBannedUsers() {
        return bannedUsers;
    }
}
