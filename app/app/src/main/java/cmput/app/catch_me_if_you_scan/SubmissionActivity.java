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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SubmissionActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private String message;
    Button photoButton;
    Button deletePhotoButton;
    Switch coordinateSwitch;
    Bitmap big_image = null;
    ImageView background_img;
    FileOutputStream fos = null;
    File photoFile = null;
    Uri photoURI;
    String photoname;
    boolean photoIsDeleted;
    FirebaseFirestore storage;

    Monster thisMonster;
    MonsterController thisMonsterController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Get the intents and message will hold the string of the decoded qr/code
        message = getIntent().getStringExtra("Code name");


        // Get's the user's location of where they scanned the code
        getCurrentLocation();

        // Find the view IDs
        photoButton = findViewById(R.id.take_photo_button);
        deletePhotoButton = findViewById(R.id.cancel_photo_button);
        coordinateSwitch = findViewById(R.id.geoLocation_switch);

        // Buttons and their onClickListeners
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {deletePhotoButtonOnClick(v);
            }
        });
        coordinateSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {onSwitchClick(v);

            }
        });






    }

    // This function is responsible for the geoLocation switch
    public void onSwitchClick(View view){
        if (coordinateSwitch.isChecked()){
            Toast.makeText(this, "COORDINATE ON", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "COORDINATE OFF", Toast.LENGTH_SHORT).show();
        }
    }

    ////// This is responsible for getting the location/////////////////////////////////////////////
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // This will double check the permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // This will get the user's coordinate location and save the location onto latitude and longitude
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });
    }

    ////// This is responsible for taking the environment picture //////////////////////////////////
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
                photoURI = FileProvider.getUriForFile(this,"cmput.app.catch_me_if_you_scan.fileprovider", photoFile);
                // Add the content URI to the intent as an extra
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Start the camera app with the intent
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    // This will create the image name based on the time and date
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

    // This works with the photo taker. Within, the photo converted to a clear image.
    // Then it will put the adjusted/clear image onto the background.
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
                    big_image = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    // Put the rotated image onto the background
                    background_img.setImageBitmap(big_image);
                    ////////////////////////////////////////////////////////////////////////////////
                }
            });

    // Code for the delete photo button
    public void deletePhotoButtonOnClick(View view) {
        // Check if the pictureBitmap exists
        if (big_image != null) {
            // Delete the picture from the device storage

            if (photoFile.exists()) {
                photoIsDeleted = photoFile.delete();
                if (photoIsDeleted) {
                    // Set pictureBitmap to null and clear the ImageView or any other view
                    big_image = null;
                    background_img.setImageResource(android.R.color.background_light);

                    // ...
                } else {
                    // Handle the case where the file couldn't be deleted
                    Toast.makeText(this, "We were not able to delete the file", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where the file doesn't exist
                Toast.makeText(this, "The file does not exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where pictureBitmap is null
            Toast.makeText(this, "Please take a picture", Toast.LENGTH_SHORT).show();
        }
    }

    // This is the Submit button function///////////////////////////////////////////////////////////
    public void submit() {
        // Create the MONSTER

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        big_image.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Compress bitmap using RLE compression
        byte[] compressedBitmap = stream.toByteArray(); // Get the compressed bitmap data as a byte array

        String envString = new String(compressedBitmap, StandardCharsets.UTF_8);

        if(coordinateSwitch.isChecked()) {
            thisMonster = new Monster(message, latitude, longitude, envString);
        } else {
            thisMonster = new Monster(message, null, null, envString);
        }

        storage = FirebaseFirestore.getInstance();

        // Put the MONSTER INTO THE DATABASE
        thisMonsterController = new MonsterController(storage);
        thisMonsterController.create(thisMonster);
    }



}


//////////////////////////////////////// DO NOT DELETE THIS ////////////////////////////////////////
//    // This is saved for when the user clicks the submit button
//    //Now we have to compress the picture and save it into compressedBitmap.
    // Big image is the Bitmap and it is the very clear picture
//    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//    big_image.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Compress bitmap using RLE compression
//    byte[] compressedBitmap = stream.toByteArray(); // Get the compressed bitmap data as a byte array
//
//    // Upload the compressed image data to Firebase Storage
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    StorageReference imagesRef = storageRef.child("images/compressed.png");
//    UploadTask uploadTask = imagesRef.putBytes(compressedBitmap);
//
//    // Download the compressed image data from Firebase Storage
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    StorageReference imagesRef = storageRef.child("images/compressed.png");
//    final long ONE_MEGABYTE = 1024 * 1024;
//    imagesRef.getBytes(ONE_MEGABYTE)
//    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//@Override
//public void onSuccess(byte[] compressedImageData) {
//        // Convert compressed image data to Bitmap
//        Bitmap decompressedBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length);
//
//        // Display decompressed image in ImageView
//        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setImageBitmap(bitmap);
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        // Handle failed download of compressed image data
//        }
//        });