package nl.tudelft.oopp.askit.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.SpeedMethods;
import nl.tudelft.oopp.askit.repositories.QuestionRepository;
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
@RequestMapping("speed")
public class SpeedController {
    private static final Gson gson = new Gson();
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;

    private static HashMap<UUID, Integer> userVote = new HashMap<>();
    private static HashMap<UUID, List<Integer>> roomSpeed = new HashMap<>();

    /**
     * Constructor for UserController, autowired for JPA repositories.
     *
     * @param repo     repository with all questions
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public SpeedController(QuestionRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    @PostMapping("send")
    @ResponseBody
    public void sendSpeed(@RequestBody String s) {
        List<String> list = gson.fromJson(s, new TypeToken<List<String>>() {
        }.getType());
        int speed = Integer.parseInt(list.get(0));
        UUID userId = UUID.fromString(list.get(1));
        UUID roomId = UUID.fromString(list.get(2));

        Room room;
        try {
            room = roomRepo.findByid(roomId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Room not found");
        }

        if (userVote.containsKey(userId)) {
            // Checks if the previous value isn't null
            int previousSpeed = Objects.requireNonNullElse(userVote.put(userId, speed), 2);

            List<Integer> speedList = roomSpeed.get(roomId);

            // Resets previous vote
            int previous = speedList.get(previousSpeed);
            previous--;
            if (previous < 0) {
                previous = 0;
            }
            speedList.set(previousSpeed, previous);

            // Adds the new vote
            previous = speedList.get(speed);
            previous++;
            roomSpeed.get(roomId).set(speed, previous);

        } else {
            // Saves the vote
            userVote.put(userId, speed);

            // Gets or makes the roomSpeed list
            List<Integer> speedList = roomSpeed.get(roomId);
            if (speedList == null) {
                speedList = SpeedMethods.generateList();
                roomSpeed.put(roomId, speedList);
            }

            // Increments the specified speed
            int previous = speedList.get(speed);
            previous++;
            roomSpeed.get(roomId).set(speed, previous);
        }
    }
}
