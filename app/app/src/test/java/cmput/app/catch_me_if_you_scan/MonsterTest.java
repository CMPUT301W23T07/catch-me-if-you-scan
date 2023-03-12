package cmput.app.catch_me_if_you_scan;

import org.junit.jupiter.api.Test;

public class MonsterTest {
    public Monster createMonster(String code){
        return new Monster(code, "1", "2");
    }

    @Test
    void testNameSys(){
        Monster monster = createMonster("Jay");
        System.out.println(monster.getName());
    }

    @Test
    void testScoreSys(){
        Monster monster = createMonster("hesa");
        monster.getScore();
    }
}