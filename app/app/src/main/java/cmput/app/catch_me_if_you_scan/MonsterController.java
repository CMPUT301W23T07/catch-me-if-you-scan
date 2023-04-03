package cmput.app.catch_me_if_you_scan;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the controller for the Monster, it communicates to the database in the CRUD format
 */
public class MonsterController {
    private FirebaseFirestore db;
    private CollectionReference collection;

    /**
     * This is the constructor for the monster controller, it initializes the database
     * @param db
     */
    public MonsterController(FirebaseFirestore db){
        this.db = db;
        this.collection = db.collection("Monster");
    }

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
        monsterData.put("locationEnabled", monster.getLocationEnabled());
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
    public Monster getMonster(String hex){
        Task<DocumentSnapshot> task = collection.document(hex).get();
        while (!task.isSuccessful()) {
            continue;
        }
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                return null;
            }
            Map<String, Object> data = document.getData();
            String name = (String) data.get("name");
            int score = ((Long) data.get("score")).intValue();
            GeoPoint location = (GeoPoint) data.get("location");
            Blob envPhotoBlob = (Blob) data.get("envPhoto");
            byte[] envPhoto = envPhotoBlob.toBytes();
            boolean locationEnabled = (boolean) data.get("locationEnabled");
            return (new Monster(name, score, document.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
        }
        return null;
    }

    /**
     * Fetches all Monsters in the db
     * @return ArrayList of all Monsters in the db on success and empty arraylist on failure
     */
    public ArrayList<Monster> getAllMonsters() {
        ArrayList<Monster> allMonsters = new ArrayList<Monster>();
        Task<QuerySnapshot> task = collection.orderBy("score", Query.Direction.DESCENDING).get();
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
                boolean locationEnabled = (boolean) data.get("locationEnabled");
                Monster newMonster = new Monster(name, score, document.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled);
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
        Monster newMonster = null;
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
                boolean locationEnabled = (boolean) data.get("locationEnabled");
                newMonster = new Monster(monsterName, score, document.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled);
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
}
