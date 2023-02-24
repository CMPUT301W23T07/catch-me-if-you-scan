package cmput.app.catch_me_if_you_scan;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

public class MonsterControllerTest {

    FirebaseFirestore db;
    CollectionReference coll;

    MonsterControllerTest(){
        this.db = FirebaseFirestore.getInstance();
        this.coll = db.collection("Monsters");
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
