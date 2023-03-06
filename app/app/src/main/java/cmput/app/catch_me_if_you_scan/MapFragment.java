package cmput.app.catch_me_if_you_scan;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mView;
    private GoogleMap map;
    private PermissionManager permissions;
    private Location userLocation;
    private FusedLocationProviderClient mLocationClient;

    private static final float DEFAULT_ZOOM = 15f;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        permissions = new PermissionManager(getActivity());

        mView = v.findViewById(R.id.map_view);
        mView.onCreate(savedInstanceState);

        mView.getMapAsync(this);

        return v;
    }

    //Suppressed the warning because the permissions for this are checked in the PermissionManager class
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5461, 113.4937), DEFAULT_ZOOM));
        loadMonstersInRegion();
        if (permissions.hasLocationPermissions()) {
            map.setMyLocationEnabled(true);
            getCurrentLocation();
        }
    }

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

    private void loadMonstersInRegion() {
        //TEMPORARY CODE FOR SETTING THE MARKER SETTINGS WILL FIX LATER
        int height = 100;
        int width = 100;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.diamond);
        Bitmap b = bitMapDraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        //HARDCODED MONSTERS
        Double[][] monsters = {{53.527275, -113.523964, 256.0}, {53.527471, -113.526879, 9237.0}};

        int i = 0;
        for (i = 0; i < monsters.length; i++) {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(monsters[i][0], monsters[i][1]))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .title(monsters[i][2].toString());
            map.addMarker(options);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mView.onLowMemory();
    }
}