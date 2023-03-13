package cmput.app.catch_me_if_you_scan;

import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SubmissionActivityTest {

    private ActivityScenario<SubmissionActivity> scenario;

    /**
     * Set up the test environment by launching the SubmissionActivity.
     */
    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(SubmissionActivity.class);
    }

    /**
     * Test the creation of the SubmissionActivity by verifying that the activity is created.
     */
    @Test
    public void testActivityCreation() {

        // Verify that the activity is created
        scenario.onActivity(activity -> assertNotNull(activity));
    }

    /**
     * Close the scenario after the test is completed.
     */
    @After
    public void tearDown() {
        scenario.close();
    }

}