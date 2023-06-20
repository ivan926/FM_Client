package com.bignerdranch.android.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URISyntaxException;

public class main_activity_container extends AppCompatActivity {


    private google_map_fragment google_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //String eventID = savedInstanceState.getString("eventID");
        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        String personID = intent.getStringExtra("personID");
        super.onCreate(savedInstanceState);



        setContentView(R.layout.map_container);
        FragmentManager fragment_map = this.getSupportFragmentManager();

        if (this.google_fragment == null) {
            //pass eventID to properly populate map
            this.google_fragment = google_fragment.getNewInstanceOfMapFrag(eventID,personID);
        }

       fragment_map.beginTransaction().add(R.id.google_map_container_activity, this.google_fragment).commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.person_menu, menu);


        return false;

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);

        switch (item.getItemId()) {
            case android.R.id.home: {
                // Settings
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}