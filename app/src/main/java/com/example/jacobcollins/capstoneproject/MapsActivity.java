package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private Button startRunBtn;

    private TextView timerTextView;
    long startTime = 0;

    ArrayList<Run> listOfRuns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startRunBtn = (Button) findViewById(R.id.startRunBtn) ;
        startRunBtn.setOnClickListener(this);

        timerTextView = (TextView)findViewById(R.id.timer);

//        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(mTopToolbar);

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.game_tab) {
            Toast.makeText(MapsActivity.this, "Game Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, GameActivity.class));
        }

        else if (item.getItemId() == R.id.health_insurance_tab) {
            Toast.makeText(MapsActivity.this, "Health Insurance Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, HealthInsuranceActivity.class));
        }

        else if (item.getItemId() == R.id.goals_tab) {
            Toast.makeText(MapsActivity.this, "Goals Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, GoalsActivity.class));
        }

        else if (item.getItemId() == R.id.help_tab) {
            Toast.makeText(MapsActivity.this, "Help Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, HelpActivity.class));
        }
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_favorite) {
//            Toast.makeText(MapsActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = (infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = (infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace(null);
//            placeResult.addOnCompleteListener
//                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                    count = likelyPlaces.getCount();
//                                } else {
//                                    count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//
//                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                            .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                    i++;
//                                    if (i > (count - 1)) {
//                                        break;
//                                    }
//                                }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                                likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
//
//    /**
//     * Displays a form allowing the user to select a place from a list of likely places.
//     */
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));
//            }
//        };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.pick_place)
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerTextView.setText("" + String.format("%02d",mins) + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%02d", milliseconds));
            timerHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onClick(View view) {
        if(view == startRunBtn)
        {
//            if(startRunBtn.getText().toString().trim().contains("Start Run")) Toast.makeText(MapsActivity.this, startRunBtn.getText().toString(), Toast.LENGTH_SHORT).show();
            if(startRunBtn.getText().toString().trim().contains("Start Run")) {
                startRunBtn.setText("End Run");
                Toast.makeText(MapsActivity.this, "Run Started!", Toast.LENGTH_SHORT).show();
                startTime = SystemClock.uptimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);

            }
            else if(startRunBtn.getText().toString().trim().contains("End Run")){
                startRunBtn.setText("Start Run");
                //Toast.makeText(MapsActivity.this, "Run Ended!", Toast.LENGTH_SHORT).show();
                Toast toast= Toast.makeText(getApplicationContext(),
                        "You ran: " + timerTextView.getText() + "!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0 , 0);
                toast.show();
                Run temp = new Run();
                temp.setRunTime(timerTextView);
                listOfRuns.add(temp);
                printListOfRuns(); //print statement remove when done ***********************************************************
                timerHandler.removeCallbacks(timerRunnable);
                timerTextView.setText("" + String.format("%02d", 00) + ":"
                        + String.format("%02d", 00) + ":"
                        + String.format("%02d", 00));
            }
        }
    }

    public String printListOfRuns()
    {
        for(Run r: listOfRuns)
        {
            System.out.println(r.getRunTime() + "\n");
        }

        return "\n";
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        saveData();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData(this);
    }

    public boolean saveData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        //Put the array size in for so when you retrieve the data you know how long the data is
        mEdit1.putInt("ArraySize", listOfRuns.size());

        for(int i=0;i<listOfRuns.size();i++)
        {
            mEdit1.remove("Run" + i);
            mEdit1.putString("Run" + i, listOfRuns.get(i).getRunTime());
        }

        return mEdit1.commit();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        listOfRuns.clear();
        int size = mSharedPreference1.getInt("ArraySize", 0);

        for(int i=0;i<size;i++)
        {
            String tempStr = mSharedPreference1.getString("Run" + i, null);
            Run temp = new Run();
            temp.setRunTime(tempStr);
            listOfRuns.add(temp);
        }
    }

    public boolean eraseData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        //Put the array size in for so when you retrieve the data you know how long the data is
        mEdit1.putInt("ArraySize", 0);

        for(int i=0;i<listOfRuns.size();i++)
        {
            mEdit1.remove("Run" + i);
        }

        return mEdit1.commit();
    }

    public class Run
    {
        int minutes;
        int seconds;
        int milliseconds;

        public Run()
        {
            minutes = 0;
            seconds = 0;
            milliseconds = 0;
        }

        void setRunTime(TextView view)
        {
            String temp = view.getText().toString();
            String valueStr = "";
            boolean onMinutes = true;
            boolean onSeconds = false;
            for(int i=0;i<temp.length();i++)
            {
                if(temp.charAt(i) == ':')
                {
                    if(onMinutes)
                    {
                        minutes = Integer.parseInt(valueStr);
                        onMinutes = false;
                        onSeconds = true;
                        valueStr = "";
                    }

                    else if(onSeconds)
                    {
                        seconds = Integer.parseInt(valueStr);
                        valueStr = temp.substring(i+1,i+3);
                        milliseconds = Integer.parseInt(valueStr);
                    }
                }
                else
                    valueStr += temp.charAt(i);
            }
        }

        void setRunTime(String str)
        {
            String temp = str;
            String valueStr = "";
            boolean onMinutes = true;
            boolean onSeconds = false;
            for(int i=0;i<temp.length();i++)
            {
                if(temp.charAt(i) == ':')
                {
                    if(onMinutes)
                    {
                        minutes = Integer.parseInt(valueStr);
                        onMinutes = false;
                        onSeconds = true;
                        valueStr = "";
                    }

                    else if(onSeconds)
                    {
                        seconds = Integer.parseInt(valueStr);
                        valueStr = temp.substring(i+1,i+3);
                        milliseconds = Integer.parseInt(valueStr);
                    }
                }
                else
                    valueStr += temp.charAt(i);
            }
        }

        String getRunTime()
        {
            return "" + String.format("%02d",minutes) + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%02d", milliseconds);
        }
    }
}