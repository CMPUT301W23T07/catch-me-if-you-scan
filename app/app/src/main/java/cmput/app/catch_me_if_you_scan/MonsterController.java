package cmput.app.catch_me_if_you_scan;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MonsterController {

    private FirebaseFirestore db;
    private CollectionReference collection;

    public MonsterController(FirebaseFirestore db){
        this.db = db;
        this.collection = db.collection("Monster");
    }

    /*
    Takes in a Monster object and sends it to the database.
    Validates Monster fields to ensure all necessary fields are filled in
    and returns "true" if the creation was successful and false if it was
    unsuccessful along with a console log of the issue.
     */
    /**
     * Adds Monster to Database Collection.
     * @return A boolean value correlated with the success of the database write.
     */
    public String create(Monster monster) {
        String[] monsterID = new String[1];
        Map<String, Object> monsterData = new HashMap<>();
        Double[] monsterLocation = monster.getLocation();
        GeoPoint locationPoint = new GeoPoint(monsterLocation[0], monsterLocation[1]);
        monsterData.put("name", monster.getName());
        monsterData.put("score", monster.getScore());
        monsterData.put("location", locationPoint);
        monsterData.put("envPhoto", monster.getEnvPhoto());
        collection.add(monsterData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        monsterID[0] = (String) documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        monsterID[0] = "FAIL";
                    }
                });
        return monsterID[0];
    }

    /**
     * gets a monster with a specific id
     * @param id Id of monster in db
     * @return monster object with given id
     */
    /*
    Returns the monster object with the given ID if it exists
    Returns null if it does not exist in the database.
     */
    public Monster getMonster(String id){
        ArrayList<Monster> monster = new ArrayList<Monster>();
        ArrayList<Boolean> success = new ArrayList<Boolean>();
        collection.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String name = (String) data.get("name");
                        int score = (int) data.get("score");
                        int intHash = (int) data.get("intHash");
                        GeoPoint location = (GeoPoint) data.get("location");
                        String envPhoto = (String) data.get("envPhoto");
                        Monster newMonster = new Monster(document.getId(), name, score, intHash, location.getLongitude(), location.getLatitude(), envPhoto);
                        monster.add(newMonster);
                        success.add(Boolean.TRUE);
                    } else {
                        success.add(Boolean.FALSE);
                    }
                } else {
                    success.add(Boolean.FALSE);
                }
            }
        });
        if (success.get(0) == Boolean.FALSE) {
            return null;
        }
        return monster.get(0);
    }

    /**
     * Get monster from db by its name
     * @param name name of the monster
     * @return monster in db with given name
     */
    public Monster getMonsterByName(String name) {
        ArrayList<Monster> monster = new ArrayList<Monster>();
        ArrayList<Boolean> success = new ArrayList<Boolean>();
        collection.whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String name = (String) data.get("name");
                            int score = (int) data.get("score");
                            int intHash = (int) data.get("intHash");
                            GeoPoint location = (GeoPoint) data.get("location");
                            String envPhoto = (String) data.get("envPhoto");
                            Monster newMonster = new Monster(document.getId(), name, score, intHash, location.getLongitude(), location.getLatitude(), envPhoto);
                            monster.add(newMonster);
                            success.add(Boolean.TRUE);
                            break;
                        }
                    } else {
                        success.add(Boolean.FALSE);
                    }
                }
            });
        if (success.get(0) == Boolean.FALSE) {
            return null;
        }
        return monster.get(0);
    }


    /**
     * Adds user to list in the database.
     * @param id Monster dbID
     * @param user DocumentReference of user to add into the list
     * @return Boolean value corresponding to whether the addition was successful or not
     */
    public boolean addUser(String id, DocumentReference user) {
        ArrayList<Boolean> success = new ArrayList<Boolean>();
        collection.document(id)
                .update("usersWhoScanned", FieldValue.arrayUnion(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success.add(Boolean.TRUE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success.add(Boolean.FALSE);
                    }
                });
        return success.get(0).booleanValue();
    }

    /**
     * Deletes user from list in the database.
     * @param id Monster dbID
     * @param user DocumentReference of user to delete from the list
     * @return Boolean value corresponding to whether the deletion was successful or not
     */
    public boolean deleteUser(String id, DocumentReference user) {
        ArrayList<Boolean> success = new ArrayList<Boolean>();
        collection.document(id)
                .update("usersWhoScanned", FieldValue.arrayRemove(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success.add(Boolean.TRUE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success.add(Boolean.FALSE);
                    }
                });
        return success.get(0).booleanValue();
    }
}
