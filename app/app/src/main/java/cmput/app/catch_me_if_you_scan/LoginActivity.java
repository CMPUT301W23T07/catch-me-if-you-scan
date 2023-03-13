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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);
    PermissionManager permissionManager;

    private String deviceId;
    private EditText usrName;
    private EditText usrEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        isServicesOK();

        if (userController.getUserByDeviceID(deviceId) != null){
            switchToMain();
        }

        Button signUp = findViewById(R.id.sign_up_button);

        permissionManager = new PermissionManager(LoginActivity.this);
        permissionManager.requestAllPermissions();

        usrName = findViewById(R.id.editTextName);
        usrEmail = findViewById(R.id.editTextEmail);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionManager.hasAllPermissions()) {
                    User user = new User(deviceId, usrName.getText().toString(), usrEmail.getText().toString());
                    userController.create(user);
                    switchToMain();
                } else {
                    permissionManager.requestAllPermissions();
                }

            }
        });


    }

    public void switchToMain(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void isServicesOK() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}


