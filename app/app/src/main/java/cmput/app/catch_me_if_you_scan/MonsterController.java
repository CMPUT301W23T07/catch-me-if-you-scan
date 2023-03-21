package cmput.app.catch_me_if_you_scan;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Bytes;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller for the Monster, it communicates to the database in the CRUD format
 */
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
    public boolean create(Monster monster) {
        Map<String, Object> monsterData = new HashMap<>();
        Double[] monsterLocation = monster.getLocation();
        GeoPoint locationPoint = new GeoPoint(0, 0);
        if (monsterLocation[0] != null && monsterLocation[1] != null) {
            locationPoint = new GeoPoint(monsterLocation[0], monsterLocation[1]);
        }
        monsterData.put("name", monster.getName());
        monsterData.put("score", monster.getScore());
        monsterData.put("location", locationPoint);
        Blob envPhotoBlob = Blob.fromBytes(monster.getEnvPhoto());
        monsterData.put("envPhoto", envPhotoBlob);
        Task<Void> task = collection.document(monster.getHashHex()).set(monsterData);
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Fetches the DocumentReference of a Monster
     * @param hex hexcode of the monster to fetch DocumentReference of
     * @return DocumentReference type for the monster
     */
    public DocumentReference getMonsterDoc(String hex) {
        Task<DocumentSnapshot> task = collection.document(hex).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return (task.getResult().getReference());
        }
        return null;
    }

    /**
     * gets a monster with a specific hex code
     * @param hex Hex code of monster
     * @return monster object with given hex code
     */
    /*
    Returns the monster object with the given ID if it exists
    Returns null if it does not exist in the database.
     */
    public Monster getMonster(String hex){
        Task<DocumentSnapshot> task = collection.document(hex).get();
        while (!task.isSuccessful()) {
            continue;
        }
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            Map<String, Object> data = document.getData();
            String name = (String) data.get("name");
            int score = ((Long) data.get("score")).intValue();
            GeoPoint location = (GeoPoint) data.get("location");
            Blob envPhotoBlob = (Blob) data.get("envPhoto");
            byte[] envPhoto = envPhotoBlob.toBytes();
            return (new Monster(name, score, document.getId(), location.getLongitude(), location.getLatitude(), envPhoto));
        }
        return null;
    }

    /**
     * Fetches all Monsters in the db
     * @return ArrayList of all Monsters in the db on success and empty arraylist on failure
     */
    public ArrayList<Monster> getAllMonsters() {
        ArrayList<Monster> allMonsters = new ArrayList<Monster>();
        Task<QuerySnapshot> task = collection.get();
        while (!task.isSuccessful()) {
            continue;
        }
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String name = (String) data.get("name");
                int score = ((Long) data.get("score")).intValue();
                GeoPoint location = (GeoPoint) data.get("location");
                Blob envPhotoBlob = (Blob) data.get("envPhoto");
                byte[] envPhoto = envPhotoBlob.toBytes();
                Monster newMonster = new Monster(name, score, document.getId(), location.getLongitude(), location.getLatitude(), envPhoto);
                allMonsters.add(newMonster);
            }
        }
        return allMonsters;
    }

    /**
     * Get monster from db by its name
     * @param name name of the monster
     * @return monster in db with given name
     */
    public Monster getMonsterByName(String name) {
        Task<QuerySnapshot> task = collection.whereEqualTo("name", name).get();
        while (!task.isSuccessful()) {
            continue;
        }
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String monsterName = (String) data.get("name");
                int score = ((Long) data.get("score")).intValue();
                GeoPoint location = (GeoPoint) data.get("location");
                Blob envPhotoBlob = (Blob) data.get("envPhoto");
                byte[] envPhoto = envPhotoBlob.toBytes();
                Monster newMonster = new Monster(monsterName, score, document.getId(), location.getLongitude(), location.getLatitude(), envPhoto);
                return newMonster;
            }
        }
        return null;
    }

    /**
     * Updates the environment photo for a monster in the db
     * @param hex hex hash of the monster to be updated
     * @param envPhoto new environment photo
     * @return boolean value corresponding with the success of the update
     */
    public boolean updatePhoto(String hex, byte[] envPhoto) {
        Blob envPhotoBlob = Blob.fromBytes(envPhoto);
        Task<Void> task = collection.document(hex).update("envPhoto", envPhotoBlob);
        while (!task.isComplete()) {
            return true;
        }
        return false;
    }

    /**
     * Updates the current location of a monster in the db
     * @param hex hex hash of the monster to be updated
     * @param location new location of the monster
     * @return boolean value corresponding with the success of the update
     */
    public boolean updateLocation(String hex, GeoPoint location) {
        Task<Void> task = collection.document(hex).update("location", location);
        while (!task.isComplete()) {
            return true;
        }
        return false;
    }


// ************************* CODE BELOW MAY BE USED IN THE FUTURE, LEAVE IT FOR NOW ****************************
//    /**
//     * Adds user to list in the database.
//     * @param id Monster dbID
//     * @param user DocumentReference of user to add into the list
//     * @return Boolean value corresponding to whether the addition was successful or not
//     */
//    public boolean addUser(String id, DocumentReference user) {
//        boolean[] success = new boolean[1];
//        collection.document(id)
//                .update("usersScanned", FieldValue.arrayUnion(user))
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        success[0] = true;
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        success[0] = false;
//                    }
//                });
//        return success[0];
//    }

//    /**
//     * Deletes user from list in the database.
//     * @param id Monster dbID
//     * @param user DocumentReference of user to delete from the list
//     * @return Boolean value corresponding to whether the deletion was successful or not
//     */
//    public boolean deleteUser(String id, DocumentReference user) {
//        ArrayList<Boolean> success = new ArrayList<Boolean>();
//        collection.document(id)
//                .update("usersWhoScanned", FieldValue.arrayRemove(user))
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        success.add(Boolean.TRUE);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        success.add(Boolean.FALSE);
//                    }
//                });
//        return success.get(0).booleanValue();
//    }
}
