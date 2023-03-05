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

public class PermissionManager {

    private static final int PERMISSION_REQUEST_CODE = 101;
    Context context;

    public PermissionManager(Context context) {
        this.context = context;
    }


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

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasAllPermissions() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
