package cmput.app.catch_me_if_you_scan;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.TextView;

@RunWith(AndroidJUnit4.class)
public class PermissionDialogTest {

    @Test
    public void testDialogIsDisplayed() {
        // Launch the activity
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        // Wait for the activity to be launched and displayed
        activityScenario.onActivity(activity -> {
            // Launch the fragment in the activity's container
            FragmentScenario<PermissionDialog> fragmentScenario = launchInContainer(PermissionDialog.class, null);

            // Wait for the fragment to be launched and displayed
            fragmentScenario.onFragment(fragment -> {
                // Verify that the dialog is not null
                Dialog dialog = fragment.getDialog();
                assertNotNull(dialog);

                // Verify that the message in the dialog is correct
                String expectedMessage = "This app requires the storage, camera and location permission.\n\nPlease manually grant the permission in the app settings.";
                String actualMessage = ((TextView) dialog.findViewById(android.R.id.message)).getText().toString();
                assertEquals(expectedMessage, actualMessage);
            });
        });
    }
}
