package cmput.app.catch_me_if_you_scan;

import android.util.Log;

import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller for the User, it communicates to the database in the CRUD format
 */
public class UserController {
    private FirebaseFirestore db;
    private CollectionReference collection;

    public UserController(FirebaseFirestore db){
        this.db = db;
        this.collection = db.collection("User");
    }

    public UserController(CollectionReference collection) {
        this.collection = collection;
    }

    /**
     * Takes in a user object and writes to the database.
     * @param user user to write to database
     * @return
     * On success, the User's db ID. On failure, null
     */
    public boolean create(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("score", user.getScoreSum());
        userData.put("deviceID", user.getDeviceID());
        userData.put("description", user.getDescription());
        List<DocumentReference> tempDocs = new ArrayList<DocumentReference>();
        userData.put("monstersScanned", tempDocs);
        Task<Void> task = collection.document(user.getName()).set(userData);
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * gets user given the username of the user
     * @param username username of user to getch
     * @return User object with relevant fields filled in on success and null on failure
     */
    public User getUser(String username) {
        Task<DocumentSnapshot> task = collection.document(username).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                return null;
            }
            Log.d("USER", document.getData().toString());
            Map<String, Object> data = document.getData();
            String email = (String) data.get("email");
            String deviceId = (String) data.get("deviceID");
            int score = ((Long) data.get("score")).intValue();
            String description = (String) data.get("description");
            User fetchedUser = new User(deviceId, document.getId().toString(), email, description);
            List<DocumentReference> monsters = (List<DocumentReference>) data.get("monstersScanned");

        /*
            For each monster in the user's list, we create a new monster object for it
            and add it to the user object created.
        */
            for (DocumentReference doc : monsters) {
                Task<DocumentSnapshot> task1 = doc.get();
                while (!task1.isComplete()) {
                    continue;
                }
                if (task1.isSuccessful()) {
                    Map<String, Object> monsterData = task1.getResult().getData();
                    String monsterName = (String) monsterData.get("name");
                    int monsterScore = ((Long) monsterData.get("score")).intValue();
                    GeoPoint location = (GeoPoint) monsterData.get("location");
                    Blob envPhotoBlob = (Blob) monsterData.get("envPhoto");
                    byte[] envPhoto = envPhotoBlob.toBytes();
                    boolean locationEnabled = (boolean) monsterData.get("locationEnabled");
                    fetchedUser.addMonster(new Monster(monsterName, monsterScore, doc.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
                }
            }
            return fetchedUser;
        }
        return null;
    }

    /**
     * Gets user given a deviceID
     * @param deviceID deviceID
     * @return User object on success and null on failure
     */
    public User getUserByDeviceID(String deviceID){
        User fetchedUser = null;
        Task<QuerySnapshot> task = collection.whereEqualTo("deviceID", deviceID).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                fetchedUser = new User(deviceId, document.getId().toString(), email, description);
                List<DocumentReference> monsters = (List<DocumentReference>) data.get("monstersScanned");

            /*
                For each monster in the user's list, we create a new monster object for it
                and add it to the user object created.
            */
                for (DocumentReference doc : monsters) {
                    Task<DocumentSnapshot> task1 = doc.get();
                    while (!task1.isComplete()) {
                        continue;
                    }
                    if (task1.isSuccessful()) {
                        Map<String, Object> monsterData = task1.getResult().getData();
                        String monsterName = (String) monsterData.get("name");
                        int monsterScore = ((Long) monsterData.get("score")).intValue();
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        Blob envPhotoBlob = (Blob) monsterData.get("envPhoto");
                        byte[] envPhoto = envPhotoBlob.toBytes();
                        boolean locationEnabled = (boolean) monsterData.get("locationEnabled");
                        fetchedUser.addMonster(new Monster(monsterName, monsterScore, doc.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
                    }
                }
                return fetchedUser;
            }
        }
        return null;
    }

    /**
     * Gets user from db that has a certain email
     * @param passedEmail email of user
     * @return User object that has the email given
     */
    public User getUserByEmail(String passedEmail) {
        User fetchedUser = null;
        Task<QuerySnapshot> task = collection.whereEqualTo("email", passedEmail).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {

            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                fetchedUser = new User(deviceId, document.getId().toString(), email, description);
                List<DocumentReference> monsters = (List<DocumentReference>) data.get("monstersScanned");

            /*
                For each monster in the user's list, we create a new monster object for it
                and add it to the user object created.
            */
                for (DocumentReference doc : monsters) {
                    Task<DocumentSnapshot> task1 = doc.get();
                    while (!task1.isComplete()) {
                        continue;
                    }
                    if (task1.isSuccessful()) {
                        Map<String, Object> monsterData = task1.getResult().getData();
                        String monsterName = (String) monsterData.get("name");
                        int monsterScore = ((Long) monsterData.get("score")).intValue();
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        Blob envPhotoBlob = (Blob) monsterData.get("envPhoto");
                        byte[] envPhoto = envPhotoBlob.toBytes();
                        boolean locationEnabled = (boolean) monsterData.get("locationEnabled");
                        fetchedUser.addMonster(new Monster(monsterName, monsterScore, doc.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
                    }
                }
                return fetchedUser;
            }
        }
        return null;
    }

    /**
     * Function that retrieves all users in the database
     * @return ArrayList of all users in the database (User objects)
     */
    public ArrayList<User> getAllUsers() {
        Task<QuerySnapshot> task = collection.get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            ArrayList<User> allUsers = new ArrayList<User>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, document.getId().toString(), email, description);
                List<DocumentReference> monsters = (List<DocumentReference>) data.get("monstersScanned");

            /*
                For each monster in the user's list, we create a new monster object for it
                and add it to the user object created.
            */
                for (DocumentReference doc : monsters) {
                    Task<DocumentSnapshot> task1 = doc.get();
                    while (!task1.isComplete()) {
                        continue;
                    }
                    if (task1.isSuccessful()) {
                        Map<String, Object> monsterData = task1.getResult().getData();
                        String monsterName = (String) monsterData.get("name");
                        int monsterScore = ((Long) monsterData.get("score")).intValue();
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        Blob envPhotoBlob = (Blob) monsterData.get("envPhoto");
                        byte[] envPhoto = envPhotoBlob.toBytes();
                        boolean locationEnabled = (boolean) monsterData.get("locationEnabled");
                        fetchedUser.addMonster(new Monster(monsterName, monsterScore, doc.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
                    }
                }
                allUsers.add(fetchedUser);
            }
            return allUsers;
        }
        return null;
    }

    /**
     * Gets all users that have scanned a monster
     * @param monster DocumentReference of monster
     * @return an array list of users who have scanned the monster
     */
    public ArrayList<User> getAllUsersForMonster(DocumentReference monster) {
        Task<QuerySnapshot> task = collection.whereArrayContains("monstersScanned", monster).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            ArrayList<User> allUsers = new ArrayList<User>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, document.getId().toString(), email, description);
                List<DocumentReference> monsters = (List<DocumentReference>) data.get("monstersScanned");

            /*
                For each monster in the user's list, we create a new monster object for it
                and add it to the user object created.
            */
                for (DocumentReference doc : monsters) {
                    Task<DocumentSnapshot> task1 = doc.get();
                    while (!task1.isComplete()) {
                        continue;
                    }
                    if (task1.isSuccessful()) {
                        Map<String, Object> monsterData = task1.getResult().getData();
                        String monsterName = (String) monsterData.get("name");
                        int monsterScore = ((Long) monsterData.get("score")).intValue();
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        Blob envPhotoBlob = (Blob) monsterData.get("envPhoto");
                        byte[] envPhoto = envPhotoBlob.toBytes();
                        boolean locationEnabled = (boolean) monsterData.get("locationEnabled");
                        fetchedUser.addMonster(new Monster(monsterName, monsterScore, doc.getId().toString(), location.getLongitude(), location.getLatitude(), envPhoto, locationEnabled));
                    }
                }
                allUsers.add(fetchedUser);
            }
            return allUsers;
        }
        return null;
    }

    /**
     * Updates a user's attributes in the db
     * @param username username of the user to update
     * @param attributes map of the fields to be updated (as Strings) and the values to update to
     * @return boolean value corresponding with the success of the update
     */
    public boolean updateUser(String username, Map<String, Object> attributes) {
        Task<Void> task = collection.document(username)
                        .update(attributes);
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Deletes user from db
     * @param username username of user to be deleted
     * @return boolean value corresponding to the success of the deletion
     */
    public boolean deleteUser(String username) {
        Task<Void> task = collection.document(username)
                        .delete();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Adds Monster Reference to User entry in db
     * @param username username of the user
     * @param monster DocumentReference of the Monster to add
     * @return boolean value corresponding to the success of the update
     */
    public boolean addMonster(String username, DocumentReference monster) {
        Task<Void> task = collection.document(username)
                        .update("monstersScanned", FieldValue.arrayUnion(monster));
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Deletes Monster Reference to User entry in db
     * @param username username of the user
     * @param monster DocumentReference of the Monster to delete
     * @return boolean value corresponding to the success of the update
     */
    public boolean deleteMonster(String username, DocumentReference monster) {
        Task<Void> task = collection.document(username).update("monstersScanned", FieldValue.arrayRemove(monster));

        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

}