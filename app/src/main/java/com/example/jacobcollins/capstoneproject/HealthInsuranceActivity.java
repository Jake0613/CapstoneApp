package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.AppUserDO;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.jacobcollins.capstoneproject.MapsActivity.Run;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HealthInsuranceActivity extends AppCompatActivity {

    private boolean hasCreatedProfile;

    private Button createProfileButton;
    private Button updateProfileButton;

    private ArrayList<Run> listOfRuns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_insurance);

        createProfileButton = findViewById(R.id.createNewProfileButton);
        updateProfileButton = findViewById(R.id.updateRunningDataButton);

        updateProfileButton.setEnabled(false);

        if(hasCreatedProfile) createProfileButton.setEnabled(false);
        if(hasCreatedProfile) updateProfileButton.setEnabled(true);
    }

    public void onClick(View v)
    {
        if(v == createProfileButton)
        {
            Intent authenticateIntent = new Intent(this, AuthenticatorActivity.class);
            startActivity(authenticateIntent);
            hasCreatedProfile = true;
            updateProfileButton.setEnabled(true);
        }

        final int REQUEST_RUNNING_DATA = 1;
        if(v == updateProfileButton)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            setResult(RESULT_OK, intent);
            intent.putExtra("Run Data Request", 100);
            startActivityForResult(intent, REQUEST_RUNNING_DATA);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("HealthInsurance: in on Pause");
        saveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("HealthInsurance: in on Resume");
        loadData(this);
    }

    public void saveData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        clearPrefsOfHealthInsuranceData();

        mEdit1.putBoolean("Created Profile?", hasCreatedProfile);

        mEdit1.apply();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);

        if(mSharedPreference1.contains("Created Profile?"))
        {
            hasCreatedProfile = mSharedPreference1.getBoolean("Created Profile?", false);
        }
        if(hasCreatedProfile) {
            createProfileButton.setEnabled(false);
            updateProfileButton.setEnabled(true);
        }
    }

    public void clearPrefsOfHealthInsuranceData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.remove("Created Profile?");

        mEdit1.apply();
    }

    String identityId = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            System.out.println("In ResultOk HealthInsurance");

            Bundle runs = getIntent().getExtras();
            if(runs != null) {
                for (int i = 0; i < runs.size(); i++) {
                    Run temp = runs.getParcelable("Run " + i);
                    listOfRuns.add(temp);
                }
            }

            AsyncTaskGetID task = new AsyncTaskGetID();
            task.execute(listOfRuns);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Running Data Was Sent", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        else if(resultCode == RESULT_CANCELED)
        {
            System.out.println("In ResultCanceled HealthInsurance");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No Running Data Found", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private class AsyncTaskGetID extends AsyncTask<ArrayList<Run>, Void, Void>
    {
        @Override
        protected Void doInBackground(ArrayList<Run>... arrayLists) {
            Bundle runs = getIntent().getExtras();
            if(runs != null) {
                for (int i = 0; i < runs.size(); i++) {
                    Run temp = runs.getParcelable("Run " + i);
                    listOfRuns.add(temp);
                }
            }

            final AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
            final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .build();
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(), // Context
                    "us-east-1:fb847b25-f0ce-4f03-95dc-f876a2a1f5a6", // Identity Pool ID
                    Regions.US_EAST_1); // Region
            System.out.println("Cached ID: " + credentialsProvider.getCachedIdentityId());
            Set<String> listOfRunsTemp = new HashSet<>();
            for(Run r: listOfRuns)
            {
                listOfRunsTemp.add("Date: " + r.getDateOfRun() + ",Distance: " + r.getDistanceRan() + ",Time: " + r.getRunTime());
            }

            final com.amazonaws.models.nosql.AppUserDO appUser = new AppUserDO();
            appUser.setUserId(credentialsProvider.getCachedIdentityId());
            appUser.setRuns(listOfRunsTemp);

            if(identityId!=null && listOfRunsTemp.size()>0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dynamoDBMapper.save(appUser);
                    }
                }).start();
            }
            return null;
        }
    }
}