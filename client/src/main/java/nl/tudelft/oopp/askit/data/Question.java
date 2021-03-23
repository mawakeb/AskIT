package nl.tudelft.oopp.askit.data;

import java.util.Objects;
import java.util.UUID;

public class Question {

    /**
     * Note: this is a different class than Question.java on the server
     * But because these attributes are a subset of that one,
     * a client Question can be deserialized from a server Question in JSON format
     * Davis: I'll try to keep them synchronized to ease communication between server and client
     */

    private final UUID id;
    private final String content;
    private final UUID roomId;
    private final UUID userId;
    private int upvotes;
    private boolean deleted;
    private boolean edited;
    private boolean answered;
    private int createTime;
    private int answerTime;

    /**
     * Create a new Question instance.
     *
     * @param id      the UUID of the question.
     * @param content the text content of the question.
     * @param roomId  the room the question belongs to.
     * @param userId  user that made this question.
     */
    public Question(UUID id, String content, int upvotes,
                    UUID roomId, UUID userId, int createTime) {
        this.id = id;
        this.content = content;
        this.upvotes = upvotes;
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
    public Question(String content, int upvotes, UUID roomId, UUID userId, int createTime) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.upvotes = upvotes;
        this.roomId = roomId;
        this.userId = userId;
        this.deleted = false;
        this.edited = false;
        this.createTime = createTime;
        this.answerTime = 0;
        this.answered = false;
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

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void addUpvote() {
        upvotes++;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(int answerTime) {
        this.answerTime = answerTime;
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", content='"
                + content + '\'' + ", roomId="
                + roomId + ", userId="
                + userId + ", upvotes="
                + upvotes + ", deleted="
                + deleted + ", edited="
                + edited + ", answered="
                + answered + ", createTime="
                + createTime + ", answerTime="
                + answerTime
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
