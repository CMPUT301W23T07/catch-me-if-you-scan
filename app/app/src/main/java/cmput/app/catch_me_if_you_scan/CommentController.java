package cmput.app.catch_me_if_you_scan;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommentController {
    private FirebaseFirestore db;
    private CollectionReference collection;

    /**
     * Creates the controller and instantiates the FirebaseFirestore object and the CollectionReference
     * @param db FirebaseFirestore Object
     */
    public CommentController(FirebaseFirestore db){
        this.db = db;
        this.collection = db.collection("Comment");
    }

    /**
     * Creates a comment in the db.
     * Structure:
     *     user: mahmadi
     *     monster: fregf34qouflkersdgfio347hfwl3384hgf
     *     content: "This monster was a waste of my time!!!"
     *     datePosted: March 1, 2023 at 9:48:38 AM UTC
     * @param comment the comment object to post into the db
     * @return id of the comment in the database on success and null on failure
     */
    public String create(Comment comment) {
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("user", comment.getUsername());
        commentData.put("monster", comment.getMonsterHashCode());
        commentData.put("content", comment.getCommentMessage());
        commentData.put("datePosted", comment.getCommentDate());
        String commentUUID = String.valueOf(UUID.randomUUID());
        Task<Void> task = collection.document(commentUUID).set(commentData);
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return commentUUID;
        }
        return null;
    }

    /**
     * Fetches all comments for a specific monster
     * @param hex hex code of the monster
     * @return ArrayList of comments written for a specific monster
     */
    public ArrayList<Comment> getCommentForMonster(String hex) {
        Task<QuerySnapshot> task = collection.whereEqualTo("monster", hex).get();
        while (!task.isComplete()) {
            continue;
        }
        ArrayList<Comment> commentsFetched = new ArrayList<Comment>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String monsterHex = (String) data.get("monster");
                String username = (String) data.get("user");
                String content = (String) data.get("content");
                Timestamp datePosted = (Timestamp) data.get("datePosted");
                Comment fetchedComment = new Comment(content, datePosted, monsterHex, username);
                fetchedComment.setDbId(document.getId());
                commentsFetched.add(fetchedComment);
            }
        }
        return commentsFetched;
    }

    /**
     * Fetches all comments written by a user
     * @param user username of the user to fetch
     * @return ArrayList of comments that were written by the user
     */
    public ArrayList<Comment> getCommentForUser(String user) {
        Task<QuerySnapshot> task = collection.whereEqualTo("username", user).get();
        while (!task.isComplete()) {
            continue;
        }
        ArrayList<Comment> commentsFetched = new ArrayList<Comment>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String monsterHex = (String) data.get("monster");
                String username = (String) data.get("user");
                String content = (String) data.get("content");
                Timestamp datePosted = (Timestamp) data.get("datePosted");
                Comment fetchedComment = new Comment(content, datePosted, monsterHex, username);
                fetchedComment.setDbId(document.getId());
                commentsFetched.add(fetchedComment);
            }
        }
        return commentsFetched;
    }

    /**
     * Deletes the comment in the db
     * @param dbID the db id of the comment
     * @return boolean value corresponding with the success of the delete
     */
    public boolean deleteComment(String dbID) {
        Task<Void> task = collection.document(dbID).delete();
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Updates the comment content in the db
     * @param dbID the database ID of the comment
     * @param content the new content of the comment
     * @param date the date/time of the update
     * @return boolean value corresponding with the success of the update
     */
    public boolean updateComment(String dbID, String content, Timestamp date) {
        Map<String, Object> newContent = new HashMap<>();
        newContent.put("content", content);
        newContent.put("date", date);
        Task<Void> task = collection.document(dbID).update(newContent);
        while (!task.isComplete()) {
            continue;
        }
        if (task.isSuccessful()) {
            return true;
        }
        return false;
    }
}
