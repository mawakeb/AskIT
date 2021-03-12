package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    private static UUID id;
    private static String content;
    private static Question question;
    private static UUID userId;
    private static UUID roomId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        content = "test";
        question = new Question(id, content, roomId, userId);
    }

    @Test
    void testQuestionConstructorWithoutId() {
        Question question1 = new Question(content, roomId, userId);
        assertNotNull(question1);

        // checks if id is unique
        assertNotEquals(id, question1.getId());
    }

    @Test
    void getId() {
        assertEquals(question.getId(), id);
    }

    @Test
    void getContent() {
        assertEquals(question.getContent(), content);
    }

    @Test
    void getUpvotes() {
        assertEquals(question.getUpvotes(), 0);
    }

    @Test
    void testToString() {
        assertEquals(
                question.toString(),
                "Question{id=" + id + ", content='test', upvotes=0}"
        );
    }

    @Test
    void testEquals() {
        Question question2 = new Question(id, content, roomId, userId);
        assertEquals(question, question2);
    }

    @Test
    void getRoom_id() {
        assertEquals(question.getRoomId(), roomId);
    }

    @Test
    void getUser_id() {
        assertEquals(question.getUserId(), userId);

    }

    @Test
    void isDeleted() {
        assertEquals(question.isDeleted(), false);
    }

    @Test
    void isEdited() {
        assertEquals(question.isEdited(), false);
    }

    @Test
    void setDeleted() {
        Question question1 = new Question(content, roomId, userId);
        question1.setDeleted(true);
        assertEquals(question1.isDeleted(), true);
    }

    @Test
    void setEdited() {
        Question question1 = new Question(content, roomId, userId);
        question1.setEdited(true);
        assertEquals(question1.isEdited(), true);
    }
}
