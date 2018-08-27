package gitmad.gitmadheatmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.List;

import gitmad.gitmadheatmap.firebase.FbAuth;
import gitmad.gitmadheatmap.firebase.FbDatabase;

public class ActivityHeatMap extends AppCompatActivity implements OnMapReadyCallback {

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    // Heatmap variables.
    private GoogleMap googleMap;
    private HeatmapTileProvider tileProvider;
    private TileOverlay tileOverlay;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionsGranted;

    private Location lastKnownLocation;
    private List<LatLng> locations;

    // Database.
    private FbDatabase database;

    // DrawerLayout variables.
    private DrawerLayout drawerLayout;
    private Intent drawerIntent;
    private boolean logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new FbDatabase();

        setContentView(R.layout.activity_maps);

        // Setup toolbar.
        Toolbar toolbar = findViewById(R.id.loggedIn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);

        // Manage DrawerLayout.
        setupDrawer();

        // Provide quick access to the device's current place.
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setUpMap();
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        getLocationsAndAddHeatMap();
    }

    /**
     * Retrieves all locations from our database.
     * If there are locations, then we create/add our heatmap on top of our existing map object.
     */
    private void getLocationsAndAddHeatMap() {
        database.getLocations(new LocationCallback() {
            @Override
            public void onFinish(List<LatLng> locations) {
                ActivityHeatMap.this.locations = locations;
                if (ActivityHeatMap.this.locations.size() == 0) {
                    return;
                }
                addHeatMap();
            }
        });
    }

    /**
     * Create a heatmap overlay on top of our map instance.
     */
    private void addHeatMap() {
        // Create the gradient.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create the tile provider.
        tileProvider = new HeatmapTileProvider.Builder()
                .data(locations)
                .gradient(gradient)
                .build();

        // Add the tile overlay to the map.
        tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of location permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionsGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionsGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Update the map UI based on location permissions and if a map instance is created.
     */
    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionsGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionsGranted) {
                Task locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = (Location) task.getResult();

                            // If lastKnownLocation is null ( usually happens on emulator ) return to avoid error.
                            if (lastKnownLocation == null) {
                                return;
                            }

                            // Move the camera to the desired position
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        } else {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Add DrawerLayout listener and add navigation view listener.
     */
    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);

        // Set this to false. We use this boolean later to mark that the user has tried to logout and we should fulfill this request.
        // This is used similarly to the drawerIntent variable. It prevents lag with the drawer screen.
        logout = false;

        // We initialize this listener mainly for its ability to remove lag when changing activities.
        // By waiting for the drawer to fully close, the animation looks smooth and cleanly executed.
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerIntent != null) {
                    startActivity(drawerIntent);
                } else if (logout) {
                    FbAuth mAuth = new FbAuth();
                    mAuth.signUserOutAndReturnToLogin();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set our map option within the nav drawer as selected.
        navigationView.setCheckedItem(R.id.nav_map_option);

        // A listener that handles onClicks for menu items in the navigation drawer.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home_option:
                                intent = new Intent(AppContext.getContext(), ActivityUserLoggedIn.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra(Integer.toString(R.string.intent_menu_item), "nav_home_option");
                                drawerIntent = intent;
                                break;
                            case R.id.nav_logout_option:
                                logout = true;
                                break;
                            case R.id.nav_settings_option:
                                intent = new Intent(AppContext.getContext(), ActivityUserLoggedIn.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra(Integer.toString(R.string.intent_menu_item), "nav_settings_option");
                                drawerIntent = intent;
                                break;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    /**
     * Handles all toolbar interactions.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
