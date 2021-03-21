package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

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
}

