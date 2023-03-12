package cmput.app.catch_me_if_you_scan;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.w3c.dom.Comment;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;

public class Monster {
    private String hashedCode;
    private String name;
    private int score;
    private int scoreRank;
    private String[] visual;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private HashCode hash;
    private String hashBinary;
    private String hashHex;
    private int hashInt;
    private Hashtable<Integer,String[]> bitWords = new Hashtable<Integer, String[]>();
    private String longitude;
    private String latitude;


    Monster(String code, String latitude, String longitude){
        this.hashedCode = code;
        this.hash = Hashing.sha256().hashString(code, StandardCharsets.UTF_8);
        this.hashHex = hash.toString();
        this.hashInt = hash.asInt();
        this.hashBinary = Integer.toBinaryString(this.hashInt);
        this.initializedHashTables();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Calculates the monster's score based on
     * the system the client asked for
     * @return The monster's score
     */
    public int getScore(){
        if (this.score !=0){
            return this.score;
        }
        // Iterate over the characters in the hash string
        for(int i=0; i<this.hashHex.length()-1; i++){
            char currentChar = this.hashHex.charAt(i);
            // Initialize the repeat counter and index
            int repeatCounter = 1;
            int j = i;
            // Count the number of consecutive occurrences of the current character
            while(j<this.hashHex.length()-1 && currentChar == this.hashHex.charAt(j+1)){
                repeatCounter++;
                j++;
            }
            // Calculate the score based on the character and the number of consecutive occurrences
            if(currentChar == '0'){
                // For '0', add 20^(repeatCounter-1) to the score
                this.score+=Math.pow(20, repeatCounter-1);
            }
            else if(repeatCounter>1){
                // For other characters with more than one consecutive occurrence,
                // calculate the score based on their value and the number of occurrences
                if (currentChar == 'a'){
                    this.score+=Math.pow(10, repeatCounter-1);
                }
                else if (currentChar == 'b'){
                    this.score+=Math.pow(11, repeatCounter-1);
                }
                else if (currentChar == 'c'){
                    this.score+=Math.pow(12, repeatCounter-1);
                }
                else if (currentChar == 'd'){
                    this.score+=Math.pow(13, repeatCounter-1);
                }
                else if (currentChar == 'e'){
                    this.score+=Math.pow(14, repeatCounter-1);
                }
                else if (currentChar == 'f'){
                    this.score+=Math.pow(15, repeatCounter-1);
                }
                else{
                    // For other characters, convert their ASCII value to an integer and calculate the score
                    int x = (int)currentChar - 48;
                    this.score+=Math.pow(x, repeatCounter-1);
                }
            }
            // Update the index based on the number of consecutive occurrences
            i+= repeatCounter-1;
        }
        // Return the final score
        return this.score;
    }

    /**
     * ranks the score based on a fixed limits
     * @return score's ranks
     */
    public int getScoreRank(){
        if(this.score < 99) scoreRank = 1;
        else if(this.score < 199) scoreRank = 2;
        else if(this.score < 399) scoreRank = 3;
        else if(this.score < 599) scoreRank = 4;
        else if(this.score >= 600) scoreRank = 5;
        return this.scoreRank;
    }
    /**
     * It creates a unique name for the monster
     * by using its hash's bits
     * @return the name of the monster
     */
    public String getName(){
        // loops through the first 6 bits
        for(int i = 0; i<8; i++){
            // turn the 0/1 char into 0/1 int
            int currentBit = (int) hashBinary.charAt(i) - 48;
            // gets the word from the list at given (i)
            String bitWord = bitWords.get(i)[currentBit];
            if(i==0){
                name = bitWord;
            }
            else if(i==4){
                this.name = this.name + " " + bitWord;
            }
            else this.name = this.name + bitWord;
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

    /**
     * initializes the hashtables for the visual/naming systems to use
     */
    public void initializedHashTables(){
        bitWords.put(0, new String[]{"Ko", "ku"});
        bitWords.put(1, new String[]{"se", "na"});
        bitWords.put(2, new String[]{"mo", "to"});
        bitWords.put(3, new String[]{"yo", "ri"});

        bitWords.put(4, new String[]{"Wa", "Gu"});
        bitWords.put(5, new String[]{"re", "yo"});
        bitWords.put(6, new String[]{"da", "chi"});
        bitWords.put(7, new String[]{"sa", "na"});
    }
    public String[] getLocation(){
        return new String[]{this.latitude, this.longitude};
    }

    public String getHashHex() { return this.hashHex; }
    public String getHashedCode() {
        return this.hashedCode;
    }
}
