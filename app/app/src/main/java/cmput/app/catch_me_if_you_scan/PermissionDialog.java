package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

/**
 * This class represents a dialog fragment that is used to request user permissions for the app.
 * The dialog is used to request the user to grant storage, camera and location permissions.
 * When the user clicks on the OK button, the app settings are opened and the user can grant the required permissions.
 * When the user clicks on the CANCEL button, the dialog is dismissed.
 * This class must only be used if the user has chosen "Don't Ask Again" for all permission requests.
 */
public class PermissionDialog extends DialogFragment {

    /**
     * This method creates a new instance of the PermissionDialog class.
     *
     * @param savedInstanceState A Bundle object containing the saved state of the activity.
     * @return A new instance of the PermissionDialog class.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the message to be displayed in the dialog
        builder.setMessage("This app requires the storage, camera and location permission.\n\nPlease manually grant the permission in the app settings.")

                // Set the positive button to open the app settings when clicked
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open app settings
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })

                // Set the negative button to dismiss the dialog when clicked
                .setNegativeButton("CANCEL", (dialog, id) -> {
                    // Dismiss the dialog
                    dialog.cancel();
                });

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Customize the text color of the CANCEL button to red
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(((AlertDialog) dialog).getContext(), android.R.color.holo_red_light));
            }
        });

        // Return the dialog
        return dialog;
    }
}