package cmput.app.catch_me_if_you_scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * This class is for the map fragment which appears when the user clicks on the map nav button
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mView;
    private GoogleMap map;
    private PermissionManager permissions;
    private Location userLocation;
    private FusedLocationProviderClient mLocationClient;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Integer> rankings = new ArrayList<>();
    private MonsterController mc = new MonsterController(FirebaseFirestore.getInstance());
    private static final float DEFAULT_ZOOM = 15f;


    /**
     * This method is called once the view is inflated to implement the rest of the fragments methods
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        rankings.add(2);
        rankings.add(5);

        permissions = new PermissionManager(getActivity());

        mView = v.findViewById(R.id.map_view);
        mView.onCreate(savedInstanceState);

        mView.getMapAsync(this);

        initSearchBar(v);

        Button filterButton = v.findViewById(R.id.filter_map_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }


    /**
     * This method is called once the map is ready to be initialized and created, it will call the
     * methods used for updating the maps UI.
     * @param googleMap
     */
    //Suppressed the warning because the permissions for this are checked in the PermissionManager class
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5461, 113.4937), DEFAULT_ZOOM));
        loadMonsters();
        if (permissions.hasLocationPermissions()) {
            map.setMyLocationEnabled(true);
            getCurrentLocation();
        }
    }

    /**
     * This method will obtain the current location of the user and center the map on it
     */
    //Permissions are checked with the line "if (permissions.hasLocationPermissions())"
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Task<Location> location = mLocationClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    userLocation = (Location) task.getResult();
                    if (userLocation != null) {
                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())));
                    }
                    else {
                        Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * This method will use the MonsterController class to obtain the monsters and display them on
     * the map with an icon
     */
    private void loadMonsters() {
        //TEMPORARY CODE FOR SETTING THE MARKER SETTINGS WILL FIX LATER
        int height = 150;
        int width = 150;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.diamond);
        Bitmap b = bitMapDraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        ArrayList<Monster> monsters = new ArrayList<Monster>();
        Log.d("MAP", "BEFORE CONTROLLER REQUEST");
        monsters = mc.getAllMonsters();
        Log.d("MAP", "AFTER CONTROLLER REQUEST");

        //This loop will cycle through each monster in the array and display it on the map
        if (monsters != null) {
            Log.d("MAP", "MONSTERS ARRAY HAS DATA");
            int i = 0;
            for (i = 0; i < monsters.size(); i++) {
                if (monsters.get(i).getLocationEnabled()) {
                    MarkerOptions options = new MarkerOptions()
                            .position(new LatLng(monsters.get(i).getLocation()[0], monsters.get(i).getLocation()[1]))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title(monsters.get(i).getName());
                    Marker marker = map.addMarker(options);
                    markers.add(marker);
                }
            }
        }
        else {
            Log.d("MAP", "MONSTERS ARRAY IS NULL");
        }
    }

    /**
     * This function is to display the filter fragment for the map and get the desired filters
     * and apply them, the filter includes the ranking of each Monster
     * @param v
     */
    private void getFilters(View v) {
        FragmentManager fragMan = getChildFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        FilterMapFragment filterFragment = new FilterMapFragment();
        fragTrans.add(R.id.map, filterFragment);
        fragTrans.addToBackStack("FILTER");
        fragTrans.commit();

        Intent i = new Intent(getActivity(), FilterMapFragment.class);

        ArrayList<Integer> tiers = i.getIntegerArrayListExtra("TIERS");

        filterMonsterTier(tiers);
    }

    /**
     * This method will filter the displayed monsters to match the provided tier filter array
     * @param tiers
     */
    private void filterMonsterTier(ArrayList<Integer> tiers) {
        for (int i = 0; i < markers.size(); i++) {
            if (!tiers.contains(rankings.get(i))) {
                markers.get(i).setVisible(false);
            }
        }
    }


    /**
     * This function is for creating the async search function for the map, it will take the
     * text entered into the search bar and move the camera to the area that was searched
     *
     * @param v
     *      Takes in an object v of type View
     */
    private void initSearchBar(View v) {
        SearchView search = (SearchView) v.findViewById(R.id.map_search_bar);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Geocoder coder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> listAddress = coder.getFromLocationName(s, 1);
                    if (listAddress.size() > 0) {
                        LatLng coords = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, DEFAULT_ZOOM));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { return false; }
        });
    }

    /**
     * This method is for the map implementation
     */
    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
    }

    /**
     * This method is for pausing of the map
     */
    @Override
    public void onPause() {
        super.onPause();
        mView.onPause();
    }

    /**
     * This method is for destroying of the map
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.onDestroy();
    }

    /**
     * This method is for when the map gets low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mView.onLowMemory();
    }
}