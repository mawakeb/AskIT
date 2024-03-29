package nl.tudelft.oopp.askit.repositories;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT content FROM Question")
    List<String> getAllStrings();

    @Query("SELECT q FROM Question q "
            + "WHERE q.roomId = ?1 AND q.answered = false "
            + "ORDER BY q.createTime")
    List<Question> getAllRoomQuestions(UUID roomId);

    @Query("SELECT q FROM Question q "
            + "WHERE q.roomId = ?1 AND q.answered = true "
            + "ORDER BY q.answerTime")
    List<Question> getAllAnsweredQuestions(UUID roomId);

    Question findById(UUID id);

    List<Question> findAll();

    @Query("SELECT MAX(createTime) FROM Question WHERE userId = ?1")
    Integer getLastQuestionTimeOfUser(UUID userId);
}
