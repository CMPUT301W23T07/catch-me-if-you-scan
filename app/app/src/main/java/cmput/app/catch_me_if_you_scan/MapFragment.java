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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
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
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mView;
    private GoogleMap map;
    private PermissionManager permissions;
    private Location userLocation;
    private FusedLocationProviderClient mLocationClient;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Integer> rankings = new ArrayList<>();
    private MonsterController mc = new MonsterController(FirebaseFirestore.getInstance());
    private static final float DEFAULT_ZOOM = 15f;
    private ConstraintLayout mapFilter;
    private RadioButton bronze;
    private RadioButton silver;
    private RadioButton gold;
    private RadioButton platinum;
    private RadioButton diamond;


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

        mapFilter = v.findViewById(R.id.map_filter_display);
        mapFilter.setVisibility(v.GONE);
        bronze = v.findViewById(R.id.bronze_radio);
        silver = v.findViewById(R.id.silver_radio);
        gold = v.findViewById(R.id.gold_radio);
        platinum = v.findViewById(R.id.platinum_radio);
        diamond = v.findViewById(R.id.diamond_radio);

        permissions = new PermissionManager(getActivity());

        mView = v.findViewById(R.id.map_view);
        mView.onCreate(savedInstanceState);

        mView.getMapAsync(this);

        initSearchBar(v);

        Button filterButton = v.findViewById(R.id.filter_map_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFilter.setVisibility(v.VISIBLE);
                bronze.setChecked(false);
                silver.setChecked(false);
                gold.setChecked(false);
                platinum.setChecked(false);
                diamond.setChecked(false);
            }
        });

        Button confirmFilters = v.findViewById(R.id.confirm_filter_button);
        confirmFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilters(v);
                mapFilter.setVisibility(v.GONE);
            }
        });

        Button clearFilters = v.findViewById(R.id.clear_filters_button);
        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFilters();
                mapFilter.setVisibility(v.GONE);
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
        map.setOnMarkerClickListener(this);
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
    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadMonsters() {
        int height = 200;
        int width = 200;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.bronze);
        Bitmap b = bitMapDraw.getBitmap();
        Bitmap bronzeMarker = Bitmap.createScaledBitmap(b, width, height, false);
        bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.silver);
        b = bitMapDraw.getBitmap();
        Bitmap silverMarker = Bitmap.createScaledBitmap(b, width, height, false);
        bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.gold);
        b = bitMapDraw.getBitmap();
        Bitmap goldMarker = Bitmap.createScaledBitmap(b, width, height, false);
        bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.platinum);
        b = bitMapDraw.getBitmap();
        Bitmap platinumMarker = Bitmap.createScaledBitmap(b, width, height, false);
        bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.diamond);
        b = bitMapDraw.getBitmap();
        Bitmap diamondMarker = Bitmap.createScaledBitmap(b, width, height, false);

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
                            .position(new LatLng(monsters.get(i).getLocation()[0], monsters.get(i).getLocation()[1]));
                    if (monsters.get(i).getScore() > 150) {
                        rankings.add(5);
                        options.icon(BitmapDescriptorFactory.fromBitmap(diamondMarker));
                    } else if (monsters.get(i).getScore() > 100) {
                        rankings.add(4);
                        options.icon(BitmapDescriptorFactory.fromBitmap(platinumMarker));
                    } else if (monsters.get(i).getScore() > 60) {
                        rankings.add(3);
                        options.icon(BitmapDescriptorFactory.fromBitmap(goldMarker));
                    } else if (monsters.get(i).getScore() > 20) {
                        rankings.add(2);
                        options.icon(BitmapDescriptorFactory.fromBitmap(silverMarker));
                    } else {
                        rankings.add(1);
                        options.icon(BitmapDescriptorFactory.fromBitmap(bronzeMarker));
                    }
                    options.title(monsters.get(i).getHashHex());
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
     * This function closes the filter fragment and gets the desired filters
     * and apply them, the filter includes the ranking of each Monster
     * @param v
     */
    private void getFilters(View v) {

        ArrayList<Integer> tiers = new ArrayList<Integer>();
        if (bronze.isChecked()) {
            tiers.add(1);
        }if (silver.isChecked()) {
            tiers.add(2);
        }if (gold.isChecked()) {
            tiers.add(3);
        }if (platinum.isChecked()) {
            tiers.add(4);
        }if (diamond.isChecked()) {
            tiers.add(5);
        }
        filterMonsterTier(tiers);
    }

    /**
     * This method will remove all filters by setting the tiers list to contain all tiers, and the
     * map will redisplay all markers
     */
    private void removeFilters() {
        ArrayList<Integer> tiers = new ArrayList<Integer>();
        tiers.add(1);
        tiers.add(2);
        tiers.add(3);
        tiers.add(4);
        tiers.add(5);
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
            else {
                markers.get(i).setVisible(true);
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

    /**
     * This is the click listener for the map so that users can click on a monster
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.d("MAP", "User clicked on a monster");
        ViewMonsterFragment nextFrag = new ViewMonsterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", marker.getTitle());
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, nextFrag, "monsterFragment")
                .addToBackStack(null)
                .commit();

        return true;
    }
}