package com.example.jacobcollins.capstoneproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GoalsActivity extends AppCompatActivity implements OnClickListener {

    private Button btnGoal1;
    private Button btnGoal2;
    private Button btnGoal3;

    boolean isFragmentDisplayedGoal1 = false;
    boolean isFragmentDisplayedGoal2 = false;
    boolean isFragmentDisplayedGoal3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        btnGoal1 = (Button) findViewById(R.id.btnGoal1) ;
        btnGoal1.setOnClickListener(this);

        btnGoal2 = (Button) findViewById(R.id.btnGoal2) ;
        btnGoal2.setOnClickListener(this);

        btnGoal3 = (Button) findViewById(R.id.btnGoal3) ;
        btnGoal3.setOnClickListener(this);
    }

    public void displayFragment(String typeOfFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if(typeOfFragment.equals("goal1"))
        {
            Goal1Fragment fragment = Goal1Fragment.newInstance();
            fragmentTransaction.add(R.id.fragment_container,
            fragment).addToBackStack(null).commit();
            // Update the Button text.
            //btnGoal1.setText(R.string.close);
            // Set boolean flag to indicate fragment is open.
            isFragmentDisplayedGoal1 = true;
        }

        else if(typeOfFragment.equals("goal2"))
        {
            Goal2Fragment fragment = Goal2Fragment.newInstance();
            fragmentTransaction.add(R.id.fragment_container,
                    fragment).addToBackStack(null).commit();
            // Update the Button text.
            //btnGoal1.setText(R.string.close);
            // Set boolean flag to indicate fragment is open.
            isFragmentDisplayedGoal2 = true;
        }

        else if(typeOfFragment.equals("goal3"))
        {
            Goal3Fragment fragment = Goal3Fragment.newInstance();
            fragmentTransaction.add(R.id.fragment_container,
                    fragment).addToBackStack(null).commit();
            // Update the Button text.
            //btnGoal1.setText(R.string.close);
            // Set boolean flag to indicate fragment is open.
            isFragmentDisplayedGoal3 = true;
        }
    }

    public void closeFragment(String typeOfFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(typeOfFragment.equals("goal1"))
        {
            Goal1Fragment fragment = (Goal1Fragment) fragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                // Create and commit the transaction to remove the fragment.
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment).commit();
            }
            // Update the Button text.
            //mButton.setText(R.string.open);
            // Set boolean flag to indicate fragment is closed.
            isFragmentDisplayedGoal1 = false;
        }

        else if(typeOfFragment.equals("goal2"))
        {
            Goal2Fragment fragment = (Goal2Fragment) fragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                // Create and commit the transaction to remove the fragment.
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment).commit();
            }
            // Update the Button text.
            //mButton.setText(R.string.open);
            // Set boolean flag to indicate fragment is closed.
            isFragmentDisplayedGoal2 = false;
        }

        else if(typeOfFragment.equals("goal3"))
        {
            Goal3Fragment fragment = (Goal3Fragment) fragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                // Create and commit the transaction to remove the fragment.
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment).commit();
            }
            // Update the Button text.
            //mButton.setText(R.string.open);
            // Set boolean flag to indicate fragment is closed.
            isFragmentDisplayedGoal3 = false;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnGoal1)
        {
            Toast.makeText(GoalsActivity.this, "Button 1 Clicked", Toast.LENGTH_SHORT).show();
            if (!isFragmentDisplayedGoal1 && !isFragmentDisplayedGoal2 && !isFragmentDisplayedGoal3)
            {
                displayFragment("goal1");
            } else
            {
                if(isFragmentDisplayedGoal2){ closeFragment("goal2"); displayFragment("goal1"); }
                else if(isFragmentDisplayedGoal3){ closeFragment("goal3"); displayFragment("goal1"); }
            }
        }

        else if(view == btnGoal2)
        {
            Toast.makeText(GoalsActivity.this, "Button 2 Clicked", Toast.LENGTH_SHORT).show();
            if (!isFragmentDisplayedGoal1 && !isFragmentDisplayedGoal2 && !isFragmentDisplayedGoal3)
            {
                displayFragment("goal2");
            } else
            {
                if(isFragmentDisplayedGoal1){ closeFragment("goal1"); displayFragment("goal2"); }
                else if(isFragmentDisplayedGoal3){ closeFragment("goal3"); displayFragment("goal2"); }
            }
        }

        else if(view == btnGoal3)
        {
            Toast.makeText(GoalsActivity.this, "Button 3 Clicked", Toast.LENGTH_SHORT).show();
            if (!isFragmentDisplayedGoal1 && !isFragmentDisplayedGoal2 && !isFragmentDisplayedGoal3)
            {
                displayFragment("goal3");
            } else
            {
                if(isFragmentDisplayedGoal1){ closeFragment("goal1"); displayFragment("goal3"); }
                else if(isFragmentDisplayedGoal2){ closeFragment("goal2"); displayFragment("goal3"); }
            }
        }
    }
}
