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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
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

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MainActivityProfileTest {

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
    @BeforeClass
    public static void setUpBeforeClass() {
        TestDetails mock = TestDetails.getInstance(getInstrumentation().getContext());;

        UserController userController = new UserController(FirebaseFirestore.getInstance());

        if (userController.getUserByDeviceID(mock.getDeviceId()) != null) {
            userController.deleteUser(userController.getUserByDeviceID(mock.getDeviceId()).getName());
        }

        userController.create(new User(mock.getDeviceId(), mock.getTestUser(), mock.getTestEmail()));
    }
    @AfterClass
    public static void finish(){
        TestDetails mock = TestDetails.getInstance(getInstrumentation().getContext());;

        UserController userController = new UserController(FirebaseFirestore.getInstance());
        userController.deleteUser(mock.getTestUser());
    }
    /**
     * Tests if the activity properly closes.
     */
    @After
    public void tearDown() {

        scenario.close();
    }

    /**
     * Tests if the MainActivity is created properly.
     */
    @Test
    public void testActivityCreation() {
        try{

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Fail the test with the exception message
            fail(e.getMessage());
        }
    }

    /**
     * Tests if the app can navigate to the profile activity using the navigation bar.
     */
    @Test
    public void switchProfileUsingNavBar(){
        try{
        scenario.onActivity(activity -> {
            View fragmentContainer = activity.findViewById(R.id.main_fragment_container);
            assertNotNull(fragmentContainer);
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.profile_nav)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Fail the test with the exception message
            fail(e.getMessage());
        }

    }

    /**
     * Tests if the app displays the profile after launch.
     */
    @Test
    public void ProfileInViewAfterLaunch(){
        try{
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.ProfileLayout)).check(matches(isDisplayed()));
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Fail the test with the exception message
            fail(e.getMessage());
        }
    }

}
