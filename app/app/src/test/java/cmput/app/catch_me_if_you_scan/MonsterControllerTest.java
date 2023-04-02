package cmput.app.catch_me_if_you_scan;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import junit.framework.Assert;

@RunWith(JUnit4.class)
public class MonsterControllerTest {
    CollectionReference testDb = FirebaseFirestore.getInstance().collection("TestMonster");
    MonsterController mock;

    byte[] fakePhoto = new byte[1];

    Monster testMonster = new Monster("TESTMONSTER1", 20, "FFFFF", 1.0, 1.0, fakePhoto, false);

    @Before
    public void setup() { this.mock = new MonsterController(this.testDb); }
    @After
    public void reset() { this.mock = null;
        this.testDb = null; }
    /**
     * Tests if create function works properly
     */
    @Test
    public void testCreate() {

        // Make sure controller returns true
        assertTrue(mock.create(testMonster));

        // Fetch from db to confirm
        Task<DocumentSnapshot> confirm = testDb.document("FFFFF").get();
        while (!confirm.isComplete()) continue;

        // Make sure fetch was successful
        assertTrue(confirm.isSuccessful());

        // Check to see if test monster successfully uploaded
        assertSame("FFFFF", confirm.getResult().getId());

        // Make sure we can't create the same monster
        assertFalse(mock.create(testMonster));
    }

    /**
     * Tests if getMonsterDoc() works properly
     */
    @Test
    public void testGetMonsterDoc() {
        DocumentReference docRef = mock.getMonsterDoc("FFFFF");
        DocumentReference confirm = testDb.document("FFFFF").get().getResult().getReference();

        // Ensures correct docRef is being returned
        assertSame(docRef, confirm);

        // Ensures fetching a monster that does not exist is null
        DocumentReference docRefDNE = mock.getMonsterDoc("DOESNOTEXIST");
        assertNull(docRefDNE);
    }

    @Test
    public void testGetMonster() {
        Monster fetchedMonster = mock.getMonster(testMonster.getHashHex());

        // Ensures monster is fetched
        assertNotNull(fetchedMonster);

        assertSame(fetchedMonster.getHashHex(), testMonster.getHashHex());
        assertSame(fetchedMonster.getName(), testMonster.getName());
        assertSame(fetchedMonster.getEnvPhoto(), testMonster.getEnvPhoto());
        assertSame(fetchedMonster.getLocation(), testMonster.getLocation());
        assertSame(fetchedMonster.getLocationEnabled(), testMonster.getLocationEnabled());
        assertSame(fetchedMonster.);

    }
}
