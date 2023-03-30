package cmput.app.catch_me_if_you_scan;

 import org.junit.After;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.junit.runners.JUnit4;

 import static junit.framework.Assert.assertEquals;
 import static junit.framework.Assert.assertFalse;
 import static org.junit.Assert.assertTrue;

 @RunWith(JUnit4.class)
 public class MonsterTest {

     Monster mock;
     /**
      * Creates a new Monster object given its code.
      * @param code the code of the Monster to be created
      * @return the created Monster object
      */
     public Monster createMonster(String code){
         byte[] photoString = new byte[1];
         return new Monster(code, 1.0, 2.0, photoString);
     }

     /**
      * Tests the getName method of the Monster class.
      */
     @Before
     public void setup(){
         mock = createMonster("Test");
     }
     @After
     public void reset(){
         mock = null;
     }
     @Test
     public void testNameSys(){
         System.out.println("\n\n");
         Monster monster = createMonster("yosri");

         //checks it returns the correct name
         assertEquals(monster.getName(), "Kusemoyo Wayochisa");

         Monster monster2 = createMonster("jay");
         //checks it returns the correct name
         assertEquals(monster2.getName(), "Kunamori Guyodasa");
     }

     /**
      * Tests the getScore method of the Monster class.
      */
     @Test
     public void testScoreSys(){
         Monster monster1 = createMonster("yosri");
         //checks it returns the correct value of the hash
         assertEquals(monster1.getScore(), 18);

         Monster monster2 = createMonster("jay");
         //checks it returns the correct value of the hash

         assertEquals(monster2.getScore(), 33);

         Monster monster3 = createMonster("mo");
         //checks it returns the correct value of the hash
         assertEquals(monster3.getScore(), 37);

         Monster monster4 = createMonster("mark");
         //checks it returns the correct value of the hash
         assertEquals(monster4.getScore(), 230);

     }

     @Test
     public void testLocationChangeWithMethod(){
         assertFalse(mock.getLocationEnabled());
         mock.setLocationEnabled(true);
         assertTrue(mock.getLocationEnabled());
         mock.setLocationEnabled(false);
         Assert.assertFalse(mock.getLocationEnabled());
     }

     @Test
     public void getLocationsMethod(){
         // For double comparison, we accept the specified threshold for similarity
         double epsilon = 0.000001;

         Double[] locations = mock.getLocation();
         Double latitude = locations[0];
         Double longitude = locations[1];
         assertTrue(Math.abs(latitude-1.0)<=epsilon);
         assertTrue(Math.abs(longitude-2.0)<=epsilon);

         mock.setLocations(3.0,2.0);
         locations = mock.getLocation();
         latitude = locations[0];
         longitude = locations[1];

         assertTrue(Math.abs(latitude-3.0)<=epsilon);
         assertTrue(Math.abs(longitude-2.0)<=epsilon);

     }

     @Test
     public void setEnvPhotoMethod(){
         mock.setEnvPhoto(new byte[1]);
         Assert.assertTrue(mock.getEnvPhoto()[0]==0);

         int length = 9;

         byte[] mockPhoto = new byte[length];

         for(int index = 0; index < length; index++){
            mockPhoto[index] = (byte) index;
         }
         mock.setEnvPhoto(mockPhoto);

         for(int index = 0; index < length; index++){
             Assert.assertTrue(mock.getEnvPhoto()[index]==(byte) index);
         }

     }
 }
