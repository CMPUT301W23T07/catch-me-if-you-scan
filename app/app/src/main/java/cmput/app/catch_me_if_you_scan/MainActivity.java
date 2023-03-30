/**
 * The MainActivity class is responsible for managing the UI elements and navigation of the app.
 * It contains methods to replace fragments, switch to the ScanningActivity, and initialize the Bottom Navigation Bar.
 * The OnMapReadyCallback interface is implemented for the MapFragment to handle the Google Maps API.
 */
package cmput.app.catch_me_if_you_scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    /**
     * The ScanningManager instance that will be used to start the ScanningActivity.
     */
    private ScanningManager scanningManager = new ScanningManager(this);


    /**
     * Initializes the UI elements of the activity, such as the Bottom Navigation Bar and the starting fragment.
     * It also sets up the listeners for the Bottom Navigation Bar items.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        replaceFragment(new ProfileFragment());

        BottomNavigationView navbar = findViewById(R.id.bottom_navigation_bar);
        navbar.getMenu().getItem(3).setChecked(true);

        navbar.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.leaderboard_nav:
                    replaceFragment(new LeaderboardFragment());
                    break;
                case R.id.map_nav:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.camera_nav:
                    switchToScanningActivity();
                    break;
                case R.id.profile_nav:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

    }

    /**
     * Replaces the current fragment with a new one.
     *
     * @param fragment The fragment to be replaced with.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment);
        ft.commit();
    }

    /**
     * Switches to the ScanningActivity.
     */
    private void switchToScanningActivity(){
        scanningManager.scanCode();
    }

}