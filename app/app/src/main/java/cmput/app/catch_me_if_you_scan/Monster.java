package cmput.app.catch_me_if_you_scan;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.w3c.dom.Comment;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;

public class Monster {

    private String name;
    private int score;
    private String[] visual;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private HashCode hash;
    private String hashToBinary;
    private String hashHex;
    private int hashInt;
    private Hashtable<Integer,String[]> bitWords = new Hashtable<Integer, String[]>();



    Monster(String code){
        this.hash = Hashing.sha256().hashString(code, StandardCharsets.UTF_8);
        this.hashHex = hash.toString();
        this.hashInt = hash.asInt();
        this.hashToBinary = Integer.toBinaryString(this.hashInt);
        this.initializedHashTables();
    }

    public int getScore(){
        return this.score;
    }

    public String getName(){
        for(int i = 0; i<6; i++){
            int currentBit = (int)hashToBinary.charAt(i) - 48;
            String bitWord = bitWords.get(i)[currentBit];
            if(i==0){
                name = bitWord + " ";
            }
            else name = name + bitWord;
        }
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
    public void initializedHashTables(){
        bitWords.put(0, new String[]{"cool", "hot"});
        bitWords.put(1, new String[]{"Fro", "Glo"});
        bitWords.put(2, new String[]{"Mo", "Lo"});
        bitWords.put(3, new String[]{"Mega", "Ultra"});
        bitWords.put(4, new String[]{"Spectral", "Sonic"});
        bitWords.put(5, new String[]{"Crab", "Shark"});
    }
}

