package cmput.app.catch_me_if_you_scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

////////////////////////////////////// Removed for testing. Jay/////////////////////////////////////
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Console;

public class MainActivity extends AppCompatActivity {

    Button toScanPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAllPermission();

        toScanPage = findViewById(R.id.tempButtonScan);
        toScanPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("\n\n\nPhase 1\n\n\n");
                Intent intent = new Intent(MainActivity.this, ScanningActivity.class);

                startActivity(intent);
            }
        });


    }




    private void requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA};

            requestPermissions(permissions, 101);
        }
    }


}