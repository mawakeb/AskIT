package nl.tudelft.oopp.askit.data;

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
    private static int upvotes;
    private static Question question;
    private static UUID userId;
    private static UUID roomId;
    private static String username;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        content = "test";
        upvotes = 5;
        userId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        username = "nickname";
        question = new Question(id, content, upvotes, roomId, userId, username, 5);
    }

    @Test
    void testQuestionConstructorWithoutId() {
        Question question1 = new Question(content, upvotes, roomId, userId, username, 5);
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
        assertEquals(question.getUpvotes(), upvotes);
    }

    @Test
    void testToString() {
        assertEquals(question.getContent(), content);
    }

    @Test
    void testEquals() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        assertEquals(question, question2);
    }

    @Test
    void testEquals1() {
        assertNotEquals(question, new Object());
    }

    @Test
    void testEquals2() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        assertEquals(question, question);
    }

    @Test
    void setUpvotes() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        question2.setUpvotes(10);
        assertEquals(question2.getUpvotes(), 10);

    }

    @Test
    void setAnswered() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        question2.setAnswered(true);
        assertTrue(question2.isAnswered());

    }

    @Test
    void getRoomId() {
        assertEquals(question.getRoomId(), roomId);
    }

    @Test
    void getUserId() {
        assertEquals(question.getUserId(), userId);
    }

    @Test
    void getUsername() {
        assertEquals(question.getUsername(), username);
    }

    @Test
    void isDeleted() {
        assertFalse(question.isDeleted());
    }

    @Test
    void isAnswered() {
        assertFalse(question.isAnswered());
    }

    @Test
    void setDeleted() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        question2.setDeleted(true);
        assertTrue(question2.isDeleted());
    }

    @Test
    void isEdited() {
        assertFalse(question.isEdited());
    }

    @Test
    void setEdited() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username,5);
        question2.setEdited(true);
        assertTrue(question2.isEdited());
    }

    @Test
    void addUpvote() {
        Question question2 = new Question(id, content, upvotes, roomId, userId, username, 5);
        question2.addUpvote();
        assertEquals(question2.getUpvotes(), 6);
    }

    @Test
    void getCreateTime() {
        assertEquals(question.getCreateTime(), 5);
    }

    @Test
    void getAndSetAnswerTime() {
        question.setAnswerTime(5);
        assertEquals(5, question.getAnswerTime());
    }

    @Test
    void testToString1() {
        assertEquals("Question{id=" + id + ", "
                + "content='test', roomId=" + roomId + ", "
                + "userId=" + userId + ", username=nickname, upvotes=5, "
                + "deleted=false, edited=false, answered=false, "
                + "createTime=5, answerTime=0}", question.toString());
    }
}
