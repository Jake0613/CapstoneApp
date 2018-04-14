package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import com.amazonaws.models.nosql.UserDO;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.jacobcollins.capstoneproject.MapsActivity.Run;

import java.util.ArrayList;
//import java.lang.Runnable;

public class HealthInsuranceActivity extends AppCompatActivity {

    private String userId;
    private String userName;

    private boolean hasCreatedProfile;

    private Button createProfileButton;
    private Button updateProfileButton;
    private EditText userIDEditText;
    private EditText userNameEditText;

    private ArrayList<Run> listOfRuns = new ArrayList<>();

    // Declare a DynamoDBMapper object
    DynamoDBMapper dynamoDBMapper;
    IdentityManager identityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_insurance);

        createProfileButton = findViewById(R.id.createNewProfileButton);
        updateProfileButton = findViewById(R.id.updateRunningDataButton);
//        userIDEditText = findViewById(R.id.userIDEditText);
//        userNameEditText = findViewById(R.id.userNameEditText);

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
//            if(userIDEditText.getText().length()==9 && containsOnlyNumbers(userIDEditText.getText()+""))
//            {
//                userId = userIDEditText.getText()+"";
//                userName = userNameEditText.getText()+"";
//                hasCreatedProfile = true;
//                createProfileButton.setEnabled(false);
//                System.out.println(userId);
//                updateProfileButton.setEnabled(true);
//                Toast toast = Toast.makeText(getApplicationContext(),
//                        "Profile Created", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }
            hasCreatedProfile = true;
            updateProfileButton.setEnabled(true);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Profile Created", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
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

    public static boolean containsOnlyNumbers(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
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
//        mEdit1.clear();

//        if(userId != null)
//            mEdit1.putString("User ID", userId);
//        if(userName != null)
//            mEdit1.putString("User Name", userName);
        mEdit1.putBoolean("Created Profile?", hasCreatedProfile);

        mEdit1.apply();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);

//        if(mSharedPreference1.contains("User ID"))
//        {
//            userId = mSharedPreference1.getString("User ID", null);
//            userIDEditText.setText(userId);
//        }
//        if(mSharedPreference1.contains("User Name"))
//        {
//            userName = mSharedPreference1.getString("User Name", null);
//            userNameEditText.setText(userName);
//        }
        if(mSharedPreference1.contains("Created Profile?"))
        {
            hasCreatedProfile = mSharedPreference1.getBoolean("Created Profile?", false);
        }
        if(hasCreatedProfile) {
            createProfileButton.setEnabled(false);
            updateProfileButton.setEnabled(true);
        }

//        clearData();
    }

    public void clearPrefsOfHealthInsuranceData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

//        mEdit1.remove("User ID");
//        mEdit1.remove("User Name");
        mEdit1.remove("Created Profile?");

        mEdit1.commit();
    }

    public void clearData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.clear();
    }

    final int NO_RUNNING_DATA_TO_SEND = 0;
    final int REQUEST_RUNNING_DATA = 1;
    final int SENT_RUNNING_DATA = 2;
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

            final AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
            this.dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .build();
            AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
                @Override
                public void onComplete(AWSStartupResult awsStartupResult) {
                    IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                        @Override
                        public void onIdentityId(String s) {
                            identityId = s;
                        }

                        @Override
                        public void handleError(Exception e) {
                            System.out.println(e);
                        }
                    });
                }
            }).execute();

            if(identityId != null) System.out.println("UserId: " + identityId);

            for(Run r: listOfRuns)
            {
                final UserDO user = new UserDO();
                user.setUserId("Test");

                Runnable runnable = new Runnable() {
                    public void run() {
                        //DynamoDB calls go here
                        dynamoDBMapper.save(user);
                    }
                };
                Thread mythread = new Thread(runnable);
                mythread.start();
            }
            listOfRuns.clear();
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
}
