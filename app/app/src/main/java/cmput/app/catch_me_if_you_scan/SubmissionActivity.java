package cmput.app.catch_me_if_you_scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    FileOutputStream fos = null;

    File photoFile = null;

    Uri photoURI;
    String photoname;

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error creating file
            }

            try {
                fos = new FileOutputStream(photoFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                }
                }
            }

            if (photoFile != null) {
                // Get the content URI for the file
                photoURI = FileProvider.getUriForFile(this,
                        "cmput.app.catch_me_if_you_scan.fileprovider",
                        photoFile);

                // Add the content URI to the intent as an extra

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Start the camera app with the intent
                takePictureLauncher.launch(takePictureIntent);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create a unique file name for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        photoname = timeStamp + ".jpg";

        // Get the directory where the image file will be saved
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create the File object for the image file
        File imageFile = File.createTempFile(
                timeStamp,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Return the File object for the image file
        return imageFile;
    }

    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    // this is for the background image
                    background_img = findViewById(R.id.background_image);

                    // this will decode the file which contains the image, but the image is rotated
                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                    // This will rotate the image
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    // Paste the Image to the screen
                    background_img.setImageBitmap(rotatedBitmap);

                    //Now we have to compress the picture and save it into compressed_img.
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        rotatedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, stream); // Compress bitmap using RLE compression
//                    }
//                    byte[] compressedBitmap = stream.toByteArray(); // Get the compressed bitmap data as a byte array
//
//
//                    // This will unpack the compressedBitmap
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.RGB_565; // Set the bitmap configuration to ARGB_8888
//                    options.inDither = false;
//                    options.inPreferQualityOverSpeed = true;
//
//
////                    options.inPreferredConfig = Bitmap.CompressFormat.WEBP_LOSSLESS; // Specify the RLE compression format
//
//                    Bitmap decompressed_bitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, options); // Decode the compressed bitmap data
//



                }
            });
}
