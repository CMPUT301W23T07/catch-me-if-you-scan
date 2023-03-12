package cmput.app.catch_me_if_you_scan;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    private ActivityScenario<MainActivity> scenario;


    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testActivityCreation() {

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }

    @Test
    public void fragment_container_exists() {
        // Verify that the activity layout contains a fragment container
        scenario.onActivity(activity -> {
            View fragmentContainer = activity.findViewById(R.id.main_fragment_container);
            assertNotNull(fragmentContainer);
        });
    }

//    @Test
//    public void testReplaceFragment() {
//        scenario.onActivity(activity -> {
//
//            FrameLayout frameLayout = activity.findViewById(R.id.main_fragment_container);
//
//            activity.replaceFragment(new MapFragment());
//
//            //TODO
//            // Verify that the MapFragment has been successfully replaced
//
//            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
//
//
//        });
//
//    }



    @After
    public void tearDown() {
        scenario.close();
    }
}
