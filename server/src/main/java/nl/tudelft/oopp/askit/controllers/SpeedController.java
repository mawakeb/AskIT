package nl.tudelft.oopp.askit.controllers;

import static nl.tudelft.oopp.askit.methods.SpeedMethods.roomSpeedWeighted;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.SpeedMethods;
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
    private final RoomRepository roomRepo;

    private static final HashMap<UUID, Integer> userVote = new HashMap<>();
    private static final HashMap<UUID, List<Integer>> roomSpeed = new HashMap<>();

    /**
     * Constructor for SpeedController, autowired for JPA repositories.
     *
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public SpeedController(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    /**
     * Returns the rooms speed.
     *
     * @param ids contains UUID of the room and moderator code
     * @return the most voted speed. If no one has voted or not
     *          enough votes, return default speed (2)
     */
    @PostMapping("get")
    @ResponseBody
    public int getSpeed(@RequestBody String ids) {
        List<String> list = gson.fromJson(ids, new TypeToken<List<String>>() {
        }.getType());
        UUID roomId = UUID.fromString(list.get(0));
        String roleId = list.get(1);

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

        int size = room.getSize();
        List<Integer> speedList = roomSpeed.get(roomId);
        if (speedList == null) {
            return 2;
        }
        // Returns the changed speed if it passes 10% of the student count size threshold.
        return roomSpeedWeighted(size, 0.1, speedList);
    }

    /** Updates the vote for what speed the user chooses.
     *
     * @param s List that contains int speed, UUID userId, UUID roomId
     */
    @PostMapping("send")
    @ResponseBody
    public void sendSpeed(@RequestBody String s) {
        List<String> list = gson.fromJson(s, new TypeToken<List<String>>() {
        }.getType());
        int speed = Integer.parseInt(list.get(0));
        UUID userId = UUID.fromString(list.get(1));
        UUID roomId = UUID.fromString(list.get(2));

        try {
            roomRepo.findByid(roomId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Room not found");
        }

        // Checks if user voted before
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
            System.out.println(previousSpeed);
            speedList.set(previousSpeed, previous);

            // Adds the new vote by incrementing speed
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

    public static HashMap<UUID, Integer> getUserVote() {
        return userVote;
    }

    public static HashMap<UUID, List<Integer>> getRoomSpeed() {
        return roomSpeed;
    }
}
