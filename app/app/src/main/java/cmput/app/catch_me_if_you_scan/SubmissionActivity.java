package cmput.app.catch_me_if_you_scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.hash.HashCode;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class activity is the place where we retrieve the monster attributes (score, name, and monster picture)
 * This class will retireve the location of the scanned monster.
 * This class will also able to take pictures and the user will be able to delete pictures if they want to
 * There is a submit button which will package everything and put it into the database
 */
public class SubmissionActivity extends AppCompatActivity {
    private double longitude;
    private double latitude;
    // All Buttons and Views
    Button photoButton;
    FloatingActionButton deletePhotoButton;
    Button submitMonsterButton;
    Switch coordinateSwitch;
    ImageView MonsterImageView;
    ImageView backgroundImg;
    TextView monsterNameView;
    TextView monsterScoreView;

    // This is the image that the user has taken
    Bitmap bigImage;
    // This is responsible for making the output stream
    FileOutputStream fos = null;
    File photoFile = null;
    Uri photoURI;
    // Firebase
    FirebaseFirestore storage = FirebaseFirestore.getInstance();

    // Monster and monster controller
    Monster thisMonster;
    MonsterController thisMonsterController;
    VisualSystem submissionVisual;

    // Getting the device ID This is to check if the code has already been scanned
    private String deviceId;
    private UserController userController = new UserController(storage);
    private User currentUser;

    /**
     * This works with the photo taker. Within, the photo converted to a clear image.
     * Then it will put the adjusted/clear image onto the background.
     * bigImage contain the clear picture and this will be adjusted
     */
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

                    // This will blur the bigImage
                    BlurPhoto blurredPhoto = new BlurPhoto(bigImage);
                    backgroundImg.setImageBitmap(blurredPhoto.getBlurredImage());

                    // Put the rotated image onto the background
                    backgroundImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ////////////////////////////////////////////////////////////////////////////////
                }
            });

    /**
     * This will create the initial page. It will contain the Monster picture, score and name.
     * Then from here the user can take picture, delete picture, turn the geolocation switch on and off.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Initializes the user's location of where they scanned the code
        InitializeCurrentLocation();

        // Create the monster based on current info. Ends activity if monster has been scanned before
        InitializeMonster();

        // Find the view IDs
        InitializeViews();

        // Display Monster Information On Screen
        UpdateViews();

        // Waits for any user interaction
        StartListeners();
    }

    /**
     * This function is responsible taking the environment picture. It will Create a file to store
     * and we will put the photo onto the file.Then it will launch the camera
     */
    // This will create the image name based on the time and date
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error creating file
            }

            // Attempt to create a new FileOutputStream with the created image file
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

            // This code block is checking if the variable 'photoFile' is not null.
            // If it is not null, it continues with the following operations to start the camera app and take a picture.
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

    /**
     * This function is responsible for creating the imageFile name and it will return the File type
     * with the unique file name
     */
    // Used in conjunction with dispatchTakePictureIntent
    private File createImageFile() throws IOException {
        // Create a unique file name for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

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

    /**
     * This function is responsible for delete the environment photo that has been taken.
     * This function will also check if the picture was taken or not as well.
     * big_image will be null in the end
     */
    private void deletePhotoButtonOnClick(View view) {
        // Check if the pictureBitmap exists
        if (bigImage != null) {
            // Delete the picture from the device storage

            if (photoFile.exists()) {
                boolean photoIsDeleted = photoFile.delete();
                if (photoIsDeleted) {
                    // Set pictureBitmap to null and clear the ImageView or any other view
                    bigImage = null;
                    backgroundImg.setImageResource(R.color.off_white);

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

    // This function is responsible for the geoLocation switch
    private void onSwitchClick(View view){
        // We check if the user turned on the geo Location and we will construct the monster here
        if (coordinateSwitch.isChecked()){
            thisMonster.setLocations(latitude,longitude);
            thisMonster.setLocationEnabled(true);
            Toast.makeText(this, "COORDINATE ON", Toast.LENGTH_SHORT).show();
        }
        else{
            thisMonster.setLocations(0.0,0.0);
            thisMonster.setLocationEnabled(false);
            Toast.makeText(this, "COORDINATE OFF", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function is the submit button. Once the user has finished editing their preferences. We will add to the
     * database and we will exit to the MainActivity
     */
    private void submit() {

        PostProcess();
        // Put the MONSTER INTO THE DATABASE
        thisMonsterController = new MonsterController(storage);
        final boolean[] success = {false};
        thisMonsterController.create(thisMonster);
        userController.addMonster(currentUser.getName(), thisMonsterController.getMonsterDoc(thisMonster.getHashHex()));
        // Go back to MainActivity
        finish();
    }
    /**
     * This function processes any photo used prior to submission*/
    private void PostProcess(){
        // This is for storing the compressed image
        byte[] envString = new byte[0];

        // If the user did take a picture and we have a picture to compress. we will resize it and
        // compress it. Then put it into the database!!
        // envString holds the compressed Photo
        if (bigImage != null) {
            // We are resizing the image before we compress
            bigImage = Bitmap.createScaledBitmap(bigImage, 480, 640, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bigImage.compress(Bitmap.CompressFormat.JPEG, 90, stream); // Compress bitmap using JPEG compression
            envString = stream.toByteArray(); // Get the compressed bitmap data as a byte array

        }
        thisMonster.setEnvPhoto(envString);
    }

    /**
     * Starts all the necessary listeners for user interactions
     */
    private void StartListeners(){
        // Buttons and their onClickListeners
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        // Button is will delete the taken photo
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {deletePhotoButtonOnClick(v);
            }
        });
        // This switch will is for turning the geolocation on and off
        coordinateSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {onSwitchClick(v);

            }
        });

        // This button will package all the attributes to the monster database
        submitMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    /**
     * Updates all the views using monster object information
     */
    private void UpdateViews(){
        submissionVisual = new VisualSystem(thisMonster.getHash(), 200, 9);
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
    }

    /**
     * Initializes the monster object that will be used throughout the process
     * Also includes the check for duplicate scans
     */
    private void InitializeMonster(){
        // Get the intents and message will hold the string of the decoded qr/code
        String message = getIntent().getStringExtra("Code name");
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Getting the user based on the devide ID
        currentUser = userController.getUserByDeviceID(deviceId);
        // Monster create
        thisMonster = new Monster(message, latitude, longitude, null);
        boolean exist = currentUser.checkIfHashExist(thisMonster.getHashHex());
        if (exist) {
            Toast.makeText(this, "This was already scanned before", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * This function is responsible for getting the user's coordinate when they enter this activity.
     * It will check the permission.
     * It will assign longitude and latitude.
     */
    private void InitializeCurrentLocation() {
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

    /**
     * Starts all the views.
     */
    private void InitializeViews(){
        photoButton = findViewById(R.id.take_photo_button);
        deletePhotoButton = findViewById(R.id.cancel_photo_button);
        coordinateSwitch = findViewById(R.id.geoLocation_switch);
        MonsterImageView = findViewById(R.id.monsterImageView);
        monsterScoreView = findViewById(R.id.MonsterScoreTextView);
        monsterNameView = findViewById(R.id.MonsterNameTextView);
        submitMonsterButton = findViewById(R.id.SubmitButton);
    }
}