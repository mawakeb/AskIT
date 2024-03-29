package nl.tudelft.oopp.askit.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Question;
import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("send")
public class SendingQuestionController {
    private static final Gson gson = new Gson();
    private final QuestionRepository repo;
    private final RoomRepository roomRepo;
    private static final HashMap<UUID, ArrayList<UUID>> questionUpVotes = new HashMap<>();

    /**
     * Constructor for SendingController, autowired for JPA repositories.
     *
     * @param repo     repository with all questions
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
     */

    @PostMapping("question") // for /send/question
    @ResponseBody
    public void sendQuestion(@RequestBody String q) {
        Question userQuestion = gson.fromJson(q, Question.class);
        // Set question up-votes to 0, to avoid hackers.
        userQuestion.setUpvotes(0);

        if (UserController.getBannedUsers().contains(userQuestion.getUserId())) {
            System.out.println("Question rejected: user banned");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "BANNED");
        }

        Room room = roomRepo.findByid(userQuestion.getRoomId());
        if (room == null) {
            System.out.println("Question doesn't belong to a room");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ROOM_NOT_FOUND");
        }

        // Checks if user didn't bypass open time
        if (room.getOpenTime().isAfter(ZonedDateTime.now())) {
            System.out.println("Room is not open yet");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "ROOM_NOT_OPEN");
        }

        if (room.isOpen()) {
            userQuestion.setCreateTime(TimeControl.getMilisecondsPassed(room.getOpenTime()));
            repo.save(userQuestion);
            System.out.println(q);
        } else {
            System.out.println("Question rejected: room closed");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "ROOM_CLOSED");
        }
    }

    /**
     * Add a single upvote to the question with the specified ID.
     */
    @PostMapping("upvote")
    @ResponseBody
    public void upvoteQuestion(@RequestBody String ids) {
        List<String> list = gson.fromJson(ids, new TypeToken<List<String>>() {
        }.getType());
        UUID uuid = UUID.fromString(list.get(0));
        UUID userId = UUID.fromString(list.get(1));

        // Checks if the user hasn't upVoted before the same question
        if (questionUpVotes.containsKey(uuid)) {
            List<UUID> prev = questionUpVotes.get(uuid);
            if (prev == null) {
                prev = new ArrayList<>();
            }
            if (prev.contains(userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "CANT_UPVOTE_TWICE");
            } else {
                prev.add(userId);
            }

        } else {
            ArrayList<UUID> arrayList = new ArrayList<>();
            arrayList.add(userId);
            questionUpVotes.put(uuid, arrayList);
        }

        Question question = repo.findById(uuid);
        question.addUpvote();
        repo.save(question);
    }

    /**
     * Cancel a single upvote to the question with the specified ID.
     */
    @PostMapping("cancelUpvote")
    @ResponseBody
    public void cancelUpvote(@RequestBody String ids) {
        List<String> list = gson.fromJson(ids, new TypeToken<List<String>>() {
        }.getType());
        UUID uuid = UUID.fromString(list.get(0));
        UUID userId = UUID.fromString(list.get(1));

        // Checks if the user has upVoted the question before
        if (questionUpVotes.containsKey(uuid)) {
            ArrayList<UUID> prev = questionUpVotes.get(uuid);
            if (prev == null || !prev.contains(userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "CANT_CANCEL_WITHOUT_UP_VOTING");
            } else {
                prev.remove(userId);
            }

        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "CANT_CANCEL_WITHOUT_UP_VOTING");
        }

        Question question = repo.findById(uuid);
        question.cancelUpvote();
        repo.save(question);
    }

    /**
     * Mark the question with the specified ID as answered.
     */
    @PostMapping("answer")
    @ResponseBody
    public void answerQuestion(@RequestBody String ids) {
        List<String> list = gson.fromJson(ids, new TypeToken<List<String>>() {
        }.getType());
        UUID questionId = UUID.fromString(list.get(0));
        String roleId = list.get(1);
        Question question = repo.findById(questionId);
        Room room = roomRepo.findByid(question.getRoomId());
        if (room.getStaff().equals(roleId)) {
            question.setAnswered(true);
            question.setAnswerTime(Integer.parseInt(list.get(2)));
            repo.save(question);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not a moderator");
        }
        
    }


    /**
     * Get how long a user has to wait before asking a new question (regarding slow mode).
     *
     * @param uid user ID
     * @param rid ID of the room the user belongs to
     * @return the amount of millis left wait, can be negative
     */
    @GetMapping("timeleft")
    @ResponseBody
    public int getTimeLeft(@RequestParam String uid, @RequestParam String rid) {

        // find when this user has last sent a question, relative to room creation
        UUID userId = UUID.fromString(uid);
        Integer lastQuestionTime = repo.getLastQuestionTimeOfUser(userId);
        if (lastQuestionTime == null) {
            // if a user has not sent questions so far, they should not have to wait
            // negative wait time means that they can send a question
            return -1;
        }

        // use the room creation time to get the actual time passed
        UUID roomId = UUID.fromString(rid);
        Room room = roomRepo.findByid(roomId);
        if (room == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ROOM_NOT_FOUND");
        }
        ZonedDateTime roomCreation = room.getOpenTime();
        int currentRoomTime = TimeControl.getMilisecondsPassed(roomCreation);
        int timePassed = currentRoomTime - lastQuestionTime;

        return room.getSlowModeSeconds() * 1000 - timePassed;
    }

    public static HashMap<UUID, ArrayList<UUID>> getQuestionUpVotes() {
        return questionUpVotes;
    }
}
