package nl.tudelft.oopp.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="questions")
public class Question {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "content")
    private String content;

    /**
     * Create a new Question instance.
     *
     * @param id Unique identifier as to be used in the database.
     * @param content Actual text content of the question.
     */
    public Question(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
