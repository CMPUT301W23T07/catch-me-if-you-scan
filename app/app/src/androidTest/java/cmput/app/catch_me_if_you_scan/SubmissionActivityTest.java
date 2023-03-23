/**
 * This is a test class for MainActivity. It tests various functionalities and navigation using Espresso UI testing framework.
 */

package cmput.app.catch_me_if_you_scan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
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

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.journeyapps.barcodescanner.CaptureActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SubmissionActivityTest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), SubmissionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Code name", "codedMessage");
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<SubmissionActivity> scenarioRule = new ActivityScenarioRule<>(intent);

    public ActivityScenario<SubmissionActivity> scenario;

    /**
     * Sets up the test scenario.
     */
    @Before
    public void setUp() {
        Intents.init();

        scenario = scenarioRule.getScenario();
    }
    /**
     * Tests if the activity properly closes.
     */
    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }

    /**
     * Tests if the SubmissionActivity is created properly.
     */

    @Test
    public void testActivityCreation() {
        // Verify that the activity is created
        scenario.onActivity(activity -> {
            assertNotNull(activity);
                }
        );
    }


    /**
     * Tests if clicking "capture" correctly opens a capture activity
     */
    @Test
    public void switchTo_IMAGE_CAPTURE_Test() throws InterruptedException {

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        scenario.onActivity(activity -> {
        });

        onView(withId(R.id.take_photo_button)).perform(click());

        Thread.sleep(2000);

        Intents.intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }

}
