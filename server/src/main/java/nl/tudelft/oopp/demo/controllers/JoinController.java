package nl.tudelft.oopp.demo.controllers;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"join"})
public class JoinController {
    @Autowired
    private RoomRepository repo;

    public JoinController() {
    }

    /**
     * Receives client request to join room and verifies the link type.
     *
     * @param q the join link
     */
    @GetMapping({"link"})
    @ResponseBody
    public String joinLink(@RequestParam String q) {
        String[] links = q.split("/");
        UUID id = UUID.fromString(links[0]);
        String role = links[1];
        Room room = this.repo.findByid(id);
        if (room != null) {

            if (role.equals(room.getStudent())) {
                System.out.println("You are a student");
                System.out.println("Successfully joined");
                return "student";
            }
            if (role.equals(room.getStaff())) {
                System.out.println("You are a staff");
                System.out.println("Successfully joined");
                return "staff";
            }

            System.out.println("Incorrect role code");

        } else {
            System.out.println("Room not found");
        }
        return null;

    }
}

