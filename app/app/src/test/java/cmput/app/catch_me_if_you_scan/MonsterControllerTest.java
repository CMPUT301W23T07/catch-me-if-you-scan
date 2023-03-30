 package cmput.app.catch_me_if_you_scan;

 import com.google.firebase.FirebaseApp;
 import com.google.firebase.firestore.CollectionReference;
 import com.google.firebase.firestore.FirebaseFirestore;

 import static org.junit.Assert.*;

 import org.junit.After;
 import org.junit.AfterClass;
 import org.junit.Before;
 import org.junit.BeforeClass;
 import org.junit.Test;

 public class MonsterControllerTest {

     static FirebaseFirestore db;
     static MonsterController monsterController;
     static UserController userController;


     @BeforeClass
     public static void setupBeforeClass(){
         db = FirebaseFirestore.getInstance();
         monsterController = new MonsterController(db);
         userController = new UserController(db);
     }
     @Before
     public void setup(){
     }
     @AfterClass
     public  static void finish(){
         db.terminate();
     }
     @After
     public void tearDown(){

     }


     @Test
     public void testCreate(){

     }

     @Test
     public void testUpdate(){

     }

     @Test
     public void testDelete() {

     }
 }
