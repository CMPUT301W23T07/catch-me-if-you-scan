 package cmput.app.catch_me_if_you_scan;

 import org.junit.Test;
 import static junit.framework.Assert.assertEquals;

 public class UserTest {
     /**
      * Creates a new User object.
      *
      * @return a new User object.
      */
     public User createUser(){
         return new User("123", "yosri", "email");
     }

     /**
      * Creates a new Monster object.
      *
      * @param code the code of the new monster.
      * @return a new Monster object.
      */
     public Monster createMonster(String code){
         return new Monster(code, 1.0, 2.0, null);
     }

     /**
      * Tests the functionality of the addMonster() method.
      */
     @Test
     public void addMonsterTest(){

         User user = createUser();
         Monster monster = createMonster("yosri");
         user.addMonster(monster);
         // Makes sure monster was added correctly and size is right
         assertEquals(user.getMonstersCount(), 1);
     }

     /**
      * Tests the functionality of the getScoreSum() method.
      */
     @Test
     public void getScoreSumTest(){

         User user = createUser();
         Monster monster = createMonster("yosri");
         user.addMonster(monster);
         Monster monster2 = createMonster("jay");
         user.addMonster(monster2);
         // Makes sure the sum of first two monsters is 51
         assertEquals(user.getScoreSum(), 51);
         Monster monster3 = createMonster("mo");
         user.addMonster(monster3);
         Monster monster4 = createMonster("mark");
         user.addMonster(monster4);
         // Makes sure the sum of all 4 monsters is 318
         assertEquals(user.getScoreSum(), 318);
         User user2 = createUser();
         // Makes sure a user with no monsters has 0 score
         assertEquals(user2.getScoreSum(), 0);
     }

     /**
      * Tests the functionality of the getScoreLowest() method.
      */
     @Test
     public void getScoreLowest(){
         User user = createUser();

         Monster monster3= createMonster("mark");
         user.addMonster(monster3);
         // Makes sure the lowest so far is 230
         assertEquals(user.getScoreLowest(), 230);
         Monster monster = createMonster("yosri");
         user.addMonster(monster);
         // Makes sure the lowest so far is 18
         assertEquals(user.getScoreLowest(), 18);
         Monster monster2= createMonster("jay");
         user.addMonster(monster2);
         // Makes sure the lowest so far is still 18 after adding another monster
         assertEquals(user.getScoreLowest(), 18);
         User user2 = createUser();
         // Makes sure user has lowest as 0 if no monsters exist
         assertEquals(user2.getScoreLowest(), 0);
     }

     /**
      * Tests the functionality of the getScoreHighest() method.
      */
     @Test
     public void getScoreHighest(){

         User user = createUser();
         Monster monster = createMonster("yosri");
         user.addMonster(monster);
 //        makes sure highest score is 18
         assertEquals(user.getScoreHighest(), 18);
         Monster monster2 = createMonster("jay");
         user.addMonster(monster2);
 //        makes sure highest score is 33
         assertEquals(user.getScoreHighest(), 33);
         Monster monster3 = createMonster("mark");
         user.addMonster(monster3);
 //        makes sure highest score is 230
         assertEquals(user.getScoreHighest(), 230);

         User user2 = createUser();
 //        makse sure highest score is 0 if no monsters exist
         assertEquals(user2.getScoreHighest(), 0);
     }


     /**

      Tests the functionality of the getMonstersCount() method in the User class.
      */
     @Test
     public void getMonsterCountTest(){
         User user = createUser();
         assertEquals(user.getMonstersCount(), 0);
 //        makes sure count is 0 when no monsters exist
         Monster monster = createMonster("yosri");
         user.addMonster(monster);
         Monster monster2 = createMonster("jay");
         user.addMonster(monster2);
 //        makes sure count is 2 after adding 2 monsters
         assertEquals(user.getMonstersCount(), 2);

     }


     /**

      Tests the functionality of the removeMonster() method in the User class.
      */
     @Test
     public void removeMonsterTest(){
         User user = createUser();

         Monster monster = createMonster("yosri");
         user.addMonster(monster);
         Monster monster2 = createMonster("jay");
         user.addMonster(monster2);

         user.removeMonster(monster);
 //        makes sure count is 1 when removing 1 monster

         assertEquals(user.getMonstersCount(),1);
         user.removeMonster(monster2);
 //        makes sure count is 0 when removing all monsters

         assertEquals(user.getMonstersCount(),0);
     }


 }
