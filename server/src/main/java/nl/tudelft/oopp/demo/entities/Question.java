package nl.tudelft.oopp.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="questions")
public class Question {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "upvotes")
    private int upvotes;

    /**
     * Create a new Question instance.
     *
     * @param id Unique identifier as to be used in the database.
     * @param content Actual text content of the question.
     */
    public Question(long id, String content) {
        this.id = id;
        this.content = content;
        this.upvotes = 0;
    }

    public Question() {
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getUpvotes(){
        return upvotes;
    }

    public void addUpvote() {
        upvotes++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                Objects.equals(content, question.content);
    }
}
