package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Question;
import nl.tudelft.oopp.askit.entities.Room;
import nl.tudelft.oopp.askit.repositories.QuestionRepository;
import nl.tudelft.oopp.askit.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

class SendingQuestionControllerTest {
    private static final Gson gson = new Gson();
    private SendingQuestionController sc;

    @Mock
    private QuestionRepository repo;

    @Mock
    private RoomRepository roomRepo;

    private Room room;

    private Question question;

    @BeforeEach
    void setUp() {
        UUID roomId = UUID.randomUUID();
        question = new Question("test",roomId,UUID.randomUUID(), "nickname", 5);
        room = new Room(roomId,"name","staf","sd",
                ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
        );
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        when(roomRepo.findByid(any(UUID.class))).thenReturn(room);
        sc = new SendingQuestionController(repo, roomRepo);
    }

    @Test
    void sendQuestion() {
        String parsed = gson.toJson(question);
        sc.sendQuestion(parsed);
        verify(repo, times(1)).save(any(Question.class));
    }

    @Test
    void sendQuestionBannedUser() {

        String parsed = gson.toJson(question);

        // configure bannedUsers, as if user supplying mock question was banned
        Set<UUID> bannedUsers = new HashSet<>();
        bannedUsers.add(question.getUserId());
        UserController.setBannedUsers(bannedUsers);

        assertThrows(ResponseStatusException.class,() -> sc.sendQuestion(parsed));
        verify(repo, times(0)).save(any(Question.class));
    }

    @Test
    void sendQuestionRoomClosed() {
        room.close();
        String parsed = gson.toJson(question);
        assertThrows(ResponseStatusException.class,() -> sc.sendQuestion(parsed));
        verify(repo, times(0)).save(any(Question.class));
    }

    @Test
    void upvoteQuestion() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe, "nickname", 5);
        when(repo.findById(uuid)).thenReturn(question);

        // List that should be received
        List<String> sendList = List.of(
                uuid.toString(),
                dupe.toString()
        );
        String parsedList = gson.toJson(sendList);
        sc.upvoteQuestion(parsedList);
        assertEquals(1, question.getUpvotes());

        // Checks if it got added to the questionUpVotes map
        assertTrue(SendingQuestionController.getQuestionUpVotes().containsKey(uuid));
    }

    @Test
    void upVoteTwice() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe, "nickname", 5);
        when(repo.findById(uuid)).thenReturn(question);

        // List that should be received
        List<String> sendList = List.of(
                uuid.toString(),
                dupe.toString()
        );
        String parsedList = gson.toJson(sendList);
        sc.upvoteQuestion(parsedList);

        // Exception should be thrown
        assertThrows(ResponseStatusException.class, () -> sc.upvoteQuestion(parsedList));
        // Checks if it got saved once
        assertEquals(1, question.getUpvotes());
    }

    @Test
    void answerQuestion() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe, "nickname", 5);
        when(repo.findById(uuid)).thenReturn(question);
        when(roomRepo.findByid(dupe)).thenReturn(room);

        // Simulated request list
        List<String> sendList = List.of(
                uuid.toString(),
                "staf",
                "200"
        );
        String parsedList = gson.toJson(sendList);

        sc.answerQuestion(parsedList);

        assertTrue(question.isAnswered());

        verify(repo, times(1)).save(any(Question.class));
    }

    @Test
    void answerQuestionUnauthorized() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe, "nickname", 5);
        when(repo.findById(uuid)).thenReturn(question);
        when(roomRepo.findByid(dupe)).thenReturn(room);

        // Simulated request list
        List<String> sendList = List.of(
                uuid.toString(),
                "wrong"
        );
        String parsedList = gson.toJson(sendList);

        // Exception should be thrown
        assertThrows(ResponseStatusException.class, () -> sc.answerQuestion(parsedList));


        verify(repo, times(0)).save(any(Question.class));
    }

    @Test
    void getQuestionUpVotes() {
        assertNotNull(SendingQuestionController.getQuestionUpVotes());
    }
}