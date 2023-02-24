package cmput.app.catch_me_if_you_scan;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Monster {

    private String name;
    private int score;
    private String[] visual;
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    Monster(String code){

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
