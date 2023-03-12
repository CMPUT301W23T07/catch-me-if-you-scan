package cmput.app.catch_me_if_you_scan;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;
public class MonsterTest {
    public Monster createMonster(String code){
        return new Monster(code, "1", "2");
    }

    @Test
    public void testNameSys(){
        System.out.println("\n\n");
        Monster monster = createMonster("yosri");

        assertEquals(monster.getName(), "Kusemoyo Wayochisa");

        Monster monster2 = createMonster("jay");
        assertEquals(monster2.getName(), "Kunamori Guyodasa");
    }

    @Test
    public void testScoreSys(){
        System.out.println("\n\n");
        Monster monster1 = createMonster("yosri");
        assertEquals(monster1.getScore(), 18);
        System.out.println(monster1.getScore()  + " FROM MONSTER TEST");
        Monster monster2 = createMonster("jay");
        assertEquals(monster2.getScore(), 33);
        System.out.println(monster2.getScore()  + " FROM MONSTER TEST");
        Monster monster3 = createMonster("mo");
        assertEquals(monster3.getScore(), 37);
        System.out.println(monster3.getScore()  + " FROM MONSTER TEST");
        Monster monster4 = createMonster("mark");
        assertEquals(monster4.getScore(), 230);
        System.out.println(monster4.getScore()  + " FROM MONSTER TEST");
    }

}