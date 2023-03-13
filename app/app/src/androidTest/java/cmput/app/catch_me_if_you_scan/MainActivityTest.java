package cmput.app.catch_me_if_you_scan;


import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.hamcrest.Matcher;
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
    public void switchLeaderboardUsingNavBar(){
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        onView(withId(R.id.LeaderBoardLayout)).check(matches(isDisplayed()));

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
