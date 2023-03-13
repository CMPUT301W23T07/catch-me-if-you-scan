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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.HashCode;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class activity is the place where we retrieve the monster attributes (score, name, and monster picture)
 * This class will retireve the location of the scanned monster.
 * This class will also able to take pictures and the user will be able to delete pictures if they want to
 * There is a submit button which will package everything and put it into the database
 */
public class SubmissionActivity extends AppCompatActivity {
    // This locationManager is essential to get the user's location when they scan the monster.
    // the coordinate is saved into longitude and latitude
    private LocationManager locationManager;
    private double longitude;
    private double latitude;

    // message will contain the message contained in the qr code/ barcode. Not the hash
    private String message;

    // All Buttons and Views
    Button photoButton;
    Button deletePhotoButton;
    Button submitMonsterButton;
    Switch coordinateSwitch;
    ImageView MonsterImageView;
    ImageView backgroundImg;
    TextView monsterNameView;
    TextView monsterScoreView;


    // This is the image that the user has taken
    Bitmap bigImage;

    //
    FileOutputStream fos = null;
    File photoFile = null;
    Uri photoURI;
    String photoName;
    boolean photoIsDeleted;
    FirebaseFirestore storage;

    Monster thisMonster;
    MonsterController thisMonsterController;
    VisualSystem submissionVisual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Get the intents and message will hold the string of the decoded qr/code
        message = getIntent().getStringExtra("Code name");


        // Get's the user's location of where they scanned the code
        getCurrentLocation();

        // Monster create
        thisMonster = new Monster(message, latitude, longitude, null);
        submissionVisual = new VisualSystem(thisMonster.getHash(), 200, 9);


        // Find the view IDs
        photoButton = findViewById(R.id.take_photo_button);
        deletePhotoButton = findViewById(R.id.cancel_photo_button);
        coordinateSwitch = findViewById(R.id.geoLocation_switch);
        MonsterImageView = findViewById(R.id.monsterImageView);
        monsterScoreView = findViewById(R.id.MonsterScoreTextView);
        monsterNameView = findViewById(R.id.MonsterNameTextView);
        submitMonsterButton = findViewById(R.id.SubmitButton);


        // Monster Post
        monsterNameView.setText(thisMonster.getName());
        monsterScoreView.setText(Integer.toString(thisMonster.getScore()));
        MonsterImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                MonsterImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                HashCode hash = thisMonster.getHash();
                submissionVisual.generate(20);
                MonsterImageView.setImageBitmap(submissionVisual.getBitmap());
                return true;
            }
        });

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


        submitMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
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
        photoName = timeStamp + ".jpg";

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
                    backgroundImg = findViewById(R.id.background_image);

                    // this will decode the file which contains the image, but the image is rotated
                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                    // This will rotate the image
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bigImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    // Put the rotated image onto the background
                    backgroundImg.setImageBitmap(bigImage);
                    ////////////////////////////////////////////////////////////////////////////////
                }
            });

    // Code for the delete photo button
    public void deletePhotoButtonOnClick(View view) {
        // Check if the pictureBitmap exists
        if (bigImage != null) {
            // Delete the picture from the device storage

            if (photoFile.exists()) {
                photoIsDeleted = photoFile.delete();
                if (photoIsDeleted) {
                    // Set pictureBitmap to null and clear the ImageView or any other view
                    bigImage = null;
                    backgroundImg.setImageResource(android.R.color.background_light);

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

        String envString;
        if (bigImage != null) {
            // We are resizing the image before we compress
            int originalWidth = bigImage.getWidth();
            int originalHeight = bigImage.getHeight();

            // Calculate the new dimensions for the resized Bitmap
            int newWidth = (int) Math.floor(originalWidth * 0.6);
            int newHeight = (int) Math.floor(originalHeight * 0.6);

            // Resize the original Bitmap by making it smaller by 30%
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bigImage, newWidth, newHeight, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); // Compress bitmap using RLE compression
            byte[] compressedBitmap = stream.toByteArray(); // Get the compressed bitmap data as a byte array

            envString = new String(compressedBitmap, StandardCharsets.UTF_8);
        }
        else{
            envString = null;
        }

        if(coordinateSwitch.isChecked()) {
            thisMonster = new Monster(message, latitude, longitude, envString);
        } else {
            thisMonster = new Monster(message, null, null, envString);
        }

        storage = FirebaseFirestore.getInstance();

        // Put the MONSTER INTO THE DATABASE
        thisMonsterController = new MonsterController(storage);
        thisMonsterController.create(thisMonster);

        // Go back to MainActivity
        finish();

    }


}