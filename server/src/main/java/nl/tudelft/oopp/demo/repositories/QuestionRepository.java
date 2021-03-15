package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT content FROM Question")
    List<String> getAllStrings();

    @Query("SELECT q FROM Question q WHERE q.roomId = ?1")
    List<Question> getAllRoomQuestions(UUID roomId);

    Question findById(UUID id);

    List<Question> findAll();
}
