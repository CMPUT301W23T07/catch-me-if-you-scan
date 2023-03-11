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
        return monstersPQ.peek().getScore();
    }

    public int getScoreLowest(){
       Monster[] monstersArray = (Monster[]) monstersPQ.toArray();
       return monstersArray[monstersArray.length - 1].getScore();
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
