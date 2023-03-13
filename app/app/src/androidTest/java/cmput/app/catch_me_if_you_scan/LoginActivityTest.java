/*
 * This class contains a test suite for LoginActivity, which is a class that allows users to sign up.
 */
package cmput.app.catch_me_if_you_scan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LoginActivityTest {

    private ActivityScenario<LoginActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(LoginActivity.class);
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

        Intents.init();

        getInstrumentation().waitForIdleSync();

        scenario.onActivity(activity -> {
        });

        onView(withId(R.id.sign_up_button)).perform(click());

        Intents.intended(hasComponent(MainActivity.class.getCanonicalName()));
        Intents.release();
    }

    @After
    public void tearDown() {
        scenario.close();
    }

}
