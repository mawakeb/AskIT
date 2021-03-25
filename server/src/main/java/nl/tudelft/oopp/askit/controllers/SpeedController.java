package nl.tudelft.oopp.askit.controllers;

import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("speed")
public class SpeedController {
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;

    /**
     * Constructor for UserController, autowired for JPA repositories.
     * @param repo repository with all questions
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public SpeedController(QuestionRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("send")
    @ResponseBody
    public void sendSpeed(@RequestParam String s) {
        System.out.println(Integer.parseInt(s));
    }
}
