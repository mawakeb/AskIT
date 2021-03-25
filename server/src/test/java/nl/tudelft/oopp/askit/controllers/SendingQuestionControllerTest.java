package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.time.ZonedDateTime;
import java.util.HashSet;
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
        question = new Question("test",roomId,UUID.randomUUID(), 5);
        room = new Room(roomId,"name","staf","sd", ZonedDateTime.now());
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        when(roomRepo.findByid(any(UUID.class))).thenReturn(room);
        sc = new SendingQuestionController(repo, roomRepo);
    }

    @Test
    void sendQuestion() {
        String parsed = gson.toJson(question);
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
        Question question = new Question(uuid, "Unit test question", dupe, dupe, 5);
        when(repo.findById(uuid)).thenReturn(question);
        sc.upvoteQuestion(uuid.toString());
        assertEquals(1, question.getUpvotes());
    }

    @Test
    void answerQuestion() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe, 5);
        when(repo.findById(uuid)).thenReturn(question);
        sc.answerQuestion(uuid.toString());
        assertTrue(question.isAnswered());
    }

}