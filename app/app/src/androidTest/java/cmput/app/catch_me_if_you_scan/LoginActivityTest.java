/*
 * This class contains a test suite for LoginActivity, which is a class that allows users to sign up.
 */
package cmput.app.catch_me_if_you_scan;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> scenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    public ActivityScenario<LoginActivity> scenario;

    private static TestDetails mock = TestDetails.getInstance(getInstrumentation().getContext());

    private static UserController userController = new UserController(FirebaseFirestore.getInstance());

    @BeforeClass
    public static void setUpBeforeClass() {

        if (userController.getUserByDeviceID(mock.getDeviceId()) != null) {
            userController.deleteUser(userController.getUserByDeviceID(mock.getDeviceId()).getName());
        }

        userController.create(new User(mock.getDeviceId(), mock.getTestUser(), mock.getTestEmail()));
    }
    @Before
    public void setUp() {

        Intents.init();
        scenario = scenarioRule.getScenario();
        mock = TestDetails.getInstance(getInstrumentation().getContext());
        if (userController.getUserByDeviceID(mock.getDeviceId()) != null) {
//            userController.deleteUser(userController.getUserByDeviceID(mock.getDeviceId()).getName());
            System.out.println(userController.deleteUser(userController.getUserByDeviceID(mock.getDeviceId()).getName()));
        }


    }
    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }
    @AfterClass
    public static void finish(){
        TestDetails mock = TestDetails.getInstance(getInstrumentation().getContext());;

        UserController userController = new UserController(FirebaseFirestore.getInstance());
        userController.deleteUser(mock.getTestUser());
    }

    /**
     * Tests if the LoginActivity activity is created.
     */
    @Test
    public void testActivityCreation() {
        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }

    /**
     * Tests if the SignUp button switches the LoginActivity activity to the MainActivity activity.
     */
    @Test
    public void switchToMainActivityUsingSignUpButton(){

        getInstrumentation().waitForIdleSync();

        scenario.onActivity(activity -> {
        });

        onView(withId(R.id.editTextName)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextName)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText(mock.getTestUser()));
        onView(withId(R.id.editTextName))
                .perform(pressImeActionButton());

        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextEmail)).perform(click());
        onView(withId(R.id.editTextEmail)).perform(typeText(mock.getTestEmail()));
        onView(withId(R.id.editTextName))
                .perform(pressImeActionButton());

        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_up_button)).perform(click());
        onView(withId(R.id.profile_nav)).check(matches(isDisplayed()));

        Intents.intended(hasComponent(MainActivity.class.getCanonicalName()));


    }

}
