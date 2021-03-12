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
    void getRoomid() {
        assertEquals(question.getRoomId(), roomId);
    }

    @Test
    void getUserid() {
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
        question.setDeleted(true);
        assertEquals(question.isDeleted(), true);
    }

    @Test
    void setEdited() {
        question.setEdited(true);
        assertEquals(question.isEdited(), true);
    }
    @Test
    void setUpvotes() {
        question.setUpvotes(4);
        assertEquals(question.getUpvotes(), 4);
    }

    @Test
    void addUpvote() {
        question.addUpvote();
        assertEquals(question.getUpvotes(), 1);
    }

}
