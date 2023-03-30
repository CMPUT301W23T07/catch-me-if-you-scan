package cmput.app.catch_me_if_you_scan;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class represents the User object
 */
public class User implements Parcelable {
    private String deviceID;
    private String name;
    private String email;
    private String description;
    private int scoreSum;
    //    private Region region;


    private ArrayList<Monster> sortedMonster = new ArrayList<Monster>();


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

    protected User(Parcel in) {
        deviceID = in.readString();
        name = in.readString();
        email = in.readString();
        description = in.readString();
        scoreSum = in.readInt();
        sortedMonster = in.createTypedArrayList(Monster.CREATOR);
    }

    /**
     * This method is part of the implementation for parcelable
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * It takes a string has the hash, the function will see if the hash is already contained within the user.
     *
     * @return Boolean; true if the hash already exists or false if the hash does not exist
     */
    public boolean checkIfHashExist(String monsterHash){
        for (int i=0; i<sortedMonster.size(); i++){
            Monster next = sortedMonster.get(i);
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
        if(sortedMonster.size()>=1){
            Collections.sort(sortedMonster);
            Monster monster = sortedMonster.get(sortedMonster.size()-1);
            return monster.getScore();
        }
        return 0;
    }

    /**
     * gets the lowest score monster if exists, otherwise 0.
     * @return lowest score monster
     */
    public int getScoreLowest(){
        if(sortedMonster.size()>=1){
            Collections.sort(sortedMonster);
            return sortedMonster.get(0).getScore();
        }
        return 0;
    }

    /**
     * count how many monster user scanned
     *
     * @return number of monster scanned
     */
    public int getMonstersCount(){
        return sortedMonster.size();
    }


    public ArrayList<Monster>  getMonsters(){
        Collections.sort(sortedMonster);
        return sortedMonster;
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
        sortedMonster.add(monster);
        this.scoreSum += monster.getScore();
    }

    /**
     * delete selected monster from user's acount
     * @param monster the monster to be deleted
     */
    public void removeMonster(Monster monster){
        this.scoreSum -= monster.getScore();
        sortedMonster.remove(monster);
    }


    /**
     * This method is part of the parcelable implementation
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This method is part of the parcelable implementation
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(deviceID);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeInt(scoreSum);
        dest.writeTypedList(sortedMonster);
    }
}