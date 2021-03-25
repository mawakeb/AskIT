package nl.tudelft.oopp.askit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Question;
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
        Question question = new Question(content, UUID.randomUUID(), UUID.randomUUID(), 5);
        entityManager.persist(question);
        entityManager.flush();

        List<String> contents = questionRepository.getAllStrings();

        assertEquals(contents.get(0), content);
    }

    @Test
    void findById() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question(id, "content", UUID.randomUUID(), UUID.randomUUID(), 5);
        entityManager.persist(question);
        entityManager.flush();

        Question found = questionRepository.findById(id);

        assertEquals(found, question);
    }

    @Test
    void getAllRoomQuestions() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question(UUID.randomUUID(), "content", id, UUID.randomUUID(), 5);
        Question question1 = new Question(UUID.randomUUID(), "content1", id, UUID.randomUUID(), 5);

        UUID id2 = UUID.randomUUID();
        Question different = new Question(UUID.randomUUID(), "content2", id2, UUID.randomUUID(), 5);
        entityManager.persist(question);
        entityManager.persist(question1);
        entityManager.persist(different);
        entityManager.flush();

        List<Question> found = questionRepository.getAllRoomQuestions(id);
        List<Question> found2 = questionRepository.getAllRoomQuestions(id2);

        assertEquals(found, List.of(question,question1));
        assertEquals(found2, List.of(different));
    }

    @Test
    void getAllAnsweredQuestions() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question(UUID.randomUUID(), "content", id, UUID.randomUUID(), 5);
        Question question1 = new Question(UUID.randomUUID(), "content1", id, UUID.randomUUID(), 5);
        question.setAnswered(true);
        entityManager.persist(question);
        entityManager.persist(question1);

        UUID id2 = UUID.randomUUID();
        Question different = new Question(UUID.randomUUID(), "content2", id2, UUID.randomUUID(), 5);
        different.setAnswered(true);
        entityManager.persist(different);
        entityManager.flush();

        List<Question> found = questionRepository.getAllAnsweredQuestions(id);
        List<Question> found2 = questionRepository.getAllAnsweredQuestions(id2);

        assertEquals(found, List.of(question));
        assertEquals(found2, List.of(different));
    }

    @Test
    void findAll() {
        // Sets up a dummy DB
        UUID id = UUID.randomUUID();
        Question question = new Question("content", id, UUID.randomUUID(), 5);
        entityManager.persist(question);
        entityManager.flush();

        List<Question> questions = questionRepository.findAll();

        assertEquals(questions.get(0), question);
    }

    @Test
    void getLastQuestionTimeOfUser() {
        UUID uid1 = UUID.randomUUID();
        Question q1 = new Question("content", UUID.randomUUID(), uid1, 5);
        Question q2 = new Question("content", UUID.randomUUID(), uid1, 6);
        Question q3 = new Question("content", UUID.randomUUID(), UUID.randomUUID(), 7);
        entityManager.persist(q1);
        entityManager.persist(q2);
        entityManager.persist(q3);
        entityManager.flush();

        Integer result = questionRepository.getLastQuestionTimeOfUser(uid1);
        assertEquals(result, q2.getCreateTime());
    }
}