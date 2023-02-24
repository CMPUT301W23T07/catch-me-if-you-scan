package cmput.app.catch_me_if_you_scan;

import android.graphics.Region;

import java.util.Comparator;
import java.util.PriorityQueue;

public class User {

    private String name;
    private String email;
    private int score;
    //    private Region region;
    private PriorityQueue<Monster> monsters = new PriorityQueue<Monster>(11, new Comparator<Monster>() {
        @Override
        public int compare(Monster t1, Monster t2) {
            if (t1.getScore() > t2.getScore()) {
                return 1;
            }
            return -1;
        };
    });

    // Constructor
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public int getScore() {
        return this.score;
    }

//    public Location getLocation() {
//        return this.location;
//    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
        this.score += monster.getScore();
    }


}
