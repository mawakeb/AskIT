package nl.tudelft.oopp.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomRepository repo;

    public RoomController() {
    }

    /**
     * generate random String to use as room s codes.
     *
     * @return String code of 15 characters.
     */
    public static String randomString() {
        String result = "";
        Random random = new Random();
        char[] characters = ("abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").toCharArray();

        for (int i = 0; i < 15; ++i) {
            int randomInt = random.nextInt(characters.length);
            result = result + characters[randomInt];
        }

        return result;
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

        String string2;
        for (string2 = randomString(); string1.equals(string2); string2 = randomString()) {
        }
        UUID roomId = UUID.randomUUID();
        Room newRoom = new Room(roomId, q, string1, string2);
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

    /** Close a room from asking new questions.
     *
     * @param id the request body containing room ID in string form
     */
    @PostMapping("close")
    @ResponseBody
    public void closeRoom(@RequestBody String id) {
        System.out.println("Closing room on server, id:" + id);
        UUID uuid = UUID.fromString(id);
        Room room = repo.findByid(uuid);

        // TODO: room is null because the room id is not sent correctly from client. Adding null check for now
        if(room == null) {
            System.out.println("TODO: room is null because the room id is not sent correctly from client. Adding null check for now");
            return;
        }
        room.close();
        repo.save(room);
    }
}
