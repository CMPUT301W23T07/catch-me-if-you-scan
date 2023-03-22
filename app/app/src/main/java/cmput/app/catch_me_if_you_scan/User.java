package cmput.app.catch_me_if_you_scan;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * This class represents the User object
 */
public class User {
    private String deviceID;
    private String name;
    private String email;
    private String description;
    private int scoreSum;
    //    private Region region;

    // sorted from smallest score to biggest score
    private PriorityQueue<Monster> monstersPQ = new PriorityQueue<Monster>(11, new Comparator<Monster>() {
        @Override
        public int compare(Monster t1, Monster t2) {
            if (t1.getScore() > t2.getScore()) {
                return 1;
            }
            return -1;
        };
    });


//    constructor
    public User(String deviceID, String name, String email) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
    }
//    constructor
    public User(String deviceID, String name, String email, String description) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.description = description;
    }

    // getters
    /**
     * It takes a string has the hash, the function will see if the hash is already contained within the user.
     *
     * @return Boolean; true if the hash already exists or false if the hash does not exist
     */
    public boolean checkIfHashExist(String monsterHash){
        Iterator loop = monstersPQ.iterator();
        while (loop.hasNext()){
            Monster next = (Monster) loop.next();
            if (Objects.equals(next.getHashHex(), monsterHash)){
                return true;
            }
        }
        return false;
    }

    /**
     * gets name of user
     * @return name of user
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets email of user
     * @return email of user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * get description of user
     * @return description of user
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * gets deviceId of user
     * @return deviceID of user
     */
    public String getDeviceID() {
        return this.deviceID;
    }

    /**
     * returns the total sum score of all scanned monsters
     * @return total score sum
     */
    public int getScoreSum(){
        return this.scoreSum;
    }

    /**
     * gets the hgihest scoring monster
     * @return the score of the highest score monster
     */
    public int getScoreHighest(){
        if(monstersPQ.size()>=1){
            Monster monster = (Monster) monstersPQ.toArray()[monstersPQ.size()-1];
            return monster.getScore();
        }
        return 0;
    }

    /**
     * gets the lowest score monster if exists, otherwise 0.
     * @return lowest score monster
     */
    public int getScoreLowest(){
        if(monstersPQ.size()>=1){
            return monstersPQ.peek().getScore();
        }
        return 0;
    }

    /**
     * count how many monster user scanned
     *
     * @return number of monster scanned
     */
    public int getMonstersCount(){
        return monstersPQ.size();
    }



    // setters

    /**
     * sets name of user
     * @param name new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets email of user
     * @param email new email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets description of user
     * @param description new description to set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * adds the monster to user's account, add calculates
     * the new total score
     * @param monster the monster to be added
     */
    public void addMonster(Monster monster) {
        monstersPQ.add(monster);
        this.scoreSum += monster.getScore();
    }

    /**
     * delete selected monster from user's acount
     * @param monster the monster to be deleted
     */
    public void removeMonster(Monster monster){
        this.scoreSum -= monster.getScore();
        monstersPQ.remove(monster);
    }


}