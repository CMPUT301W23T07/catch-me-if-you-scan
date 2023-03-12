package cmput.app.catch_me_if_you_scan;

import java.util.Comparator;
import java.util.PriorityQueue;

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

    public User(String deviceID, String name, String email) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
    }
    public User(String deviceID, String name, String email, String description) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.description = description;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public int getScoreSum(){
        return this.scoreSum;
    }
    public int getScoreHighest(){
        Monster monster = (Monster) monstersPQ.toArray()[monstersPQ.size()-1];
        return monster.getScore();
    }

    public int getScoreLowest(){
        return monstersPQ.peek().getScore();
    }
    public int getMonstersCount(){
        return monstersPQ.size();
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description){
        this.description =description;
    }

    public void addMonster(Monster monster) {
        monstersPQ.add(monster);
        this.scoreSum += monster.getScore();
    }
}
