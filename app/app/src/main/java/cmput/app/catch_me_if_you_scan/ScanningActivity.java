package cmput.app.catch_me_if_you_scan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanningActivity extends AppCompatActivity {

    Button scan_button;
    private String message;

    private LocationManager locationManager;


    private String Longitude;
    private String Latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        System.out.println("\n\n\nPhase 2\n\n\n");


//        // Related to Location
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                Longitude = String.valueOf(location.getLongitude());
//                Latitude = String.valueOf(location.getLatitude());
//            }
//        };
//
//        if ((ContextCompat.checkSelfPermission(ScanningActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
//                (ContextCompat.checkSelfPermission(ScanningActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(ScanningActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);


        // Related to the scan button
        scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(v ->
        {
            scanCode();
        });
    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {

        // If the capture was successfull. make a alert dialog for it.
        if ((result.getContents() != null))
        {
            message = result.getContents();

            Intent intent = new Intent(ScanningActivity.this, SubmissionActivity.class);
            intent.putExtra("Code name", message);

            startActivity(intent);

//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("Result");
//
//            builder.setMessage(result.getContents() + "\nLatitude: " + Latitude + "\nLongitude: " + Longitude);
//
//            message = result.getContents();
//            System.out.println(message);
//
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                }
//            }).show();
        }
    });


}