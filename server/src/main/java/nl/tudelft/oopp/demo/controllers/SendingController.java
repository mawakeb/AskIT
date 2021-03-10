package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("send")
public class SendingController {

    private QuestionRepository repo;

    @Autowired
    public SendingController(QuestionRepository repo){
        this.repo = repo;
    }

    // TODO: ideally this should be handled by an autoincrement on the DB side
    private static long idCounter = 0;

    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        Question question = new Question(idCounter++,q);
        repo.save(question);
        System.out.println(q);
    }
}
