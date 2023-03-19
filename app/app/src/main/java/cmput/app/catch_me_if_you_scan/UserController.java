package cmput.app.catch_me_if_you_scan;

import android.graphics.Region;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

    /**
     * Takes in a user object and writes to the database.
     * @param user user to write to database
     * @return
     * On success, the User's db ID. On failure, null
     */
    public String create(User user) {
        String[] userDbID = new String[1];
        boolean[] success = new boolean[1];
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());
        userData.put("score", user.getScoreSum());
        userData.put("deviceID", user.getDeviceID());
        userData.put("description", user.getDescription());
        List<DocumentReference> tempDocs = new List<DocumentReference>() {
        ;
        userData.put("monstersScanned", tempDocs);
        collection.add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        userDbID[0] = documentReference.getId();
                        success[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success[0] = false;
                    }
                });
        if (!success[0]) {
            return null;
        }
        return userDbID[0];
    }

    /**
     * Gets User DocumentReference (to add to Monster's usersWhoScanned field/Comment user field)
     * @param docID doc id of user
     * @return DocumentReference object
     */
    public DocumentReference getUserDoc(String docID) {
        Task<DocumentSnapshot> task = collection.document(docID).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return (task.getResult().getReference());
        }
        return null;
    }

    /**
     * gets user given the dbID of the user
     * @param id dbID of the user
     * @return User object with relevant fields filled in on success and null on failure
     */
    public User getUser(String id) {
        Task<DocumentSnapshot> task = collection.document(id).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            Map<String, Object> data = document.getData();
            String name = (String) data.get("name");
            String email = (String) data.get("email");
            String deviceId = (String) data.get("deviceID");
            int score = ((Long) data.get("score")).intValue();
            String description = (String) data.get("description");
            User fetchedUser = new User(deviceId, name, email, description);
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
                    String hexHash = (String) monsterData.get("hash");
                    GeoPoint location = (GeoPoint) monsterData.get("location");
                    byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                    fetchedUser.addMonster(new Monster(document.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
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
        Task<QuerySnapshot> task = collection.whereEqualTo("deviceID", deviceID).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String name = (String) data.get("name");
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, name, email, description);
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
                        String hexHash = (String) monsterData.get("hash");
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                        fetchedUser.addMonster(new Monster(doc.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
                    }
                }
                return fetchedUser;
            }
        }
        return null;
    }

    /**
     * gets the user info from database using the name given by them
     * @param namePassed the name to be searched
     * @return null if name doesn't exist, object if name exists
     */
    public User getUserByName(String namePassed){
        Task<QuerySnapshot> task = collection.whereEqualTo("name", namePassed).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String name = (String) data.get("name");
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, name, email, description);
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
                        String hexHash = (String) monsterData.get("hash");
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                        fetchedUser.addMonster(new Monster(doc.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
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
        Task<QuerySnapshot> task = collection.whereEqualTo("email", passedEmail).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {

            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String name = (String) data.get("name");
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, name, email, description);
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
                        String hexHash = (String) monsterData.get("hash");
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                        fetchedUser.addMonster(new Monster(doc.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
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
                String name = (String) data.get("name");
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, name, email, description);
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
                        String hexHash = (String) monsterData.get("hash");
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                        fetchedUser.addMonster(new Monster(doc.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
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
     * @param monsterId monster id of monster to get users for
     * @return an array list of users who have scanned the monster
     */
    public ArrayList<User> getAllUsersForMonster(String monsterId) {
        Task<QuerySnapshot> task = collection.whereArrayContains("monstersScanned", monsterId).get();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            ArrayList<User> allUsers = new ArrayList<User>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String name = (String) data.get("name");
                String email = (String) data.get("email");
                String deviceId = (String) data.get("deviceID");
                int score = ((Long) data.get("score")).intValue();
                String description = (String) data.get("description");
                User fetchedUser = new User(deviceId, name, email, description);
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
                        String hexHash = (String) monsterData.get("hash");
                        GeoPoint location = (GeoPoint) monsterData.get("location");
                        byte[] envPhoto = (byte[]) monsterData.get("envPhoto");
                        fetchedUser.addMonster(new Monster(doc.getId(), monsterName, monsterScore, hexHash, location.getLongitude(), location.getLatitude(), envPhoto));
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
     * @param docID doc id of the user to update
     * @param attributes map of the fields to be updated (as Strings) and the values to update to
     * @return boolean value corresponding with the success of the update
     */
    public boolean updateUser(String docID, Map<String, Object> attributes) {
        boolean[] success = new boolean[1];

        collection.document(docID)
                .update(attributes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success[0] = false;
                    }
                });

        return success[0];
    }

    /**
     * Deletes user from db
     * @param docID doc id of the user to be deleted
     * @return boolean value corresponding to the success of the deletion
     */
    public boolean deleteUser(String docID) {
        boolean[] success = new boolean[1];

        collection.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success[0] = false;
                    }
                });
        return success[0];
    }

    /**
     * Adds Monster Reference to User entry in db
     * @param docID doc id of user
     * @param monster DocumentReference of the Monster to add
     * @return boolean value corresponding to the success of the update
     */
    public boolean addMonster(String docID, DocumentReference monster) {
        boolean[] success = new boolean[1];
        collection.document(docID)
                .update("monstersScanned", FieldValue.arrayUnion(monster))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success[0] = false;
                    }
                });
        return success[0];
    }
}
