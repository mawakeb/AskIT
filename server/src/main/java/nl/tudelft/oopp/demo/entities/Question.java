package nl.tudelft.oopp.demo.entities;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "upvotes")
    private int upvotes;

    @Column(name = "roomId")
    private UUID roomId;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "edited")
    private boolean edited;

    /**
     * Create a new Question instance.
     *
     * @param id      Unique identifier as to be used in the database.
     * @param content Actual text content of the question.
     */
    public Question(UUID id, String content, UUID roomId, UUID userId) {
        this.id = id;
        this.content = content;
        this.upvotes = 0;
        this.roomId = roomId;
        this.userId = userId;
        this.deleted = false;
        this.edited = false;
    }

    /**
     * Constructor for the Question class that generates an id.
     *
     * @param content the text content of the question.
     */
    public Question(String content, UUID roomId, UUID userId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.upvotes = 0;
        this.roomId = roomId;
        this.userId = userId;
        this.deleted = false;
        this.edited = false;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void addUpvote() {
        upvotes++;
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", content='" + content + '\''
                + ", upvotes=" + upvotes
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return upvotes == question.upvotes
                && deleted == question.deleted
                && edited == question.edited
                && Objects.equals(id, question.id)
                && Objects.equals(content, question.content)
                && Objects.equals(roomId, question.roomId)
                && Objects.equals(userId, question.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, upvotes, roomId, userId, deleted, edited);
    }
}