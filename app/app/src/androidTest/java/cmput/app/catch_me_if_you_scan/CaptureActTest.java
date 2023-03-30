/*
 * This class contains a test suite for CaptureAct, which is a class that allows users to scan
 * QR codes.
 */

package cmput.app.catch_me_if_you_scan;

import static org.junit.Assert.assertNotNull;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CaptureActTest {

    private ActivityScenario<CaptureAct> scenario;

    /**
     * Sets up the testing environment before each test method runs.
     */
    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(CaptureAct.class);
    }

    /**
     * Tests that the activity is created properly.
     */
    @Test
    public void testActivityCreation() {

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }


    /**
     * Cleans up the testing environment after each test method runs.
     */
    @After
    public void tearDown() {
        scenario.close();
    }

}