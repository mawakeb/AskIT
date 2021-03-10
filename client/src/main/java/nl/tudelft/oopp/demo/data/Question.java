package nl.tudelft.oopp.demo.data;

import java.util.Objects;

public class Question {

    /**
     * Note: this is a different class than Question.java on the server
     * But because these attributes are a subset of that one,
     * a client Question can be deserialized from a server Question in JSON format
     */
    private long id;
    private String content;
    private int upvotes;

    public Question(long id, String content, int upvotes) {
        this.id = id;
        this.content = content;
        this.upvotes = upvotes;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getUpvotes() {
        return upvotes;
    }

    @Override
    public String toString(){
        return getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                upvotes == question.upvotes &&
                Objects.equals(content, question.content);
    }
}
