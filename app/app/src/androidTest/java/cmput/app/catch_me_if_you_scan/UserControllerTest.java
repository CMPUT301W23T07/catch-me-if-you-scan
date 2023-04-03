package cmput.app.catch_me_if_you_scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserControllerTest {
    CollectionReference testDb;
    UserController mock;
    User testUser;

    /**
     * Setup testdb, mock usercontroller, and write test user into database
     */
    @Before
    public void setup() {
        this.testDb = FirebaseFirestore.getInstance().collection("TestUser");
        this.mock = new UserController(this.testDb);
        this.testUser = new User("FAKDEVICEID", "FAKEUSERNAME", "FAKEEMAIL", "FAKEDESCRIPTION");

        // Put Fake User in testdb
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", testUser.getEmail());
        userData.put("score", testUser.getScoreSum());
        userData.put("deviceID", testUser.getDeviceID());
        userData.put("description", testUser.getDescription());
        List<DocumentReference> tempDocs = new ArrayList<DocumentReference>();
        userData.put("monstersScanned", tempDocs);
        Task<Void> task = testDb.document(testUser.getName()).set(userData);
        while (!task.isComplete()) {
            continue;
        }
        if (!task.isSuccessful()) {
            fail();
        }
    }

    /**
     * Clean up
     */
    @After
    public void reset() {
        this.mock = null;
        Task<Void> task = this.testDb.document(testUser.getName()).delete();
        while (!task.isComplete()) {
            continue;
        }
        if (!task.isSuccessful()) {
            fail();
        }
        this.testDb = null;
        this.testUser = null;
    }

    /**
     * Tests if create function works properly
     */
    @Test
    public void testCreate() {
        // Create Dummy User
        User dummyUser = new User("FAKEDEVICEID2", "FAKEUSERNAME2", "FAKEEMAIL2", "FAKEDESCRIPTION2");
        // Make sure controller returns true
        assertTrue(this.mock.create(dummyUser));

        // Fetch from db to confirm
        Task<DocumentSnapshot> confirm = testDb.document(dummyUser.getName()).get();
        while (!confirm.isComplete()) continue;

        // Check to see if test monster successfully uploaded
        Map<String, Object> confirmData = confirm.getResult().getData();
        assertEquals(confirm.getResult().getId(), dummyUser.getName());
        assertEquals(confirmData.get("description"), dummyUser.getDescription());
        assertEquals(confirmData.get("deviceID"), dummyUser.getDeviceID());
        assertEquals(confirmData.get("email"), dummyUser.getEmail());

        // Delete Dummy User
        this.testDb.document(dummyUser.getName()).delete();
    }

    /**
     * Test if getUser() works properly
     */
    @Test
    public void testGetUser() {
        User fetchedUser = this.mock.getUser(this.testUser.getName());

        // Ensures monster is fetched
        assertNotNull(fetchedUser);
        assertEquals(this.testUser.getName(), fetchedUser.getName());
        assertEquals(this.testUser.getDescription(), fetchedUser.getDescription());
        assertEquals(this.testUser.getDeviceID(), fetchedUser.getDeviceID());
        assertEquals(this.testUser.getEmail(), fetchedUser.getEmail());

        // Tests to make sure a monster that does not exist will not get returned
        User userDNE = this.mock.getUser("DNE");
        assertNull(userDNE);
    }

    /**
     * Test if fetching user by device ID works
     */
    @Test
    public void testGetUserByDeviceID() {
        User fetchedUser = this.mock.getUserByDeviceID(this.testUser.getDeviceID());

        // Ensures monster is fetched
        assertNotNull(fetchedUser);
        assertEquals(this.testUser.getName(), fetchedUser.getName());
        assertEquals(this.testUser.getDescription(), fetchedUser.getDescription());
        assertEquals(this.testUser.getDeviceID(), fetchedUser.getDeviceID());
        assertEquals(this.testUser.getEmail(), fetchedUser.getEmail());

        // Tests to make sure a monster that does not exist will not get returned
        User userDNE = this.mock.getUserByDeviceID("DNEDEVICEID");
        assertNull(userDNE);
    }

    /**
     * Testing if fetching user by email works
     */
    @Test
    public void testGetUserByEmail() {
        User fetchedUser = this.mock.getUserByEmail(this.testUser.getEmail());

        // Ensures monster is fetched
        assertNotNull(fetchedUser);
        assertEquals(this.testUser.getName(), fetchedUser.getName());
        assertEquals(this.testUser.getDescription(), fetchedUser.getDescription());
        assertEquals(this.testUser.getDeviceID(), fetchedUser.getDeviceID());
        assertEquals(this.testUser.getEmail(), fetchedUser.getEmail());

        // Tests to make sure a monster that does not exist will not get returned
        User userDNE = this.mock.getUserByEmail("DNEEMAIL");
        assertNull(userDNE);
    }

    /**
     * Ensures that all users in the db are returned when calling getAllUsers()
     */
    @Test
    public void testGetAllUsers() {
        ArrayList<User> allUsers = this.mock.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
        assertEquals(allUsers.get(0).getName(), this.testUser.getName());
    }

    /**
     * Ensures that the update function works correctly
     */
    @Test
    public void testUpdate() {
        // Setting updated values
        Map<String, Object> updatedUserInfo = new HashMap<>();
        updatedUserInfo.put("email", "FAKEEMAIL2");
        updatedUserInfo.put("description", "FAKEDESC2");
        boolean res = this.mock.updateUser(this.testUser.getName(), updatedUserInfo);

        // Ensure call is successful
        assertTrue(res);

        Task<DocumentSnapshot> confirm = testDb.document(this.testUser.getName()).get();
        while (!confirm.isComplete()) continue;

        assertEquals(confirm.getResult().getData().get("email"), updatedUserInfo.get("email"));
        assertEquals(confirm.getResult().getData().get("description"), updatedUserInfo.get("description"));
    }

    /**
     * Tests both add and delete monster functionality (To save some time)
     */
    @Test
    public void testAddDeleteMonster() {
        // Create testMonster
        CollectionReference monsterCol = FirebaseFirestore.getInstance().collection("TestMonster");
        Map<String, Object> monsterData = new HashMap<>();
        monsterData.put("name", "Tom Hanks");
        Task<Void> task = monsterCol.document("23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec42").set(monsterData);
        while (!task.isComplete()) {
            continue;
        }
        if (!task.isSuccessful()) {
            fail();
        }
        DocumentReference monsterRef = monsterCol.document("23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec42");
        boolean addMonster = this.mock.addMonster(this.testUser.getName(), monsterRef);

        // Make sure add was successful
        assertTrue(addMonster);
        Task<DocumentSnapshot> confirm = testDb.document(this.testUser.getName()).get();
        while (!confirm.isComplete()) continue;
        ArrayList<DocumentReference> monstersScanned = (ArrayList<DocumentReference>) confirm.getResult().getData().get("monstersScanned");
        assertNotNull(monstersScanned);
        assertEquals(monstersScanned.get(0), monsterRef);

        // Delete Monster
        boolean deleteMonster = this.mock.deleteMonster(this.testUser.getName(), monsterRef);

        // Make sure delete was successful
        assertTrue(deleteMonster);
        Task<DocumentSnapshot> confirmDelete = testDb.document(this.testUser.getName()).get();
        while (!confirmDelete.isComplete()) continue;
        monstersScanned = (ArrayList<DocumentReference>) confirmDelete.getResult().getData().get("monstersScanned");
        assertNotNull(monstersScanned);
        assertTrue(monstersScanned.isEmpty());

        // Delete monster from db
        Task<Void> cleanUpMonster = monsterCol.document("23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec42").delete();
        while (!cleanUpMonster.isComplete()) continue;
    }

    /**
     * Ensures that the delete user functionality works
     */
    @Test
    public void testUserDelete() {
        Map<String, Object> doomedData = new HashMap<>();
        doomedData.put("description", "I am going to be deleted :(");
        this.testDb.document("TOBEDELETED").set(doomedData);
        boolean deleteUser = this.mock.deleteUser("TOBEDELETED");

        // Make sure controller returns true
        assertTrue(deleteUser);

        // Make sure user was deleted from db
        Task<DocumentSnapshot> confirm = this.testDb.document("TOBEDELETED").get();
        while (!confirm.isComplete()) continue;
        assertNull(confirm.getResult().getData());
    }
}
