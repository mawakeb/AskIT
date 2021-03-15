package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("get")
public class GettingController {

    private QuestionRepository repo;

    @Autowired
    public GettingController(QuestionRepository repo) {
        this.repo = repo;
    }

    // Endpoint that returns a list containing all questions in the repository
    @GetMapping("questions") // for /get/questions
    @ResponseBody
    public List<Question> getQuestions(@RequestParam String q) {
        return repo.getAllRoomQuestions(UUID.fromString(q));
    }
}
