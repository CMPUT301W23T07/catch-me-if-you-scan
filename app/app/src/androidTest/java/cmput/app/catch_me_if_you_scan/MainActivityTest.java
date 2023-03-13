package cmput.app.catch_me_if_you_scan;


import static androidx.test.espresso.Espresso.onView;
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

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


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

    @Before
    public void setUp() {
        scenario = scenarioRule.getScenario();
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
    @Test
    public void switchToScanningActivityTestUsingNavBar(){

        Intents.init();

        getInstrumentation().waitForIdleSync();

        scenario.onActivity(activity -> {
        });

        onView(withId(R.id.camera_nav)).perform(click());

        Intents.intended(hasComponent(CaptureAct.class.getCanonicalName()));
        Intents.release();
    }
    @Test
    public void switchLeaderboardUsingNavBar() throws InterruptedException {
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());

        onView(withId(R.id.fragment_leaderboard)).check(matches(isDisplayed()));

    }
    @Test
    public void leaderBoardNotInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        onView(withId(R.id.map)).check(doesNotExist());

    }
    @Test
    public void switchProfileUsingNavBar(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.profile_nav)).perform(click());
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));

    }
    @Test
    public void ProfileInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));
    }
    @Test
    public void switchMapUsingNavBar(){

        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.map_nav)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));

    }
    @Test
    public void mapNotInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.map)).check(doesNotExist());
    }
    @After
    public void tearDown() {
        scenario.close();
    }

}
