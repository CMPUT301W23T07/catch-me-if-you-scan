package cmput.app.catch_me_if_you_scan;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.junit.Assert.*;
import org.junit.Test;

public class MonsterControllerTest {


    @Test
    public void testCreate(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference coll = db.collection("Monsters");
        Monster monster = new Monster("Pikachu");
//
        MonsterController controller = new MonsterController(db);
        //assertEquals(4, (2+2));

        assertTrue(controller.create(monster));
    }

    @Test
    public void testUpdate(){

    }

    @Test
    public void testDelete() {

    }
}
