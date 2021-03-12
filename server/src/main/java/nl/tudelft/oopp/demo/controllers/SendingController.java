package nl.tudelft.oopp.demo.controllers;

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

    private QuestionRepository repo;
    private RoomRepository roomRepo;

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

    //TODO: the client should send roomId and userId, using temp for now
    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        UUID tempUserId = UUID.randomUUID();
        UUID tempRoomId = UUID.randomUUID();
        Question question = new Question(q, tempRoomId, tempUserId);

        // TODO: extract room ID from question instead of hardcoding
        Room room = roomRepo.findByid(tempRoomId);

        // TODO: the room wont be found, so adding a null check for now
        if(room == null) {
            System.out.println("TODO: the room wont be found, so adding a null check for now");
            return;
        }
        if (room.isOpen()) {
            repo.save(question);
            System.out.println(q);
        } else {
            System.out.println("Question rejected");
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
}
