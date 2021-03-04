package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class SendingController {

    @Autowired
    private QuestionRepository repo;

    // TODO: ideally this should be handled by an autoincrement on the DB side
    private static long idCounter = 0;

    @GetMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestParam String q) {
        Question question = new Question(idCounter++,q);
        repo.save(question);
        System.out.println(q);
    }
}
