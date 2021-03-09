package nl.tudelft.oopp.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("create")
public class RoomController {
    private long roomId = 0L;
    @Autowired
    private RoomRepository repo;

    public RoomController() {
    }

    @PostMapping("room")
    @ResponseBody
    public List<String> createLink(@RequestBody String q) {
        String string1 = randomString();

        String string2;
        for(string2 = randomString(); string1.equals(string2); string2 = randomString()) {
        }

        ++this.roomId;
        Room newRoom = new Room(this.roomId, q, string1, string2);
        this.repo.save(newRoom);
        string1 = this.roomId + "/" + string1;
        string2 = this.roomId + "/" + string2;
        System.out.println(string1);
        System.out.println(string2);
        List<String> links = new ArrayList();
        links.add(string1);
        links.add(string2);
        return links;
    }

    public static String randomString() {
        String result = "";
        Random random = new Random();
        char[] characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

        for(int i = 0; i < 15; ++i) {
            int randomInt = random.nextInt(characters.length);
            result = result + characters[randomInt];
        }

        return result;
    }
}
