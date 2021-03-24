package nl.tudelft.oopp.askit.controllers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class UserController {

    private static Set<UUID> bannedUsers = new HashSet<>();
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;

    /**
     * Constructor for SendingController, autowired for JPA repositories.
     * @param repo repository with all questions
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public UserController(QuestionRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }


    /**
     * Prevent a user from sending new questions, using their id.
     */
    @PostMapping("ban")
    @ResponseBody
    public void banUser(@RequestBody String id) {
        UUID uuid = UUID.fromString(id);
        bannedUsers.add(uuid);
    }

    public static void setBannedUsers(Set<UUID> set) {
        bannedUsers = set;
    }

    public static Set<UUID> getBannedUsers() {
        return bannedUsers;
    }
}
