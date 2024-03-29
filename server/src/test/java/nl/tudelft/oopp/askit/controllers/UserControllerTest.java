package nl.tudelft.oopp.askit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.time.ZonedDateTime;
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

class UserControllerTest {
    private static final Gson gson = new Gson();
    private SendingQuestionController sc;
    private UserController uc;

    @Mock
    private QuestionRepository repo;

    @Mock
    private RoomRepository roomRepo;

    private Question question;
    private UUID roomId;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        question = new Question("test", roomId, UUID.randomUUID(), "nickname", 5);
        Room room = new Room(roomId, "name", "staff", "sd", ZonedDateTime.now());
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        when(roomRepo.findByid(any(UUID.class))).thenReturn(room);
        sc = new SendingQuestionController(repo, roomRepo);
        uc = new UserController(roomRepo);
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
    void banUser() {
        UUID uuidToBan = UUID.randomUUID();
        assertFalse(UserController.getBannedUsers().contains(uuidToBan));
        // Simulated request list, to get length
        List<String> sendList = List.of(
                uuidToBan.toString(),
                "staff",
                roomId.toString()
        );
        String parsedList = gson.toJson(sendList);
        // bodyPublisher does not expose the contents d

        uc.banUser(parsedList);
        assertTrue(UserController.getBannedUsers().contains(uuidToBan));
    }

    @Test
    void banUserUnauthorized() {
        UUID uuidToBan = UUID.randomUUID();
        assertFalse(UserController.getBannedUsers().contains(uuidToBan));
        // Simulated request list, to get length
        List<String> sendList = List.of(
                uuidToBan.toString(),
                "wrong",
                roomId.toString()
        );
        String parsedList = gson.toJson(sendList);

        // Exception should be thrown
        assertThrows(ResponseStatusException.class, () -> uc.banUser(parsedList));

    }

    @Test
    void banUserRoomNotFound() {
        when(roomRepo.findByid(any(UUID.class))).thenReturn(null);
        UUID uuidToBan = UUID.randomUUID();
        assertFalse(UserController.getBannedUsers().contains(uuidToBan));
        // Simulated request list, to get length
        List<String> sendList = List.of(
                uuidToBan.toString(),
                "wrong",
                roomId.toString()
        );
        String parsedList = gson.toJson(sendList);

        // Exception should be thrown
        assertThrows(ResponseStatusException.class, () -> uc.banUser(parsedList));

    }

    @Test
    void setBannedUsers() {
        HashSet<UUID> hash = new HashSet<>();
        hash.add(UUID.randomUUID());
        UserController.setBannedUsers(hash);
        assertEquals(hash, UserController.getBannedUsers());
    }

    @Test
    void getBannedUsers() {
        assertNotNull(UserController.getBannedUsers());
    }
}