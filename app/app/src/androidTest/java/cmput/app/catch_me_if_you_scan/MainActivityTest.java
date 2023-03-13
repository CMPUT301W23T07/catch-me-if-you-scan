/**
 * This is a test class for MainActivity. It tests various functionalities and navigation using Espresso UI testing framework.
 */

package cmput.app.catch_me_if_you_scan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    public ActivityScenario<MainActivity> scenario;

    /**
     * Sets up the test scenario.
     */
    @Before
    public void setUp() {
        scenario = scenarioRule.getScenario();
    }

    /**
     * Tests if the MainActivity is created properly.
     */
    @Test
    public void testActivityCreation() {

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }

    /**
     * Tests if the fragment container is present in the activity layout.
     */
    @Test
    public void fragment_container_exists() {
        // Verify that the activity layout contains a fragment container
        scenario.onActivity(activity -> {
            View fragmentContainer = activity.findViewById(R.id.main_fragment_container);
            assertNotNull(fragmentContainer);
        });
    }

    /**
     * Tests if the app can navigate to the scanning activity using the navigation bar.
     */
    @Test
    public void switchToScanningActivityTestUsingNavBar(){

        Intents.init();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        scenario.onActivity(activity -> {
        });

        onView(withId(R.id.camera_nav)).perform(click());

        Intents.intended(hasComponent(CaptureAct.class.getCanonicalName()));
        Intents.release();
    }

    /**
     * Tests if the app can navigate to the leaderboard activity using the navigation bar.
     */
    @Test
    public void switchLeaderboardUsingNavBar() throws InterruptedException {
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());

        onView(withId(R.id.fragment_leaderboard)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the leaderboard is not displayed in the activity layout after the activity is launched.
     */
    @Test
    public void leaderBoardNotInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        onView(withId(R.id.map)).check(doesNotExist());

    }

    /**
     * Tests if the app can navigate to the profile activity using the navigation bar.
     */
    @Test
    public void switchProfileUsingNavBar(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.profile_nav)).perform(click());
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));

    }


    /**
     * Tests if the app displays the profile after launch.
     */
    @Test
    public void ProfileInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the switches to the Map using the navigation bar.
     */
    @Test
    public void switchMapUsingNavBar(){

        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.map_nav)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));

    }

    /**
     * Tests if the map is not displayed immediately after launch.
     */
    @Test
    public void mapNotInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.map)).check(doesNotExist());
    }

    /**
     * Tests if the activity properly closes.
     */
    @After
    public void tearDown() {
        scenario.close();
    }

}
