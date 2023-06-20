package com.bignerdranch.android.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class settings_activity extends AppCompatActivity {

    private Switch life_story_switch;

    private Switch family_tree_line_switch;

    private Switch spouse_line_switch;

    private Switch father_side_switch;

    private Switch mother_side_switch;

    private Switch male_events_line_switch;

    private Switch female_events_line_switch;

    private TextView logout_button;

    private settings_data settings;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

       life_story_switch = findViewById(R.id.life_story_line);
       family_tree_line_switch = findViewById(R.id.Family_Tree_line_Switch);
       spouse_line_switch = findViewById(R.id.Spouse_line_Switch);
       father_side_switch = findViewById(R.id.Father_Side_Switch);
       mother_side_switch = findViewById(R.id.mother_side_line_Switch);
       male_events_line_switch = findViewById(R.id.male_events_line_Switch);
       female_events_line_switch = findViewById(R.id.female_events_line_Switch);

       logout_button = findViewById(R.id.logout_settings);

       //getting data for current settings
        settings = settings_data.getInstance();

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText("I have been clicked");

        //when settings activity is created set toggles to current settings
        synchronizeCurrentSettings();


        life_story_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    Log.d("onChecked_life_story","changing life story to true");
                    settings.setLife_story_line(isChecked);
                    //change settings make changes
                }
                else {
                    Log.d("onChecked_life_story","changing life story to false = " + isChecked);
                    settings.setLife_story_line(isChecked);
                    //change settings
                }


            }
        });

        family_tree_line_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    settings.setFamily_tree_line(isChecked);

                    //change settings make changes
                }
                else {
                    settings.setFamily_tree_line(isChecked);
                    //change settings
                }


            }
        });

        spouse_line_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    settings.setSpouse_line(isChecked);
                    //change settings make changes
                }
                else {
                    settings.setSpouse_line(isChecked);
                    //change settings
                }



            }
        });

        father_side_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                    settings.setFather_side_line(isChecked);
                    //change settings make changes
                }
                else {
                    settings.setFather_side_line(isChecked);
                    //change settings
                }


            }
        });

        mother_side_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked == true)
                {
                    settings.setMother_side(isChecked);
                    //change settings make changes
                }
                else {
                    settings.setMother_side(isChecked);
                    //change settings
                }

            }
        });

        male_events_line_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked == true)
                {
                    settings.setMale_event_side(isChecked);
                    //change settings make changes
                }
                else {
                    settings.setMale_event_side(isChecked);
                    //change settings
                }

            }
        });

        female_events_line_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked == true)
                {
                    settings.setFemale_event_side(isChecked);
                    //change settings make changes
                }
                else {
                    settings.setFemale_event_side(isChecked);
                    //change settings
                }

            }
        });



        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });



    }

    private void synchronizeCurrentSettings()
    {
        Log.d("Inside synch function","Synch is being checked");
        //example below called settings method to find out current state
        life_story_switch.setChecked(settings.getLife_story_line());
        family_tree_line_switch.setChecked(settings.getFamily_tree_line());
        spouse_line_switch.setChecked(settings.getSpouse_line());
        father_side_switch.setChecked(settings.getFather_side_line());
        mother_side_switch.setChecked(settings.getMother_side());
        male_events_line_switch.setChecked(settings.getMale_event_side());
        female_events_line_switch.setChecked(settings.getFemale_event_side());

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