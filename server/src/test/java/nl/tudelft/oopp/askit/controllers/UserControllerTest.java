package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

class UserControllerTest {
    private static final Gson gson = new Gson();
    private SendingQuestionController sc;
    private UserController uc;

    @Mock
    private QuestionRepository repo;

    @Mock
    private RoomRepository roomRepo;

    private Room room;

    private Question question;

    @BeforeEach
    void setUp() {
        UUID roomId = UUID.randomUUID();
        question = new Question("test", roomId, UUID.randomUUID(), 5);
        room = new Room(roomId, "name", "staf", "sd", ZonedDateTime.now());
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        when(roomRepo.findByid(any(UUID.class))).thenReturn(room);
        sc = new SendingQuestionController(repo, roomRepo);
        uc = new UserController(repo, roomRepo);
    }

    @Test
    void sendQuestionBannedUser() {

        String parsed = gson.toJson(question);

        // configure bannedUsers, as if user supplying mock question was banned
        Set<UUID> bannedUsers = new HashSet<>();
        bannedUsers.add(question.getUserId());
        UserController.setBannedUsers(bannedUsers);

        assertEquals("You have been banned from sending questions", sc.sendQuestion(parsed));
        verify(repo, times(0)).save(any(Question.class));
    }

    @Test
    void banUser() {
        UUID uuidToBan = UUID.randomUUID();
        assertFalse(UserController.getBannedUsers().contains(uuidToBan));
        uc.banUser(uuidToBan.toString());
        assertTrue(UserController.getBannedUsers().contains(uuidToBan));
    }

}