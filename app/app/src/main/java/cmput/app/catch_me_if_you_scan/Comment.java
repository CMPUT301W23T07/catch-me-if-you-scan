package cmput.app.catch_me_if_you_scan;

import com.google.firebase.Timestamp;

public class Comment {
    private String commentMessage;
    private Timestamp commentDate;
//    private Monster commentedMonster;
    private String monsterHashCode;
//    private User commentingUser;
    private String username;

    private String dbId;

    public Comment(String commentMessage, Timestamp commentDate, String monsterHashCode, String username) {
        this.commentMessage = commentMessage;
        this.commentDate = commentDate;
        this.monsterHashCode = monsterHashCode;
        this.username = username;
    }

    // TO DO
    // an empty string should not be added to the database.
    // 1. Test for "", "\n", " ",

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }

    public String getMonsterHashCode() {
        return monsterHashCode;
    }

    public void setMonsterHashCode(String monsterHashCode) {
        this.monsterHashCode = monsterHashCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDbId(String dbID) { this.dbId = dbID; }

    public String getDbId() { return this.dbId; }
}
