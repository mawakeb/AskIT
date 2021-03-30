package nl.tudelft.oopp.askit.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    private static UUID id;
    private static String content;
    private static Question question;
    private static UUID userId;
    private static UUID roomId;
    private static String username;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        content = "test";
        username = "nickname";
        question = new Question(id, content, roomId, userId, username, 5);
    }

    @Test
    void testQuestionConstructorWithoutId() {
        Question question1 = new Question(content, roomId, userId, username, 5);
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
    void isAnswered() {
        assertFalse(question.isAnswered());
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
        Question question2 = new Question(id, content, roomId, userId, username, 5);
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
    void getUserName() {
        assertEquals(question.getUsername(), username);

    }

    @Test
    void isDeleted() {
        assertFalse(question.isDeleted());
    }

    @Test
    void isEdited() {
        assertFalse(question.isEdited());
    }

    @Test
    void setDeleted() {
        question.setDeleted(true);
        assertTrue(question.isDeleted());
    }

    @Test
    void setEdited() {
        question.setEdited(true);
        assertTrue(question.isEdited());
    }

    @Test
    void setUpvotes() {
        question.setUpvotes(4);
        assertEquals(question.getUpvotes(), 4);
    }

    @Test
    void setAnswered() {
        question.setAnswered(true);
        assertTrue(question.isAnswered());
    }

    @Test
    void addUpvote() {
        question.addUpvote();
        assertEquals(question.getUpvotes(), 1);
    }

    @Test
    void getAndSetCreateTime() {
        question.setCreateTime(5);
        assertEquals(question.getCreateTime(), 5);
    }

    @Test
    void getAndSetAnswerTime() {
        question.setAnswerTime(5);
        assertEquals(5, question.getAnswerTime());
    }

}
