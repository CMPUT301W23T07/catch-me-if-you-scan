package cmput.app.catch_me_if_you_scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmissionActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private String message;
    private String currentPhotoPath;
    TextView info_Text;
    TextView latititude_Text;
    TextView longitutde_Text;

    Button photoButton;
    Bitmap compressed_img;

    ImageView background_img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Find the view IDs
//        latititude_Text = findViewById(R.id.Latitude);
//        longitutde_Text = findViewById(R.id.Longitude);
        photoButton = findViewById(R.id.take_photo_button);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        // Get the intents
        message = getIntent().getStringExtra("Code name");

        getCurrentLocation();
    }

    ////// This is responsible for getting the location/////////////////////////////////////////////
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
//                            updateLocationViews();
                        }
                    }
                });
    }
    // This will only update the location views. This is mainly for testing purposes////////////////
//    private void updateLocationViews() {
//        TextView latitude_display = findViewById(R.id.Latitude);
//        latitude_display.setText(Double.toString(latitude));
//
//        TextView longitude_display = findViewById(R.id.Longitude);
//        longitude_display.setText(Double.toString(longitude));
//    }

    // This is responsible for the picture taking //////////////////////////////////////////////////
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
//            takePictureLauncher.launch(takePictureIntent);
//    }
//    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                    Bundle extras = result.getData().getExtras();
//                    Bitmap image_taken = (Bitmap) extras.get("data");
//
//                    // this is for the background image
//                    background_img = findViewById(R.id.background_image);
//                    background_img.setImageBitmap(image_taken);
//
//                    //Now we have to compress the picture
//                    int width = image_taken.getWidth();
//                    int height = image_taken.getHeight();
//                    float aspectRatio = (float) width / (float) height;
//                    int newWidth = 800;
//                    int newHeight = Math.round(newWidth / aspectRatio);
//
//                    // Resize the bitmap to the new dimensions
//                    compressed_img = Bitmap.createScaledBitmap(image_taken, newWidth, newHeight, false);
                //              }
            //});
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error creating file
            }

            if (photoFile != null) {
                // Get the content URI for the file
                Uri photoURI = FileProvider.getUriForFile(this,
                        "cmput.app.catch_me_if_you_scan.fileprovider",
                        photoFile);
//                // Add the content URI to the intent as an extra
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                // Start the camera app with the intent
//                takePictureLauncher.launch(takePictureIntent);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create a unique file name for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // Get the directory where the image file will be saved
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create the File object for the image file
        File imageFile = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Return the File object for the image file
        return imageFile;
    }

    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    Bundle extras = result.getData().getExtras();
                    Bitmap image_taken = (Bitmap) extras.get("data");

                    // this is for the background image
                    background_img = findViewById(R.id.background_image);
                    background_img.setImageBitmap(image_taken);

                    //Now we have to compress the picture
                    int width = image_taken.getWidth();
                    int height = image_taken.getHeight();
                    float aspectRatio = (float) width / (float) height;
                    int newWidth = 800;
                    int newHeight = Math.round(newWidth / aspectRatio);

                    // Resize the bitmap to the new dimensions
                    compressed_img = Bitmap.createScaledBitmap(image_taken, newWidth, newHeight, false);
                }
            });
}
