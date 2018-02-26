package com.example.jacobcollins.capstoneproject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.Toast;

public class GoalsActivity extends AppCompatActivity implements OnClickListener {

    private Button btnGoal1;
    private Button btnGoal2;
    private Button btnGoal3;

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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // Create a progress bar to display while the list loads
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//        progressBar.setIndeterminate(true);
//        getListView().setEmptyView(progressBar);
//
//        // Must add the progress bar to the root of the layout
//        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
//        root.addView(progressBar);
    }

    @Override
    public void onClick(View view) {
        if(view == btnGoal1)
        {
            Toast.makeText(GoalsActivity.this, "Button 1 Clicked", Toast.LENGTH_SHORT).show();
        }

        else if(view == btnGoal2)
        {
            Toast.makeText(GoalsActivity.this, "Button 2 Clicked", Toast.LENGTH_SHORT).show();
        }

        else if(view == btnGoal3)
        {
            Toast.makeText(GoalsActivity.this, "Button 3 Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
