package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest

class QuestionRepositoryTest {
    // further info: https://www.baeldung.com/spring-boot-testing

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void getAllStrings() {
        // Sets up a dummy DB
        String content = "test";
        Question question = new Question(content, UUID.randomUUID(), UUID.randomUUID());
        entityManager.persist(question);
        entityManager.flush();

        List<String> contents = questionRepository.getAllStrings();

        assertEquals(contents.get(0), content);
    }

    @Test
    void findById() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question(id, "content", UUID.randomUUID(), UUID.randomUUID());
        entityManager.persist(question);
        entityManager.flush();

        Question found = questionRepository.findById(id);

        assertEquals(found, question);
    }

    @Test
    void findAll() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question("content", id, UUID.randomUUID());
        entityManager.persist(question);
        entityManager.flush();

        List<Question> questions = questionRepository.findAll();

        assertEquals(questions.get(0), question);
    }
}