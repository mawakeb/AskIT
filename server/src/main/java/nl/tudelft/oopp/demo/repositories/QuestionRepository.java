package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT content FROM Question")
    List<String> getAllStrings();

    Question findById(long id);

    List<Question> findAll();
}
