// package cmput.app.catch_me_if_you_scan;

// import org.junit.Test;
// import static junit.framework.Assert.assertEquals;
// public class MonsterTest {
//     /**
//      * Creates a new Monster object given its code.
//      * @param code the code of the Monster to be created
//      * @return the created Monster object
//      */
//     public Monster createMonster(String code){
//         byte[] photoString = new byte[1];
//         return new Monster(code, 1.0, 2.0, null);
//     }

//     /**
//      * Tests the getName method of the Monster class.
//      */
//     @Test
//     public void testNameSys(){
//         System.out.println("\n\n");
//         Monster monster = createMonster("yosri");

//         //checks it returns the correct name
//         assertEquals(monster.getName(), "Kusemoyo Wayochisa");

//         Monster monster2 = createMonster("jay");
//         //checks it returns the correct name
//         assertEquals(monster2.getName(), "Kunamori Guyodasa");
//     }


//     /**
//      * Tests the getScore method of the Monster class.
//      */
//     @Test
//     public void testScoreSys(){
//         System.out.println("\n\n");
//         Monster monster1 = createMonster("yosri");
//         //checks it returns the correct value of the hash
//         assertEquals(monster1.getScore(), 18);
//         System.out.println(monster1.getScore()  + " FROM MONSTER TEST");
//         Monster monster2 = createMonster("jay");
//         //checks it returns the correct value of the hash

//         assertEquals(monster2.getScore(), 33);
//         System.out.println(monster2.getScore()  + " FROM MONSTER TEST");
//         Monster monster3 = createMonster("mo");
//         //checks it returns the correct value of the hash
//         assertEquals(monster3.getScore(), 37);
//         System.out.println(monster3.getScore()  + " FROM MONSTER TEST");
//         Monster monster4 = createMonster("mark");
//         //checks it returns the correct value of the hash
//         assertEquals(monster4.getScore(), 230);
//         System.out.println(monster4.getScore()  + " FROM MONSTER TEST");
//     }


// }
