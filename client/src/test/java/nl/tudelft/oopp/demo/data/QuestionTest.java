package nl.tudelft.oopp.demo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    private static UUID id;
    private static String content;
    private static int upvotes;
    private static Question question;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        content = "test";
        upvotes = 5;
        question = new Question(id, content, upvotes);
    }

    @Test
    void testQuestionConstructorWithoutId() {
        Question question1 = new Question(content, upvotes);
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
        assertEquals(question.toString(), content);
    }

    @Test
    void testEquals() {
        Question question2 = new Question(id, content, upvotes);
        assertEquals(question, question2);
    }
}
