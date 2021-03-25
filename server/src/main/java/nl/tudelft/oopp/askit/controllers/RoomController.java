package nl.tudelft.oopp.askit.controllers;

import static nl.tudelft.oopp.askit.methods.GenerationMethods.randomString;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("room")
public class RoomController {

    private RoomRepository repo;

    @Autowired
    public RoomController(RoomRepository repo) {
        this.repo = repo;
    }


    /**
     * Create new room on the server and generate two access links.
     *
     * @param q the String title of the room to create.
     * @return list of two strings, containing join links for staff and student respectively.
     */
    @PostMapping("create")
    @ResponseBody
    public List<String> createLink(@RequestBody String q) {
        String string1 = randomString();

        String[] info = q.split("!@#");
        ZonedDateTime time = ZonedDateTime.parse(info[1]);

        String string2;
        for (string2 = randomString(); string1.equals(string2); string2 = randomString()) {
        }
        UUID roomId = UUID.randomUUID();
        Room newRoom = new Room(roomId, info[0], string1, string2, time);
        this.repo.save(newRoom);

        // TODO: links are way too long because of room id
        string1 = roomId + "/" + string1;
        string2 = roomId + "/" + string2;
        System.out.println(string1);
        System.out.println(string2);
        List<String> links = new ArrayList<>();
        links.add(string1);
        links.add(string2);
        return links;
    }


    /**
     * Receives client request to join room and verifies the link type.
     *
     * @param q the join link
     */
    @GetMapping("join")
    @ResponseBody
    public List<String> joinLink(@RequestParam String q) {
        String[] links = q.split("/");

        // Checks if format is correct
        if (links.length != 2) {
            System.out.println("Invalid link");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid link");
        }
        // Checks if UUID is correct format
        UUID id;
        try {
            id = UUID.fromString(links[0]);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid link");
        }
        String role = links[1];
        Room room = this.repo.findByid(id);
        if (room != null) {

            if (role.equals(room.getStudent())) {
                System.out.println("You are a student");
                System.out.println("Successfully joined");
                return List.of(room.getName(),"student", room.getOpenTime().toString());
            }
            if (role.equals(room.getStaff())) {
                System.out.println("You are a staff");
                System.out.println("Successfully joined");
                return List.of(room.getName(),"staff", room.getOpenTime().toString());
            }

            System.out.println("Incorrect role code");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect role code");
        } else {
            System.out.println("Room not found");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Room not found");
        }
    }

    /**
     * Close a room from asking new questions.
     *
     * @param id the request body containing room ID in string form
     */
    @PostMapping("close")
    @ResponseBody
    public void closeRoom(@RequestBody String id) {
        System.out.println("Closing room on server, id:" + id);
        UUID uuid = UUID.fromString(id);
        Room room = repo.findByid(uuid);

        if (room == null) {
            System.out.println("room doesnt exist, id:" + id);
            return;
        }
        room.close();
        repo.save(room);
    }

    /**
     * Get the status of a room by ID.
     *
     * @param id the request body containing room ID in string form
     * @return a boolean, true iff the room is open for new questions,
     *           also returns false if the room can't be found.
     */
    @GetMapping("status")
    @ResponseBody
    public boolean getRoomStatus(@RequestParam String id) {
        UUID uuid = UUID.fromString(id);
        Room room = repo.findByid(uuid);
        if (room == null) {
            return false;
        }
        return room.isOpen();
    }
}