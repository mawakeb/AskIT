package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class SendingController {

    // TODO: ideally this should be handled by an autoincrement on the DB side
    private static long idCounter = 0;
    private QuestionRepository repo;

    @Autowired
    public SendingController(QuestionRepository repo) {
        this.repo = repo;
    }

    /**
     * Receive question sent by a student and store it in the repository.
     *
     * @param q String containing only the question text content.
     */
    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        Question question = new Question(idCounter++, q);
        repo.save(question);
        System.out.println(q);
    }

    /**
     * Add a single upvote to the question with the specified ID.
     */
    @PostMapping("upvote")
    @ResponseBody
    public void upvoteQuestion(@RequestBody String id) {
        long longId = Long.parseLong(id);
        Question question = repo.findById(longId);
        question.addUpvote();
        repo.save(question);
    }
}
