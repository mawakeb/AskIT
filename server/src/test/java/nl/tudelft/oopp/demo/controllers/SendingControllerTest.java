package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SendingControllerTest {

    private SendingController sc;

    @Mock
    private QuestionRepository repo;

    @Mock
    private RoomRepository roomRepo;

    @Mock
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // necessary when using @Mock's
        when(roomRepo.findByid(any(UUID.class))).thenReturn(room);
        sc = new SendingController(repo, roomRepo);
    }

    @Test
    void sendQuestion() {
        when(room.isOpen()).thenReturn(true);
        sc.sendQuestion("SendingControllerTest question");
        verify(repo, times(1)).save(any(Question.class));
    }

    @Test
    void sendQuestionRoomClosed() {
        when(room.isOpen()).thenReturn(false);
        sc.sendQuestion("SendingControllerTest question");
        verify(repo, times(0)).save(any(Question.class));
    }

    @Test
    void upvoteQuestion() {
        UUID uuid = UUID.randomUUID();
        UUID dupe = UUID.randomUUID();
        Question question = new Question(uuid, "Unit test question", dupe, dupe);
        when(repo.findById(uuid)).thenReturn(question);
        sc.upvoteQuestion(uuid.toString());
        assertEquals(1, question.getUpvotes());
    }
}