package cmput.app.catch_me_if_you_scan;

import android.graphics.Region;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        DocumentReference[] userDoc = new DocumentReference[1];
        boolean[] success = new boolean[1];

        collection.document(docID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userDoc[0] = document.getReference();
                                success[0] = true;
                            } else
                                success[0] = false;
                        } else {
                            success[0] = false;
                        }

                    }
                });
        if (!success[0]) {
            return null;
        }
        return userDoc[0];
    }

    /**
     * gets user given the dbID of the user
     * @param id dbID of the user
     * @return User object with relevant fields filled in on success and null on failure
     */
    public User getUser(String id) {
        User[] user = new User[1];
        boolean[] success = new boolean[1];

        collection.document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                String name = (String) data.get("name");
                                String email = (String) data.get("email");
                                String deviceID = (String) data.get("deviceID");
                                int score = (int) data.get("score");
                                String description = (String) data.get("description");
                                user[0] = new User(deviceID, name, email, description);
                                success[0] = true;
                            } else
                                success[0] = false;
                        } else {
                            success[0] = false;
                        }

                    }
                });
        if (!success[0]) {
            return null;
        }
        return user[0];
    }

    /**
     * Gets user given a deviceID
     * @param deviceID deviceID
     * @return User object on success and null on failure
     */
    public User getUserByDeviceID(String deviceID) {
        User[] user = new User[1];
        boolean[] success = new boolean[1];

        collection.whereEqualTo("deviceID", deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String name = (String) data.get("name");
                                String email = (String) data.get("email");
                                String deviceID = (String) data.get("deviceID");
                                int score = (int) data.get("score");
                                String description = (String) data.get("description");
                                user[0] = new User(deviceID, name, email, description);
                                success[0] = true;
                                break;
                            }
                        } else {
                            success[0] = false;
                        }
                    }
                });
        if (!success[0]) {
            return null;
        }
        return user[0];
    }

    /**
     * Gets user from db that has a certain email
     * @param email email of user
     * @return User object that has the email given
     */
    public User getUserByEmail(String email) {
        User[] user = new User[1];
        boolean[] success = new boolean[1];

        collection.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String name = (String) data.get("name");
                                String email = (String) data.get("email");
                                String deviceID = (String) data.get("deviceID");
                                int score = (int) data.get("score");
                                String description = (String) data.get("description");
                                user[0] = new User(deviceID, name, email, description);
                                success[0] = true;
                                break;
                            }
                        } else {
                            success[0] = false;
                        }
                    }
                });
        if (!success[0]) {
            return null;
        }
        return user[0];
    }

    /**
     * Function that retrieves all users in the database
     * @return ArrayList of all users in the database (User objects)
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<User>();
        boolean[] success = new boolean[1];
        collection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User newUser;
                                Map<String, Object> data = document.getData();
                                String name = (String) data.get("name");
                                String email = (String) data.get("email");
                                String deviceID = (String) data.get("deviceID");
                                int score = (int) data.get("score");
                                String description = (String) data.get("description");
                                newUser = new User(deviceID, name, email, description);
                                allUsers.add(newUser);
                                success[0] = true;
                            }
                        } else {
                            success[0] = false;
                        }
                    }
                });
        if (!success[0]) {
            return null;
        }
        return allUsers;
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
}
