package cmput.app.catch_me_if_you_scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SubmissionActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private String message;
    TextView info_Text;
    TextView latititude_Text;
    TextView longitutde_Text;
    ImageButton photoButton;
    Bitmap compressed_img;

    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap image_taken = (Bitmap) extras.get("data");

                    //Now we have to compress the picture
                    int width = image_taken.getWidth();
                    int height = image_taken.getHeight();
                    float aspectRatio = (float) width / (float) height;
                    int newWidth = 800;
                    int newHeight = Math.round(newWidth / aspectRatio);

                    // Resize the bitmap to the new dimensions
                    compressed_img = Bitmap.createScaledBitmap(image_taken, newWidth, newHeight, false);

                    photoButton = findViewById(R.id.photo_button);
                    photoButton.setImageBitmap(image_taken);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Find the view IDs
        info_Text = findViewById(R.id.QRinfo);
        latititude_Text = findViewById(R.id.Latitude);
        longitutde_Text = findViewById(R.id.Longitude);
        photoButton = findViewById(R.id.photo_button);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        // Get the intents
        message = getIntent().getStringExtra("Code name");
        info_Text.setText(message);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            // Update the latitude and longitude values here
                            updateLocationViews();
                        }
                    }
                });

    }


    private void updateLocationViews() {
        TextView latitude_display = findViewById(R.id.Latitude);
        latitude_display.setText(Double.toString(latitude));

        TextView longitude_display = findViewById(R.id.Longitude);
        longitude_display.setText(Double.toString(longitude));
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            takePictureLauncher.launch(takePictureIntent);
    }

}
