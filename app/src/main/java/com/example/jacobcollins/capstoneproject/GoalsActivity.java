package com.example.jacobcollins.capstoneproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

    private boolean OneMileDistanceGoal = false;
    private boolean FiveMilesDistanceGoal = false;
    private boolean TenMilesDistanceGoal = false;
    private boolean TwentyMilesDistanceGoal = false;
    private boolean FiftyMilesDistanceGoal = false;
    private boolean OneHundredMilesDistanceGoal = false;

    private boolean NineMinuteTimeGoal = false;
    private boolean EightMinuteTimeGoal = false;
    private boolean SevenMinuteTimeGoal = false;
    private boolean SixMinuteTimeGoal = false;
    private boolean FiveMinuteTimeGoal = false;

    private int progressDistanceAmount = 0;
    private int progressMileTimeAmount = 0;

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

    public void distanceCheckboxClicked(View view)
    {
        unCheckOtherDistanceBoxesOtherThan(view);
    }

    public void mileTimeCheckboxClicked(View view)
    {
        unCheckOtherMileTimeBoxesOtherThan(view);
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

        mEdit1.clear();

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

        return mEdit1.commit();
    }

    public void loadData(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);

        if(mSharedPreference1.contains("distanceProgressBar Amount")) distanceProgressBar.setProgress(mSharedPreference1.getInt("distanceProgressBar Amount", 0));

        if(mSharedPreference1.contains("distanceCheckBox"))
        {
            String temp = mSharedPreference1.getString("distanceCheckBox", null);

            if (temp.equals("1")) checkBoxOneMile.setChecked(true);
            else if (temp.equals("5")) checkBoxFiveMiles.setChecked(true);
            else if (temp.equals("10")) checkBoxTenMiles.setChecked(true);
            else if (temp.equals("20")) checkBoxTwentyMiles.setChecked(true);
            else if (temp.equals("50")) checkBoxFiftyMiles.setChecked(true);
            else if (temp.equals("100")) checkBoxOneHundredMiles.setChecked(true);
        }

        if(mSharedPreference1.contains("mileTimeProgressBar Amount")) mileTimeProgressBar.setProgress((mSharedPreference1.getInt("mileTimeProgressBar Amount", 0)));

        if(mSharedPreference1.contains("mileTimeCheckBox"))
        {
            String temp = mSharedPreference1.getString("mileTimeCheckBox", null);

            if (temp.equals("9")) checkBoxNineMinuteMile.setChecked(true);
            else if (temp.equals("8")) checkBoxEightMinuteMile.setChecked(true);
            else if (temp.equals("7")) checkBoxSevenMinuteMile.setChecked(true);
            else if (temp.equals("6")) checkBoxSixMinuteMile.setChecked(true);
            else if (temp.equals("5")) checkBoxFiveMinuteMile.setChecked(true);
        }
    }

    @Override
    public void onClick(View view)
    {

    }
}
