/**
 * The LoginActivity class represents the main entry point of the application and handles the user's
 * login functionality. It checks if the Google Play Services version is up-to-date and requests all necessary
 * permissions from the user before switching to the MainActivity.
 */


package cmput.app.catch_me_if_you_scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);
    PermissionManager permissionManager;

    private String deviceId;
    private EditText usrName;
    private EditText usrEmail;


    /**
     * This method is called when the activity is starting. It initializes the activity, sets the content view,
     * checks for the Google Play Services version and requests all necessary permissions from the user.
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        isServicesOK();

//        checks if user exist in db
        if (userController.getUserByDeviceID(deviceId) != null){
            switchToMain();}


        Button signUp = findViewById(R.id.sign_up_button);

        permissionManager = new PermissionManager(LoginActivity.this);
        permissionManager.requestAllPermissions();

        usrName = findViewById(R.id.editTextName);
        usrEmail = findViewById(R.id.editTextEmail);
        signUp.setOnClickListener(new View.OnClickListener() {

            /**
             * This method is called when the sign up button is clicked. It checks if the user has all necessary
             * permissions and switches to the MainActivity if the user has all permissions. If not, it requests
             * all necessary permissions from the user.
             * @param v the view of the clicked button
             */
            @Override
            public void onClick(View v) {
                if (permissionManager.hasAllPermissions()) {
                    if (!usrName.getText().toString().equals("") && !usrEmail.getText().toString().equals("")) {
                        if (usrName.getText().toString().matches("[^A-Za-z0-9-_]{5,30}")) {
                            Toast.makeText(LoginActivity.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                        }
                        else if (usrEmail.getText().toString().matches("[^a-zA-Z0-9@.]{7,30}")) {
                            Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (userController.getUser(usrName.getText().toString()) == null) {
                                User user = new User(deviceId, usrName.getText().toString(), usrEmail.getText().toString());
                                userController.create(user);
                                switchToMain();
                            } else {
                                Toast.makeText(LoginActivity.this, "This user name is taken", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Please enter a username/email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    permissionManager.requestAllPermissions();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userController.getUserByDeviceID(deviceId) != null){
            switchToMain();}
    }

    /**
     * This method switches to the MainActivity.
     */
    private void switchToMain(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    /**
     * This method checks if the Google Play Services version is up-to-date. If it is, it prints a log statement that the service is working.
     * If not, it checks if the error is user resolvable and displays an error dialog if it is. If not, it displays a toast message.
     */
    private void isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            //Maps connection works
            Log.d(TAG, "isServicesOK: Google play service is working");
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error occurred but it can be resolved easily
            Log.d(TAG, "isServicesOK: the version error can be resolved");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            if (dialog != null) {
                dialog.show();
            }
        }
        else {
            Toast.makeText(this, "You can't make map requests, API cannot connect.", Toast.LENGTH_SHORT).show();
        }
    }



/**
 * This method is called when the permission request has been completed. It forwards the permission results to
 * the permission manager.
 * @param requestCode the request code of the permission request
 * @param permissions the requested permissions
 * @param grantResults the grant results of the requested permissions
 * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}


