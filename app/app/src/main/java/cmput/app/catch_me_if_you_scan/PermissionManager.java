package cmput.app.catch_me_if_you_scan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * PermissionManager class provides methods to manage permissions related to location, camera, and storage.
 * It checks for the required permissions and requests them if necessary.
 * It also handles the result of the permission request and shows relevant messages to the user.
 */
public class PermissionManager {

    // Request code for the permission request
    private static final int PERMISSION_REQUEST_CODE = 101;

    // The context in which the permissions are requested
    Context context;

    /**
     * Constructor for PermissionManager.
     *
     * @param context The context in which the permissions are requested.
     */
    public PermissionManager(Context context) {
        this.context = context;
    }

    /**
     * Requests all the necessary permissions for the app.
     * Checks if any of the permissions are already granted, and requests the ones that are not.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestAllPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Checks if all the required permissions for the app are granted or not.
     *
     * @return True if all the required permissions are granted, False otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasAllPermissions() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Handles the result of the permission request.
     * If all the required permissions are granted, the app proceeds.
     * If some of the permissions are not granted, it shows an appropriate message to the user.
     *
     * @param requestCode  The request code used to request permissions.
     * @param permissions  The permissions requested.
     * @param grantResults The results of the permission request.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasLocationPermissions() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            boolean showRationale = false;

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissions[i])) {
                        showRationale = true;
                    } else {
                        showRationale = false;
                    }
                }
            }

            if (allPermissionsGranted) {
                // Do something when all permissions are granted
                // None for the app's case.

                // else is used to catch EVERY case that is not allPermissionsGranted. Including unforeseen cases.
            } else {
                if (showRationale) {
                    // The user denied some permissions but did not check "Don't ask again"
                    // Show an explanation to the user and request the permissions again
                    Toast.makeText(context, "Please grant all the permissions to use the app", Toast.LENGTH_SHORT).show();

                } else {
                    // The user denied some permissions and checked "Don't ask again"
                    // We can either disable some functionality of the app or redirect the user to app settings
                    PermissionDialog dialogFragment = new PermissionDialog();
                    dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "permission_denied_dialog");
                }
            }
        }
    }
}
