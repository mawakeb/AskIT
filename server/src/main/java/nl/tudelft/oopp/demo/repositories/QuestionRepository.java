package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Query("SELECT content FROM Question")
    Collection<Question> findAll(String query);
}
