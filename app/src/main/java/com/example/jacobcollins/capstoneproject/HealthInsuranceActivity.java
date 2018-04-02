package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jacobcollins.capstoneproject.MapsActivity.Run;

import java.util.ArrayList;

public class HealthInsuranceActivity extends AppCompatActivity {

    private String userId;
    private String userName;

    private boolean hasCreatedProfile;

    private Button createProfileButton;
    private Button updateProfileButton;
    private EditText userIDEditText;
    private EditText userNameEditText;

    private ArrayList<Run> listOfRuns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_insurance);

        createProfileButton = findViewById(R.id.createNewProfileButton);
        updateProfileButton = findViewById(R.id.updateRunningDataButton);
        userIDEditText = findViewById(R.id.userIDEditText);
        userNameEditText = findViewById(R.id.userNameEditText);

        updateProfileButton.setEnabled(false);

        if(hasCreatedProfile) createProfileButton.setEnabled(false);
        if(hasCreatedProfile) updateProfileButton.setEnabled(true);
    }

    public void onClick(View v)
    {
        if(v == createProfileButton)
        {
            if(userIDEditText.getText().length()==9 && containsOnlyNumbers(userIDEditText.getText()+""))
            {
                userId = userIDEditText.getText()+"";
                hasCreatedProfile = true;
                createProfileButton.setEnabled(false);
                System.out.println(userId);
                updateProfileButton.setEnabled(true);
            }
        }

        final int REQUEST_RUNNING_DATA = 1;
        if(v == updateProfileButton)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            setResult(RESULT_OK, intent);
            startActivityForResult(intent, REQUEST_RUNNING_DATA);

            //send data to the database and clear the listOfRunsArray
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
        saveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(this);
    }

    public void saveData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        if(userId != null)
            mEdit1.putString("User ID", userId);
        if(userName != null)
            mEdit1.putString("User Name", userName);

        mEdit1.apply();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);

        if(mSharedPreference1.contains("User ID"))
        {
            userId = mSharedPreference1.getString("User ID", null);
        }
        if(mSharedPreference1.contains("User Name"))
        {
            userName = mSharedPreference1.getString("User Name", null);
        }
    }

    final int NO_RUNNING_DATA_TO_SEND = 0;
    final int REQUEST_RUNNING_DATA = 1;
    final int SENT_RUNNING_DATA = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == NO_RUNNING_DATA_TO_SEND)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No Running Data Found", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if(requestCode == SENT_RUNNING_DATA)
            {
                Bundle runs = getIntent().getExtras();
                for(int i=0;i<runs.size();i++)
                {
                    Run temp = runs.getParcelable("Run "+i);
                    listOfRuns.add(temp);
                }
            }
        }
    }
}
