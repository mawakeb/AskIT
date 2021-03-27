package nl.tudelft.oopp.askit.entities;

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
    private UUID id = null;
    @Column(name = "content")
    private String content = "";
    @Column(name = "roomId")
    private UUID roomId = null;
    @Column(name = "userId")
    private UUID userId = null;
    @Column(name = "upvotes")
    private int upvotes;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "edited")
    private boolean edited;
    @Column(name = "createTime")
    private int createTime;
    @Column(name = "answered")
    private boolean answered;
    @Column(name = "answerTime")
    private int answerTime;

    /**
     * Create a new Question instance.
     *
     * @param id      the UUID of the question.
     * @param content the text content of the question.
     * @param roomId  the room the question belongs to.
     * @param userId  user that made this question.
     */
    public Question(UUID id, String content, UUID roomId, UUID userId, int createTime) {
        this.id = id;
        this.content = content;
        this.upvotes = 0;
        this.roomId = roomId;
        this.userId = userId;
        this.deleted = false;
        this.edited = false;
        this.createTime = createTime;
        this.answerTime = 0;
        this.answered = false;
    }

    /**
     * Constructor for the Question class that generates an id.
     *
     * @param content the text content of the question.
     * @param roomId  the room the question belongs to.
     * @param userId  user that made this question.
     */
    public Question(String content, UUID roomId, UUID userId, int createTime) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.upvotes = 0;
        this.roomId = roomId;
        this.userId = userId;
        this.deleted = false;
        this.edited = false;
        this.createTime = createTime;
        this.answerTime = 0;
        this.answered = false;
    }

    public Question() {

    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void addUpvote() {
        upvotes++;
    }

    public void cancelUpvote() {
        upvotes--;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = Question.this.createTime;
    }

    public int getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(int answerTime) {
        this.answerTime = answerTime;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAnswered() {
        return this.answered;
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