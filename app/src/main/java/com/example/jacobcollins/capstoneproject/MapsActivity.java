package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private SensorManager mSensorManager;
    private Sensor stepSensor;

    private LocationManager locationManager;
    private Criteria criteria;
    private String bestProvider;

    double distanceRanInMiles;

//    private Chronometer timer;

    //private LatLng startOfRunCoords = new LatLng();

    private ArrayList<Run> listOfRuns = new ArrayList<>();

    private double steps = 0;

    private SensorEventListener mLightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            float[] values = event.values;
            int value = -1;

            if (values.length > 0)
            {
                value = (int) values[0];
            }

            if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                steps++;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };

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

        startRunBtn = findViewById(R.id.startRunBtn);
        startRunBtn.setOnClickListener(this);

        timerTextView = findViewById(R.id.timer);
        timerTextView.setText("00:00:00");

        mLocationPermissionGranted = false;

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null) {
            stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

//        timer = (Chronometer) findViewById(R.id.timer);
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
        } else if (item.getItemId() == R.id.health_insurance_tab) {
            Toast.makeText(MapsActivity.this, "Health Insurance Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, HealthInsuranceActivity.class));
        } else if (item.getItemId() == R.id.goals_tab) {
            Toast.makeText(MapsActivity.this, "Goals Tab Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, GoalsActivity.class));
        } else if (item.getItemId() == R.id.help_tab) {
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

        boolean locationAccessAllowed = false;
        while (!locationAccessAllowed) {
            // Prompt the user for permission.
            getLocationPermission();
            if (mLocationPermissionGranted) locationAccessAllowed = true;
        }

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
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            //locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    double timeInMilliseconds = 0L;
    double timeSwapBuff = 0L;
    double updatedTime = 0L;

//    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            DecimalFormat df = new DecimalFormat("00");

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            double secs = (int) (updatedTime / 1000);
            double mins = secs / 60;
            secs = secs % 60;
            double milliseconds = (updatedTime % 1000);
//
            timerTextView.setText("" + df.format(mins) + ":"
                    + df.format(secs) + ":"
                    + df.format(milliseconds));
            timerHandler.postDelayed(this, 5);
        }
    };

    @Override
    public void onClick(View view) {
        if (view == startRunBtn) {
//            if(startRunBtn.getText().toString().trim().contains("Start Run")) Toast.makeText(MapsActivity.this, startRunBtn.getText().toString(), Toast.LENGTH_SHORT).show();
            if (startRunBtn.getText().toString().trim().contains("Start Run")) {
                if(mLastKnownLocation != null) {
                    Location myLocation = mLastKnownLocation;
                    LatLng myPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    MarkerOptions currentLocationMarker = new MarkerOptions()
                            .position(myPosition)
                            .title("Start of Run");
                    mMap.addMarker(currentLocationMarker);
                }
                getDeviceLocation();
                startRunBtn.setText("End Run");
                Toast.makeText(MapsActivity.this, "Run Started!", Toast.LENGTH_SHORT).show();
                startTime = SystemClock.uptimeMillis();
//                timer.start();
                timerHandler.postDelayed(timerRunnable, 0);

            } else if (startRunBtn.getText().toString().trim().contains("End Run")) {
                startRunBtn.setText("Start Run");
                if(mLastKnownLocation != null) {
                    Location myLocation = mLastKnownLocation;
                    LatLng myPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    MarkerOptions currentLocationMarker = new MarkerOptions()
                            .position(myPosition)
                            .title("End of Run");
                    mMap.addMarker(currentLocationMarker);
                }

//                DecimalFormat df = new DecimalFormat("0.00");

//                timer.stop();
                distanceRanInMiles = getDistanceRun(steps);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You ran: " + timerTextView.getText() + "!\n" + "You ran: " + String.format("%.2f", distanceRanInMiles) + "miles", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Run temp = new Run();
                temp.setRunTime(timerTextView);
                temp.setDistanceRan(distanceRanInMiles);
                listOfRuns.add(temp);
                printListOfRuns(); //print statement remove when done ***********************************************************
                timerHandler.removeCallbacks(timerRunnable);
                timerTextView.setText("00:00:00");
                steps = 0;

                System.out.println("Run Time: " + temp.getRunTimeInMinutes() + "mins");
                System.out.println("Distance Ran: " + temp.getDistanceRan() + "miles");
//                System.out.println("Distance in Minutes: " + df.format(temp.getRunTimeInMinutes()) + "mins");
//                System.out.println("Distance ran: " + temp.getDistanceRan() + "miles");

                Intent i = new Intent(this, GoalsActivity.class);
                double runTimePerMile = temp.getRunTimeInMinutes()/Double.parseDouble(temp.getDistanceRan());
                System.out.println("Run Time Per Mile: " + runTimePerMile + "/mins");
                i.putExtra("Run Time Per Mile",runTimePerMile);
                i.putExtra("Distance", temp.getDistanceRan());
                startActivity(i);
//                timer.setBase(SystemClock.elapsedRealtime());
            }
        }
    }

    public String printListOfRuns() {
        for (Run r : listOfRuns) {
            System.out.println(r.getRunTime() + "\n" + r.getDistanceRan() + "\n" + r.getDateOfRun() + "\n");
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
    public void onResume() {
        super.onResume();
        loadData(this);
        mSensorManager.registerListener(mLightSensorListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(mLightSensorListener, stepSensor);
    }

    public void saveData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        //Put the array size in for so when you retrieve the data you know how long the data is
        mEdit1.putInt("ArraySize", listOfRuns.size());

        for (int i = 0; i < listOfRuns.size(); i++) {
            mEdit1.remove("Run Time" + i);
            mEdit1.remove("Run Distance" + i);
            mEdit1.remove("Run Date" + i);
            mEdit1.putString("Run Time" + i, listOfRuns.get(i).getRunTime());
            mEdit1.putString("Run Distance" + i, listOfRuns.get(i).getDistanceRan());
            mEdit1.putString("Run Date" + i, listOfRuns.get(i).getDateOfRun());
        }

        mEdit1.apply();
    }

    public void loadData(Context mContext) {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        listOfRuns.clear();
        int size = mSharedPreference1.getInt("ArraySize", 0);

        for (int i = 0; i < size; i++) {
            String tempStr = mSharedPreference1.getString("Run Time" + i, null);
            Run temp = new Run();
            temp.setRunTime(tempStr);
            tempStr = mSharedPreference1.getString("Run Distance" + i, null);
            temp.setDistanceRan(Double.parseDouble(tempStr));
            tempStr = mSharedPreference1.getString("Run Date" + i, null);
            temp.setDateOfRun(tempStr);
            listOfRuns.add(temp);
        }
    }

    //function to determine the distance run in kilometers using average step length for men and number of steps
    public double getDistanceRun(double stepsRan) {
        stepsRan = 2000; //delete this hardcoded for debuggggggggggggggggingggggggggggggggg
        double distance = (stepsRan * 78) / (double) 100000;
        distance /= 1.609344;
        return distance;
    }

    public static class Run implements Parcelable
    {
        double minutes;
        double seconds;
        double milliseconds;
        double distanceInMiles;
        String date;

        public Run()
        {
            minutes = 0;
            seconds = 0;
            milliseconds = 0;
            distanceInMiles = 0;
            date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        }

        void setDistanceRan(double distance)
        {
            distanceInMiles = distance;
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
                        minutes = Double.parseDouble(valueStr);
                        onMinutes = false;
                        onSeconds = true;
                        valueStr = "";
                    }

                    else if(onSeconds)
                    {
                        seconds = Double.parseDouble(valueStr);
                        valueStr = temp.substring(i+1,i+3);
                        milliseconds = Double.parseDouble(valueStr);
                        onSeconds = false;
                    }
                }
                else
                    valueStr += temp.charAt(i);
            }
        }

        void setDateOfRun(String newDate)
        {
            date = newDate;
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
                        minutes = Double.parseDouble(valueStr);
                        onMinutes = false;
                        onSeconds = true;
                        valueStr = "";
                    }

                    else if(onSeconds)
                    {
                        seconds = Double.parseDouble(valueStr);
                        valueStr = temp.substring(i+1,i+3);
                        milliseconds = Double.parseDouble(valueStr);
                        onSeconds = false;
                    }
                }
                else
                    valueStr += temp.charAt(i);
            }
        }

        String getRunTime()
        {
//            DecimalFormat df = new DecimalFormat("00");
//            System.out.println("Mins: " + df.format(minutes));
//            System.out.println("Seconds: " + df.format(seconds));
//            System.out.println("MS: " + df.format(milliseconds));
            return "" + String.format("%.0f", minutes) + ":"
                    + String.format("%.0f", seconds) + ":"
                    + String.format("%.0f", milliseconds);
//            return "" + minutes + ":"
//                    + seconds + ":"
//                    + milliseconds;
        }

        double getRunTimeInMinutes()
        {
//            DecimalFormat df = new DecimalFormat("0.00");

//            System.out.println("MS: " + milliseconds);
//            System.out.println("MS to Mins: " + (milliseconds/(double)1000)/60);
//            System.out.println("Secs: " + seconds);
//            System.out.println("Secs to Mins: " + seconds/(double)60);
//            System.out.println("MS: " + milliseconds);
//            double temp = minutes + (seconds/60.00) + ((milliseconds/1000.00)/60);
//            System.out.println("Mins: " + temp);
//            System.out.println("Time in Minutes (Float): " + df.format(temp));
            return minutes + (seconds/60.00) + ((milliseconds/1000.00)/60);
        }

        String getDistanceRan()
        {
//            DecimalFormat df = new DecimalFormat("0.00");
            return "" + String.format("%.2f", distanceInMiles);
        }

        String getDateOfRun()
        {
            return date;
        }

        // Parcelling part
        Run(Parcel in){
            String[] data = new String[5];

            in.readStringArray(data);
            // the order needs to be the same as in writeToParcel() method
            this.minutes = Double.parseDouble(data[0]);
            this.seconds = Double.parseDouble(data[1]);
            this.milliseconds = Double.parseDouble(data[2]);
            this.distanceInMiles = Double.parseDouble(data[3]);
            this.date = data[4];
        }

        @Override
        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[] {this.minutes + "",
                    this.seconds + "",
                    this.milliseconds + "",
                    this.distanceInMiles + "",
                    this.date});
        }
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Run createFromParcel(Parcel in) {
                return new Run(in);
            }

            public Run[] newArray(int size) {
                return new Run[size];
            }
        };
    }

    final int NO_RUNNING_DATA_TO_SEND = 0;
    final int REQUEST_RUNNING_DATA = 1;
    final int SENT_RUNNING_DATA = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("In Activity Result");
        if (resultCode == RESULT_OK)
        {
            System.out.println("In Activity RESULT_OK");
            if (requestCode == REQUEST_RUNNING_DATA)
            {
                System.out.println("In Activity REQUEST_RUNNING_DATA");
                Intent intent = new Intent(this, HealthInsuranceActivity.class);
                if(listOfRuns.size() == 0)
                {
                    startActivityForResult(intent, NO_RUNNING_DATA_TO_SEND);
                }
                else
                {
                    for (int i = 0; i < listOfRuns.size(); i++) {
                        intent.putExtra("Run 1", listOfRuns.get(i));
                    }
                    startActivityForResult(intent, SENT_RUNNING_DATA);
                    listOfRuns.clear();
                }
            }
        }
    }
}