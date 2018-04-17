package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GoalsActivity extends AppCompatActivity implements OnClickListener {

    private CheckBox checkBoxOneMile;
    private CheckBox checkBoxFiveMiles;
    private CheckBox checkBoxTenMiles;
    private CheckBox checkBoxTwentyMiles;
    private CheckBox checkBoxFiftyMiles;
    private CheckBox checkBoxOneHundredMiles;

    private ProgressBar distanceProgressBar;

    private CheckBox checkBoxNineMinuteMile;
    private CheckBox checkBoxEightMinuteMile;
    private CheckBox checkBoxSevenMinuteMile;
    private CheckBox checkBoxSixMinuteMile;
    private CheckBox checkBoxFiveMinuteMile;

    private ProgressBar mileTimeProgressBar;

    private double amountOfMilesRan = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        checkBoxOneMile = findViewById(R.id.OneMileCheckBox);
        checkBoxFiveMiles = findViewById(R.id.FiveMilesCheckBox);
        checkBoxTenMiles = findViewById(R.id.TenMilesCheckBox);
        checkBoxTwentyMiles = findViewById(R.id.TwentyMilesCheckBox);
        checkBoxFiftyMiles = findViewById(R.id.FiftyMilesCheckBox);
        checkBoxOneHundredMiles = findViewById(R.id.OneHundredMilesCheckBox);

        distanceProgressBar = findViewById(R.id.distanceProgressBar);

        checkBoxNineMinuteMile = findViewById(R.id.checkBox9MinuteMileTime);
        checkBoxEightMinuteMile = findViewById(R.id.checkBox8MinuteMileTime);
        checkBoxSevenMinuteMile = findViewById(R.id.checkBox7MinuteMileTime);
        checkBoxSixMinuteMile = findViewById(R.id.checkBox6MinuteMileTime);
        checkBoxFiveMinuteMile = findViewById(R.id.checkBox5MinuteMileTime);

        mileTimeProgressBar = findViewById(R.id.mileTimeProgressBar);
    }

    public boolean hasDistanceGoal() {
        return checkBoxOneMile.isChecked() || checkBoxFiveMiles.isChecked() || checkBoxTenMiles.isChecked()
                || checkBoxTwentyMiles.isChecked() || checkBoxFiftyMiles.isChecked() || checkBoxOneHundredMiles.isChecked();
    }

    public boolean hasMileTimeGoal() {
        return checkBoxNineMinuteMile.isChecked() || checkBoxEightMinuteMile.isChecked() ||
                checkBoxSevenMinuteMile.isChecked() || checkBoxSixMinuteMile.isChecked() || checkBoxFiveMinuteMile.isChecked();
    }

    public double findDistanceGoal()
    {
        double value = 0;
        if(checkBoxOneMile.isChecked())
            value = 1.00;
        else if(checkBoxFiveMiles.isChecked())
            value = 5.00;
        else if(checkBoxTenMiles.isChecked())
            value = 10.00;
        else if(checkBoxTwentyMiles.isChecked())
            value = 20.00;
        else if(checkBoxFiftyMiles.isChecked())
            value = 50.00;
        else if(checkBoxOneHundredMiles.isChecked())
            value = 100.00;
        return value;
    }

    public double findMileTimeGoal()
    {
        double value = 0;
        if(checkBoxNineMinuteMile.isChecked())
            value = 9.00;
        else if(checkBoxEightMinuteMile.isChecked())
             value = 8.00;
        else if(checkBoxSevenMinuteMile.isChecked())
            value = 7.00;
        else if(checkBoxSixMinuteMile.isChecked())
            value = 6.00;
        else if(checkBoxFiveMinuteMile.isChecked())
            value = 5.00;
        return value;
    }

    public void distanceCheckboxClicked(View view)
    {
        unCheckOtherDistanceBoxesOtherThan(view);
        distanceProgressBar.setProgress(0);
    }

    public void mileTimeCheckboxClicked(View view)
    {
        unCheckOtherMileTimeBoxesOtherThan(view);
        mileTimeProgressBar.setProgress(0);
    }

    public void unCheckAllDistanceBoxes()
    {
        checkBoxOneMile.setChecked(false);
        checkBoxFiveMiles.setChecked(false);
        checkBoxTenMiles.setChecked(false);
        checkBoxTwentyMiles.setChecked(false);
        checkBoxFiftyMiles.setChecked(false);
        checkBoxOneHundredMiles.setChecked(false);
    }

    public void unCheckAllTimeBoxes()
    {
        checkBoxNineMinuteMile.setChecked(false);
        checkBoxEightMinuteMile.setChecked(false);
        checkBoxSevenMinuteMile.setChecked(false);
        checkBoxSixMinuteMile.setChecked(false);
        checkBoxFiveMinuteMile.setChecked(false);
    }

    public void unCheckOtherDistanceBoxesOtherThan(View view)
    {
        switch (view.getId()) {
            case R.id.OneMileCheckBox:
                if (checkBoxFiveMiles.isChecked()) checkBoxFiveMiles.toggle();
                else if (checkBoxTenMiles.isChecked()) checkBoxTenMiles.toggle();
                else if (checkBoxTwentyMiles.isChecked()) checkBoxTwentyMiles.toggle();
                else if (checkBoxFiftyMiles.isChecked()) checkBoxFiftyMiles.toggle();
                else if (checkBoxOneHundredMiles.isChecked()) checkBoxOneHundredMiles.toggle();
                break;
            case R.id.FiveMilesCheckBox:
                if (checkBoxOneMile.isChecked()) checkBoxOneMile.toggle();
                else if (checkBoxTenMiles.isChecked()) checkBoxTenMiles.toggle();
                else if (checkBoxTwentyMiles.isChecked()) checkBoxTwentyMiles.toggle();
                else if (checkBoxFiftyMiles.isChecked()) checkBoxFiftyMiles.toggle();
                else if (checkBoxOneHundredMiles.isChecked()) checkBoxOneHundredMiles.toggle();
                break;
            case R.id.TenMilesCheckBox:
                if (checkBoxOneMile.isChecked()) checkBoxOneMile.toggle();
                else if (checkBoxFiveMiles.isChecked()) checkBoxFiveMiles.toggle();
                else if (checkBoxTwentyMiles.isChecked()) checkBoxTwentyMiles.toggle();
                else if (checkBoxFiftyMiles.isChecked()) checkBoxFiftyMiles.toggle();
                else if (checkBoxOneHundredMiles.isChecked()) checkBoxOneHundredMiles.toggle();
                break;
            case R.id.TwentyMilesCheckBox:
                if (checkBoxOneMile.isChecked()) checkBoxOneMile.toggle();
                else if (checkBoxFiveMiles.isChecked()) checkBoxFiveMiles.toggle();
                else if (checkBoxTenMiles.isChecked()) checkBoxTenMiles.toggle();
                else if (checkBoxFiftyMiles.isChecked()) checkBoxFiftyMiles.toggle();
                else if (checkBoxOneHundredMiles.isChecked()) checkBoxOneHundredMiles.toggle();
                break;
            case R.id.FiftyMilesCheckBox:
                if (checkBoxOneMile.isChecked()) checkBoxOneMile.toggle();
                else if (checkBoxFiveMiles.isChecked()) checkBoxFiveMiles.toggle();
                else if (checkBoxTenMiles.isChecked()) checkBoxTenMiles.toggle();
                else if (checkBoxTwentyMiles.isChecked()) checkBoxTwentyMiles.toggle();
                else if (checkBoxOneHundredMiles.isChecked()) checkBoxOneHundredMiles.toggle();
                break;
            case R.id.OneHundredMilesCheckBox:
                if (checkBoxOneMile.isChecked()) checkBoxOneMile.toggle();
                else if (checkBoxFiveMiles.isChecked()) checkBoxFiveMiles.toggle();
                else if (checkBoxTenMiles.isChecked()) checkBoxTenMiles.toggle();
                else if (checkBoxTwentyMiles.isChecked()) checkBoxTwentyMiles.toggle();
                else if (checkBoxFiftyMiles.isChecked()) checkBoxFiftyMiles.toggle();
                break;
        }
    }

    public void unCheckOtherMileTimeBoxesOtherThan(View view)
    {
        switch (view.getId()) {
            case R.id.checkBox9MinuteMileTime:
                if (checkBoxEightMinuteMile.isChecked()) checkBoxEightMinuteMile.toggle();
                else if (checkBoxSevenMinuteMile.isChecked()) checkBoxSevenMinuteMile.toggle();
                else if (checkBoxSixMinuteMile.isChecked()) checkBoxSixMinuteMile.toggle();
                else if (checkBoxFiveMinuteMile.isChecked()) checkBoxFiveMinuteMile.toggle();
                break;
            case R.id.checkBox8MinuteMileTime:
                if (checkBoxNineMinuteMile.isChecked()) checkBoxNineMinuteMile.toggle();
                else if (checkBoxSevenMinuteMile.isChecked()) checkBoxSevenMinuteMile.toggle();
                else if (checkBoxSixMinuteMile.isChecked()) checkBoxSixMinuteMile.toggle();
                else if (checkBoxFiveMinuteMile.isChecked()) checkBoxFiveMinuteMile.toggle();
                break;
            case R.id.checkBox7MinuteMileTime:
                if (checkBoxNineMinuteMile.isChecked()) checkBoxNineMinuteMile.toggle();
                else if (checkBoxEightMinuteMile.isChecked()) checkBoxEightMinuteMile.toggle();
                else if (checkBoxSixMinuteMile.isChecked()) checkBoxSixMinuteMile.toggle();
                else if (checkBoxFiveMinuteMile.isChecked()) checkBoxFiveMinuteMile.toggle();
                break;
            case R.id.checkBox6MinuteMileTime:
                if (checkBoxNineMinuteMile.isChecked()) checkBoxNineMinuteMile.toggle();
                else if (checkBoxEightMinuteMile.isChecked()) checkBoxEightMinuteMile.toggle();
                else if (checkBoxSevenMinuteMile.isChecked()) checkBoxSevenMinuteMile.toggle();
                else if (checkBoxFiveMinuteMile.isChecked()) checkBoxFiveMinuteMile.toggle();
                break;
            case R.id.checkBox5MinuteMileTime:
                if (checkBoxNineMinuteMile.isChecked()) checkBoxNineMinuteMile.toggle();
                else if (checkBoxEightMinuteMile.isChecked()) checkBoxEightMinuteMile.toggle();
                else if (checkBoxSevenMinuteMile.isChecked()) checkBoxSevenMinuteMile.toggle();
                else if (checkBoxSixMinuteMile.isChecked()) checkBoxSixMinuteMile.toggle();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Goals: in on Pause");
        saveData();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("Goals: in on Resume");
        loadData(this);
        Bundle extras = getIntent().getExtras();
        System.out.println("In onCreate MapsActivity");
        if (extras != null)
        {
            System.out.println("In Intent");
            if(hasMileTimeGoal()) {
                System.out.println("In MileTime Goal");
                double runTimePerMile = extras.getDouble("Run Time Per Mile");
                System.out.println("Mile Time: " + runTimePerMile);
                double mileTimeGoal = findMileTimeGoal();
                System.out.println("Mile Time Goal: " + mileTimeGoal);
                System.out.println("mileTimeGoal <= runTimePerMile: " + (runTimePerMile <= mileTimeGoal));
                if (runTimePerMile <= mileTimeGoal) {
                    System.out.println("MileTimeGoal Completed");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You completed your mile time goal!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    unCheckAllTimeBoxes();
                    mileTimeProgressBar.setProgress(0);
                } else {
                    double temp = mileTimeGoal / runTimePerMile;
                    int progressNow = (int) temp;
                    System.out.println("Progress MileTime: " + progressNow);
                    mileTimeProgressBar.setProgress(progressNow);
                }
            }

            if(hasDistanceGoal()) {
                System.out.println("In Distance Goal");
                double distanceRan = Double.parseDouble(extras.getString("Distance"));
                amountOfMilesRan += distanceRan;
                double amountOfMilesRanGoal = findDistanceGoal();
                System.out.println("Miles Ran Total: " + amountOfMilesRan);
                System.out.println("Miles Ran Goal: " + amountOfMilesRanGoal);
                if (amountOfMilesRanGoal <= amountOfMilesRan)
                {
                    System.out.println("DistanceGoal Completed");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You completed your mile distance goal!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    unCheckAllDistanceBoxes();
                    distanceProgressBar.setProgress(0);
                    amountOfMilesRan = 0;
                }
                else
                {
                    double temp = (amountOfMilesRan/amountOfMilesRanGoal)*100;
                    int progressNow = (int) temp;
                    System.out.println("Progress Distance: " + progressNow);
                    distanceProgressBar.setProgress(progressNow);
                }
            }
        }
    }

    public void clearPrefsOfGoalData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.remove("distanceCheckBox");
        mEdit1.remove("distanceProgressBar Amount");
        mEdit1.remove("mileTimeCheckBox");
        mEdit1.remove("mileTimeProgressBar Amount");
        mEdit1.remove("distanceRanTotal");

        mEdit1.apply();
    }

    public void saveData()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        clearPrefsOfGoalData();

        if(checkBoxOneMile.isChecked()) mEdit1.putString("distanceCheckBox", "1");
        else if(checkBoxFiveMiles.isChecked()) mEdit1.putString("distanceCheckBox", "5");
        else if(checkBoxTenMiles.isChecked()) mEdit1.putString("distanceCheckBox", "10");
        else if(checkBoxTwentyMiles.isChecked()) mEdit1.putString("distanceCheckBox", "20");
        else if(checkBoxFiftyMiles.isChecked()) mEdit1.putString("distanceCheckBox", "50");
        else if(checkBoxOneHundredMiles.isChecked()) mEdit1.putString("distanceCheckBox", "100");

        mEdit1.putInt("distanceProgressBar Amount", distanceProgressBar.getProgress());

        if(checkBoxNineMinuteMile.isChecked()) mEdit1.putString("mileTimeCheckBox", "9");
        else if(checkBoxEightMinuteMile.isChecked()) mEdit1.putString("mileTimeCheckBox", "8");
        else if(checkBoxSevenMinuteMile.isChecked()) mEdit1.putString("mileTimeCheckBox", "7");
        else if(checkBoxSixMinuteMile.isChecked()) mEdit1.putString("mileTimeCheckBox", "6");
        else if(checkBoxFiveMinuteMile.isChecked()) mEdit1.putString("mileTimeCheckBox", "5");

        mEdit1.putInt("mileTimeProgressBar Amount", mileTimeProgressBar.getProgress());
        mEdit1.putString("distanceRanTotal", "" + amountOfMilesRan);

        mEdit1.apply();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);

        if(mSharedPreference1.contains("distanceProgressBar Amount")) distanceProgressBar.setProgress(mSharedPreference1.getInt("distanceProgressBar Amount", 0));

        if(mSharedPreference1.contains("distanceCheckBox"))
        {
            String temp = mSharedPreference1.getString("distanceCheckBox", null);
            System.out.println("distanceBoxChecked: " + temp);
            if (temp != null) {
                switch (temp) {
                    case "1":
                        checkBoxOneMile.setChecked(true);
                        break;
                    case "5":
                        checkBoxFiveMiles.setChecked(true);
                        break;
                    case "10":
                        checkBoxTenMiles.setChecked(true);
                        break;
                    case "20":
                        checkBoxTwentyMiles.setChecked(true);
                        break;
                    case "50":
                        checkBoxFiftyMiles.setChecked(true);
                        break;
                    case "100":
                        checkBoxOneHundredMiles.setChecked(true);
                        break;
                }
            }
        }

        if(mSharedPreference1.contains("mileTimeProgressBar Amount"))
        {
            System.out.println(mSharedPreference1.getInt("mileTimeProgressBar Amount", 0));
            mileTimeProgressBar.setProgress((mSharedPreference1.getInt("mileTimeProgressBar Amount", 0)));
        }

        if(mSharedPreference1.contains("mileTimeCheckBox"))
        {
            String temp = mSharedPreference1.getString("mileTimeCheckBox", null);
            System.out.println("mileTimeCheckBoxChecked: " + temp);
            if (temp != null) {
                switch (temp) {
                    case "9":
                        checkBoxNineMinuteMile.setChecked(true);
                        break;
                    case "8":
                        checkBoxEightMinuteMile.setChecked(true);
                        break;
                    case "7":
                        checkBoxSevenMinuteMile.setChecked(true);
                        break;
                    case "6":
                        checkBoxSixMinuteMile.setChecked(true);
                        break;
                    case "5":
                        checkBoxFiveMinuteMile.setChecked(true);
                        break;
                }
            }
        }

        if(mSharedPreference1.contains("distanceRanTotal"))
        {
            System.out.println("AmountOfMilesRan: " + amountOfMilesRan);
            amountOfMilesRan = Double.parseDouble(mSharedPreference1.getString("distanceRanTotal", null));
        }

//        clearData();
    }

//    public void clearData()
//    {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor mEdit1 = sp.edit();
//
//        mEdit1.clear();
//    }

    @Override
    public void onClick(View view)
    {

    }
}
