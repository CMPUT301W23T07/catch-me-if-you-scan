package cmput.app.catch_me_if_you_scan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    ScanningManager scanningManager = new ScanningManager(this);
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
                    scanningManager.scanCode();
                    break;
                case R.id.profile_nav:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment);
        ft.commit();
    }
}