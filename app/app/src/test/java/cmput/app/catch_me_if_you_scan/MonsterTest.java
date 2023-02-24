package cmput.app.catch_me_if_you_scan;

import org.junit.jupiter.api.Test;

public class MonsterTest {
    public Monster createMonster(String code){
        return new Monster(code);
    }

    @Test
    void testNameSys(){
        Monster monster = createMonster("test");
        monster.getName();
    }
}
