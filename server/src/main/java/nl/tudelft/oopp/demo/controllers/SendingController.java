package nl.tudelft.oopp.demo.controllers;

import java.util.UUID;

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

    //TODO: the client should send roomId and userId, using temp for now
    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        UUID tempUserId = UUID.randomUUID();
        UUID tempRoomId = UUID.randomUUID();
        Question question = new Question(q,tempRoomId,tempUserId);
        repo.save(question);
        System.out.println(q);
    }

    /**
     * Add a single upvote to the question with the specified ID.
     */
    @PostMapping("upvote")
    @ResponseBody
    public void upvoteQuestion(@RequestBody String id) {
        UUID uuid = UUID.fromString(id);
        Question question = repo.findById(uuid);
        question.addUpvote();
        repo.save(question);
    }
}
