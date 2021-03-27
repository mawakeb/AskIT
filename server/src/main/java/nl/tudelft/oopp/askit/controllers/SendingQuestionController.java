package nl.tudelft.oopp.askit.controllers;

import com.google.gson.Gson;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Question;
import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class SendingQuestionController {
    private static final Gson gson = new Gson();
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;

    /**
     * Constructor for SendingController, autowired for JPA repositories.
     * @param repo repository with all questions
     * @param roomRepo repository with all rooms
     */
    @Autowired
    public SendingQuestionController(QuestionRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    /**
     * Receive question sent by a student and store it in the repository.
     *
     * @param q String containing the question object
     * @return "SUCCESS", or a message describing what failed
     */

    @PostMapping("question") // for /send/question
    @ResponseBody
    public String sendQuestion(@RequestBody String q) {
        Question userQuestion = gson.fromJson(q, Question.class);
        // Set question up-votes to 0, to avoid hackers.
        userQuestion.setUpvotes(0);

        if (UserController.getBannedUsers().contains(userQuestion.getUserId())) {
            System.out.println("Question rejected: user banned");
            return "You have been banned from sending questions";
        }

        Room room = roomRepo.findByid(userQuestion.getRoomId());
        if (room == null) {
            System.out.println("Question doesn't belong to a room");
            return "The room can't be found on the server";
        }
        if (room.isOpen()) {
            userQuestion.setCreateTime(TimeControl.getMilisecondsPassed(room.getOpenTime()));
            repo.save(userQuestion);
            System.out.println(q);
        } else {
            System.out.println("Question rejected: room closed");
            return "The room has been closed by a staff member";
        }
        return "SUCCESS";
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

    /**
     * Cancel a single upvote to the question with the specified ID.
     */
    @PostMapping("cancelUpvote")
    @ResponseBody
    public void cancelUpvote(@RequestBody String id) {
        UUID uuid = UUID.fromString(id);
        Question question = repo.findById(uuid);
        question.cancelUpvote();
        repo.save(question);
    }

    /**
     * Mark the question with the specified ID as answered.
     */
    @PostMapping("answer")
    @ResponseBody
    public void answerQuestion(@RequestBody String id) {
        UUID uuid = UUID.fromString(id);
        Question question = repo.findById(uuid);
        question.setAnswered(true);
        repo.save(question);
    }
}
