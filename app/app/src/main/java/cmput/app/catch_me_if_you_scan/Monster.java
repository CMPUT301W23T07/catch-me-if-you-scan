package cmput.app.catch_me_if_you_scan;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.w3c.dom.Comment;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Monster {

    private String name;
    private int score;
    private String[] visual;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private HashCode hash;

    Monster(String code){
        hash = Hashing.sha256().hashString(code, StandardCharsets.UTF_8);
        String hashHex = hash.toString();
        int hashInt = hash.asInt();
    }

    public int getScore(){
        return this.score;
    }

    public String getName(){
        return this.name;
    }

    public String[] getVisual(){
        return this.visual;
    }

    public ArrayList<Comment> getComments(){
        return this.comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
}
