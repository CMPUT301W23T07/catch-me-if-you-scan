package cmput.app.catch_me_if_you_scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentControllerTest {
    CollectionReference testDb;

    CommentController mock;

    Comment testComment;

    @Before
    public void setup() {
        this.testDb = FirebaseFirestore.getInstance().collection("TestComment");
        Timestamp time = new Timestamp(new Date());
        this.testComment = new Comment("TESTING COMMENT", time, "MONSTERHASH", "USERNAME");
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("content", this.testComment.getCommentMessage());
        commentData.put("datePosted", this.testComment.getCommentDate());
        commentData.put("monster", this.testComment.getMonsterHashCode());
        commentData.put("user", this.testComment.getUsername());
        Task<Void> task = testDb.document("COMMENTUUID").set(commentData);
        while (!task.isComplete()) {
            continue;
        }
        if (!task.isSuccessful()) {
            fail();
        }
        this.mock = new CommentController(this.testDb);
    }
    @After
    public void reset() {
        Task<Void> task = this.testDb.document("COMMENTUUID").delete();
        while (!task.isComplete()) continue;
        this.testComment = null;
        this.mock = null;
        this.testDb = null;
    }

    /**
     * Ensures the create function is working properly
     */
    @Test
    public void testCreate() {
        // Create dummy comment
        Timestamp time = new Timestamp(new Date());
        Comment dummyComment = new Comment("TESTING COMMENT2", time, "MONSTERHASH2", "USERNAME2");

        String dummyUUID = this.mock.create(dummyComment);

        // Make sure we get the uuid returned to us
        assertNotNull(dummyUUID);

        Task<DocumentSnapshot> confirm = testDb.document(dummyUUID).get();
        while (!confirm.isComplete()) continue;

        // Ensure that the comment is in the db
        Map<String, Object> commentData = confirm.getResult().getData();
        assertNotNull(commentData);
        assertEquals(commentData.get("content"), dummyComment.getCommentMessage());
        assertEquals(commentData.get("datePosted"), dummyComment.getCommentDate());
        assertEquals(commentData.get("monster"), dummyComment.getMonsterHashCode());
        assertEquals(commentData.get("user"), dummyComment.getUsername());

        Task<Void> cleanUpDummy = testDb.document(dummyUUID).delete();
        while (!cleanUpDummy.isComplete()) continue;
    }

    /**
     * Ensures comments can be fetched for a monster
     */
    @Test
    public void testGetCommentForMonster() {
        ArrayList<Comment> fetchedComments = this.mock.getCommentForMonster("MONSTERHASH");

        // Ensures comments is not empty
        assertFalse(fetchedComments.isEmpty());

        // Checks comment fields
        assertEquals(fetchedComments.get(0).getUsername(), testComment.getUsername());
        assertEquals(fetchedComments.get(0).getCommentMessage(), testComment.getCommentMessage());
        assertEquals(fetchedComments.get(0).getCommentDate(), testComment.getCommentDate());
    }

    /**
     * Ensures comments can be fetched for a user
     */
    @Test
    public void testGetCommentForUser() {
        ArrayList<Comment> fetchedComments = this.mock.getCommentForUser("USERNAME");

        // Ensures comments is not empty
        assertFalse(fetchedComments.isEmpty());

        // Checks comment fields
        assertEquals(fetchedComments.get(0).getMonsterHashCode(), testComment.getMonsterHashCode());
        assertEquals(fetchedComments.get(0).getCommentMessage(), testComment.getCommentMessage());
        assertEquals(fetchedComments.get(0).getCommentDate(), testComment.getCommentDate());
    }

    /**
     * Ensures that delete functionality works for comment
     */
    @Test
    public void testDeleteComment() {
        // Create dummy comment
        Timestamp time = new Timestamp(new Date());
        Comment dummyComment = new Comment("TESTING COMMENT2", time, "MONSTERHASH2", "USERNAME2");
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("content", dummyComment.getCommentMessage());
        commentData.put("datePosted", dummyComment.getCommentDate());
        commentData.put("monster", dummyComment.getMonsterHashCode());
        commentData.put("user", dummyComment.getUsername());
        Task<Void> createDummy = this.testDb.document("DUMMYUUID2").set(commentData);
        while (!createDummy.isComplete()) continue;

        boolean deleteRes = this.mock.deleteComment("DUMMYUUID2");

        assertTrue(deleteRes);

        // Ensure Comment was deleted from db
        Task<DocumentSnapshot> confirm = this.testDb.document("DUMMYUUID2").get();
        while (!confirm.isComplete()) continue;
        assertNull(confirm.getResult().getData());
    }

    /**
     * Ensures that the update function works properly
     */
    @Test
    public void testUpdateComment() {
        // Set up update vars
        String newContent = "NEW CONTENT!";
        Timestamp time = new Timestamp(new Date());
        boolean res = this.mock.updateComment("COMMENTUUID", newContent, time);

        assertTrue(res);

        Task<DocumentSnapshot> confirm = this.testDb.document("COMMENTUUID").get();
        while(!confirm.isComplete()) continue;

        // Ensures data has been properly updated
        assertNotNull(confirm.getResult().getData());
        assertEquals(confirm.getResult().getData().get("content"), newContent);
        assertEquals(confirm.getResult().getData().get("datePosted"), time);
    }
}
