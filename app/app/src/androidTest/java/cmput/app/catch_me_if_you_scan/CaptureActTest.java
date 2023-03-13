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

public class CaptureActTest {

    private ActivityScenario<CaptureAct> scenario;
    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(CaptureAct.class);
    }

    @Test
    public void testActivityCreation() {

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }

    @After
    public void tearDown() {
        scenario.close();
    }

}