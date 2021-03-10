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

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        content = "test";
        question = new Question(id, content);
    }

    @Test
    void testQuestionConstructorWithoutId() {
        Question question1 = new Question(content);
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
                "Question{id=" +id+", content='test', upvotes=0}"
        );
    }

    @Test
    void testEquals() {
        Question question2 = new Question(id, content);
        assertEquals(question, question2);
    }
}
