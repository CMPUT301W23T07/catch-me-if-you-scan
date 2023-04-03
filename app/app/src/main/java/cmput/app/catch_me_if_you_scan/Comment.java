package cmput.app.catch_me_if_you_scan;

import com.google.firebase.Timestamp;

/**
 * This class represents a comment object which can be added to a monster by a user
 */
public class Comment {
    private String commentMessage;
    private Timestamp commentDate;
    private String monsterHashCode;
    private String username;
    private String dbId;

    /**
     * This is the constructor for the comment
     * @param commentMessage
     * @param commentDate
     * @param monsterHashCode
     * @param username
     */
    public Comment(String commentMessage, Timestamp commentDate, String monsterHashCode, String username) {
        this.commentMessage = commentMessage;
        this.commentDate = commentDate;
        this.monsterHashCode = monsterHashCode;
        this.username = username;
    }

    /**
     * Returns the comments message
     * @return
     */
    public String getCommentMessage() {
        return commentMessage;
    }

    /**
     * Sets the comments message
     * @param commentMessage
     */
    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    /**
     * Gets the comments data
     * @return
     */
    public Timestamp getCommentDate() {
        return commentDate;
    }

    /**
     * Returns the monsters hashcode
     * @return
     */
    public String getMonsterHashCode() {
        return monsterHashCode;
    }

    /**
     * Sets the monsters hashcode for this comment
     * @param monsterHashCode
     */
    public void setMonsterHashCode(String monsterHashCode) {
        this.monsterHashCode = monsterHashCode;
    }

    /**
     * Gets the username for the user who wrote the comment
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user who wrote the comment
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the dbID, used for database purposes
     * @param dbID
     */
    public void setDbId(String dbID) { this.dbId = dbID; }

    /**
     * Gets the dbID, used for database purposes
     * @return
     */
    public String getDbId() { return this.dbId; }
}
