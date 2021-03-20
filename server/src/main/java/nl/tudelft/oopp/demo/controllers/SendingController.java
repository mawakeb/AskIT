package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class SendingController {
    private static final Gson gson = new Gson();
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;

    private Set<UUID> bannedUsers;

    @Autowired
    public SendingController(QuestionRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    /**
     * Receive question sent by a student and store it in the repository.
     *
     * @param q String containing only the question text content.
     */
    
    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        Question userQuestion = gson.fromJson(q, Question.class);
        // Set question up-votes to 0, to avoid hackers
        userQuestion.setUpvotes(0);

        if (bannedUsers.contains(userQuestion.getUserId())) {
            System.out.println("Question rejected: user banned");
            return;
        }

        Room room = roomRepo.findByid(userQuestion.getRoomId());
        if (room == null) {
            System.out.println("Question doesn't belong to a room");
            return;
        }
        if (room.isOpen()) {
            repo.save(userQuestion);
            System.out.println(q);
        } else {
            System.out.println("Question rejected: room closed");
        }
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
     * Prevent a user from sending new questions, using their id.
     */
    @PostMapping("")
    @ResponseBody
    public void banUser(@RequestBody String id) {
        UUID uuid = UUID.fromString(id);
        bannedUsers.add(uuid);
    }
}
