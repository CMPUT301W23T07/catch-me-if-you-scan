package cmput.app.catch_me_if_you_scan;

import com.google.firebase.firestore.auth.User;

public class Comment {

    private User user;
    private String content;

    public User getUser(){
        return this.user;
    }

    public String getContent(){
        return this.content;
    }
}
