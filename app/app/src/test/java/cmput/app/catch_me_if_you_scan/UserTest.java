package cmput.app.catch_me_if_you_scan;

import static junit.framework.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    public User createUser(){
        return new User("123", "yosri", "email");
    }
    public Monster createMonster(String code){
        return new Monster(code, "1", "2");
    }


    @Test
    public void addMonsterTest(){
        System.out.println("\n\n addMonsterTest -------------");
        User user = createUser();
        Monster monster = createMonster("yosri");
        user.addMonster(monster);
        assertEquals(user.getMonstersCount(), 1);
    }

    @Test
    public void getScoreSumTest(){
        System.out.println("\n\n getScoreSumTest --------------");
        User user = createUser();
        Monster monster = createMonster("yosri");
        user.addMonster(monster);
        Monster monster2 = createMonster("jay");
        user.addMonster(monster2);
        assertEquals(user.getScoreSum(), 51);
        Monster monster3 = createMonster("mo");
        user.addMonster(monster3);
        Monster monster4 = createMonster("mark");
        user.addMonster(monster4);
        assertEquals(user.getScoreSum(), 318);
    }

    @Test
    public void getScoreLowest(){
        System.out.println("\n\n getScoreLowest --------------");
        User user = createUser();
        Monster monster3= createMonster("mark");
        user.addMonster(monster3);
        assertEquals(user.getScoreLowest(), 230);
        Monster monster = createMonster("yosri");
        user.addMonster(monster);
        assertEquals(user.getScoreLowest(), 18);
        Monster monster2= createMonster("jay");
        user.addMonster(monster2);
        assertEquals(user.getScoreLowest(), 18);
    }

    @Test
    public void getScoreHighest(){
        System.out.println("\n\n getScoreHighest --------------");
        User user = createUser();
        Monster monster = createMonster("yosri");
        user.addMonster(monster);
        assertEquals(user.getScoreHighest(), 18);
        Monster monster2 = createMonster("jay");
        user.addMonster(monster2);
        assertEquals(user.getScoreHighest(), 33);
        Monster monster3 = createMonster("mark");
        user.addMonster(monster3);
        assertEquals(user.getScoreHighest(), 230);
    }


}