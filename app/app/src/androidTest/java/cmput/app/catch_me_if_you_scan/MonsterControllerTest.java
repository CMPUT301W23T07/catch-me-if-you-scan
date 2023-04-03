package cmput.app.catch_me_if_you_scan;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class MonsterControllerTest {
    CollectionReference testDb;
    MonsterController mock;

    byte[] fakePhoto;

    Monster testMonster;

    /**
     * Initialize testMonster, fakePhoto, mock monster controller and testDB
     */
    @Before
    public void setup() {
        this.testDb = FirebaseFirestore.getInstance().collection("TestMonster");
        this.mock = new MonsterController(this.testDb);
        this.fakePhoto = new byte[1];
        this.testMonster = new Monster("TESTMONSTER1", 20, "23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec42", 1.0, 1.0, fakePhoto, false);
        Map<String, Object> monsterData = new HashMap<>();
        Double[] monsterLocation = testMonster.getLocation();
        GeoPoint locationPoint = new GeoPoint(0, 0);
        if (monsterLocation[0] != null && monsterLocation[1] != null) {
            locationPoint = new GeoPoint(monsterLocation[0], monsterLocation[1]);
        }
        monsterData.put("name", testMonster.getName());
        monsterData.put("score", testMonster.getScore());
        monsterData.put("location", locationPoint);
        Blob envPhotoBlob = Blob.fromBytes(testMonster.getEnvPhoto());
        monsterData.put("envPhoto", envPhotoBlob);
        monsterData.put("locationEnabled", testMonster.getLocationEnabled());
        Task<Void> task = testDb.document(testMonster.getHashHex()).set(monsterData);
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
        Task<Void> task = this.testDb.document(testMonster.getHashHex()).delete();
        while (!task.isComplete()) {
            continue;
        }
        if (!task.isSuccessful()) {
            fail();
        }
        this.testDb = null;
        this.testMonster = null;
    }
    /**
     * Tests if create function works properly
     */
    @Test
    public void testCreate() {
        // Create dummy monster
        Monster dummyMonster = new Monster("TESTMONSTER2", 19, "23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec46", 2.0, 2.0, this.fakePhoto, true);

        // Make sure controller returns true
        assertTrue(this.mock.create(dummyMonster));

        // Fetch from db to confirm
        Task<DocumentSnapshot> confirm = this.testDb.document(dummyMonster.getHashHex()).get();
        while (!confirm.isComplete()) continue;

        // Check to see if test monster successfully uploaded
        assertEquals(confirm.getResult().getId(), dummyMonster.getHashHex());
        assertEquals(confirm.getResult().getData().get("name"), dummyMonster.getName());
        assertEquals(confirm.getResult().getData().get("locationEnabled"), dummyMonster.getLocationEnabled());
        assertEquals(confirm.getResult().getData().get("score"), Integer.toUnsignedLong(dummyMonster.getScore()));

        Task<Void> deleteDummy = this.testDb.document(dummyMonster.getHashHex()).delete();
        while(!deleteDummy.isComplete()) continue;
    }

    /**
     * Tests if getMonsterDoc() works properly
     */
    @Test
    public void testGetMonsterDoc() {
        DocumentReference docRef = mock.getMonsterDoc(this.testMonster.getHashHex());
        Task<DocumentSnapshot> confirm = testDb.document(this.testMonster.getHashHex()).get();
        while (!confirm.isComplete()) continue;
        // Ensures correct docRef is being returned
        assertEquals(docRef, confirm.getResult().getReference());
    }

    /**
     * Test if getMonster works properly
     */
    @Test
    public void testGetMonster() {
        Monster fetchedMonster = this.mock.getMonster(this.testMonster.getHashHex());

        // Ensures monster is fetched
        assertNotNull(fetchedMonster);
        assertEquals(fetchedMonster.getScore(), this.testMonster.getScore());
        assertArrayEquals(fetchedMonster.getLocation(), this.testMonster.getLocation());
        assertEquals(fetchedMonster.getHashHex(), this.testMonster.getHashHex());
        assertEquals(fetchedMonster.getName(), this.testMonster.getName());
        assertEquals(fetchedMonster.getLocationEnabled(), this.testMonster.getLocationEnabled());

        // Tests to make sure a monster that does not exist will not get returned
        Monster monsterDNE = this.mock.getMonster("FFFFFFFF");
        assertNull(monsterDNE);
    }

    /**
     * Test if we can get monster by name
     */
    @Test
    public void testGetMonsterByName() {
        Monster fetchedMonster = this.mock.getMonsterByName(this.testMonster.getName());

        // Ensures monster is fetched
        assertNotNull(fetchedMonster);
        assertEquals(fetchedMonster.getScore(), this.testMonster.getScore());
        assertArrayEquals(fetchedMonster.getLocation(), this.testMonster.getLocation());
        assertEquals(fetchedMonster.getHashHex(), this.testMonster.getHashHex());
        assertEquals(fetchedMonster.getName(), this.testMonster.getName());
        assertEquals(fetchedMonster.getLocationEnabled(), this.testMonster.getLocationEnabled());

        // Tests to make sure a monster that does not exist will not get returned
        Monster monsterDNE = this.mock.getMonsterByName("FAKENAME");
        assertNull(monsterDNE);
    }

    /**
     * Ensures that the get all monster functionality works properly
     */
    @Test
    public void testGetAllMonsters() {
        ArrayList<Monster> allMonsters = this.mock.getAllMonsters();
        assertNotNull(allMonsters);
        assertFalse(allMonsters.isEmpty());
        assertEquals(allMonsters.get(0).getHashHex(), this.testMonster.getHashHex());
    }

    /**
     * Test if we can update monster location
     */
    @Test
    public void testUpdateLocation() {
        GeoPoint newLoc = new GeoPoint(-10, 10);

        boolean res = this.mock.updateLocation(this.testMonster.getHashHex(), newLoc);
        assertTrue(res);

        Task<DocumentSnapshot> confirm = testDb.document(this.testMonster.getHashHex()).get();
        while (!confirm.isComplete()) continue;

        // Ensures updated location is the same
        assertEquals(newLoc, confirm.getResult().getData().get("location"));
    }

    /**
     * Test if we can update a monster photo
     */
    @Test
    public void testUpdatePhoto() {
        String stringPhoto = "FAKEPHOTO";
        byte[] envPhoto = stringPhoto.getBytes();
        Blob envPhotoBlob = Blob.fromBytes(envPhoto);

        boolean res = this.mock.updatePhoto(this.testMonster.getHashHex(), envPhoto);
        assertTrue(res);
        Task<DocumentSnapshot> confirm = testDb.document("23e6de80aa982e7cfb3b58eefd93d458a63cabba44954b50035e7e9e43bcec42").get();
        while (!confirm.isComplete()) continue;

        // Ensures photo has been updated
        assertEquals(confirm.getResult().getData().get("envPhoto"), envPhotoBlob);
    }
}