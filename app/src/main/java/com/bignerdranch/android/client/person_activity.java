package com.bignerdranch.android.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import model.Event;
import model.Person;

public class person_activity extends AppCompatActivity {

    private String CurrentPersonID = "";

    private TextView firstName;

    private TextView lastName;

    private TextView gender;

    private dataCache family_tree_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        CurrentPersonID = getIntent().getExtras().getString("personID");
        //Log.d("In_person_activity",CurrentPersonID);

        setContentView(R.layout.person_activity_layout);

        ExpandableListView expandableListView = findViewById(R.id.person_expandable_list_view);



        family_tree_data = dataCache.getInstance();

        firstName = findViewById(R.id.first_name_person_activity);
        lastName = findViewById(R.id.last_name_person_activity);
        gender = findViewById(R.id.gender_person_actvity);

        Person CurrentPerson = (Person)family_tree_data.getPersonMap().get(CurrentPersonID);


        firstName.setText(CurrentPerson.getFirstName());
        lastName.setText(CurrentPerson.getLastName());

        String StringGender = "";

        if(CurrentPerson.getGender().equalsIgnoreCase("m"))
        {
            StringGender = "Male";
        }
        else
        {
            StringGender = "Female";
        }

        gender.setText(StringGender);

        expandableListView.setAdapter(new ExpandableListAdapter(this,
                new ArrayList<>( Arrays.asList(getString(R.string.group_title_event), getString(R.string.group_title_family))),
                CurrentPerson));


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("In_person_activity","On resume called, forground active");
    }



    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private final int EVENT_POSITION = 0;
        private final int FAMILY_POSITION = 1;
        private final Context context;


        private ArrayList<String> type_of_List;
        private ArrayList<Event> list_of_events;

        private ArrayList<Person> list_of_family_members;

        private Person person;


        private ExpandableListAdapter(Context context, ArrayList<String> list_of_titles,Person person)
        {
            this.person = person;
            this.context = context;
            this.type_of_List = list_of_titles;

            this.list_of_events = (ArrayList<Event>) family_tree_data.getEventMap().get(person.getPersonID());

           this.list_of_family_members = family_tree_data.getListOfFamilyMembers(person.getPersonID());


        }

        //just two groups
        @Override
        public int getGroupCount() {
            return type_of_List.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            //it is a life event
            if(groupPosition == 0)
            {
                return family_tree_data.getSizeOfPersonEvents(person.getPersonID());
            }
            else
            {
                return family_tree_data.getTotalFamilyMembersSize(person.getPersonID());
            }
        }

        //specify group position return the title or string of events or family
        //make a string in the string resource file R.String.example

        //if else to determine if its events or family
        @Override
        public Object getGroup(int groupPosition) {
            if(groupPosition == 0)
            {
                return getString(R.string.group_title_event);
            }
            else
            {
                return getString(R.string.group_title_family);
            }

        }

        //given the group position you will know if its an event or family
        //
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if(groupPosition == 0)
            {
               return list_of_events.get(childPosition);

            }
            else {
               return list_of_family_members.get(childPosition);

            }

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_group, parent, false);
            }

            String title = (String)getGroup(groupPosition);

            TextView titleView = convertView.findViewById(R.id.listTitle);

            if(title.equals(getString(R.string.group_title_family)))
            {
                titleView.setText(R.string.group_title_family);
            }
            else {
                titleView.setText(R.string.group_title_event);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){



            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }



            if(groupPosition == 0)
            {
                //setting the icon
                Event Currentevent = (Event)getChild(groupPosition,childPosition);
                ImageView icon = (ImageView) convertView.findViewById(R.id.image_child);
                icon.setBackgroundResource(R.drawable.map_marker_48);

                TextView informationOnTop = (TextView) convertView.findViewById(R.id.detail_above);
                String top_info = Currentevent.getEventType() +": " + Currentevent.getCity() + ", " +
                        Currentevent.getCountry() + " (" + Currentevent.getYear() + ")";

                informationOnTop.setText(top_info);

                TextView informationOnBottom = (TextView) convertView.findViewById(R.id.detail_below);
                Person currentPerson = (Person)family_tree_data.getPersonMap().get(Currentevent.getPersonID());

                String bottom_info = currentPerson.getFirstName() + " " + currentPerson.getLastName();

                informationOnBottom.setText(bottom_info);

            }
            else
            {
                Person familyMember = (Person)getChild(groupPosition,childPosition);

                //setting correct icon for gender
                ImageView icon = (ImageView) convertView.findViewById(R.id.image_child);

                if(familyMember.getGender().equalsIgnoreCase("m")) {
                    icon.setBackgroundResource(R.drawable.male_48_xhdpi);
                }
                else {
                    icon.setBackgroundResource(R.drawable.female_48_xhdpi);
                }

               // setting text views for bottom and top
                TextView name = (TextView) convertView.findViewById(R.id.detail_above);
                TextView relationship_with_current_person = (TextView) convertView.findViewById(R.id.detail_below);

                String fullName = familyMember.getFirstName() + " " + familyMember.getLastName();
                name.setText(fullName);

                String relationship = family_tree_data.get_relation_to_current_person(person.getPersonID(), familyMember.getPersonID());
                relationship_with_current_person.setText(relationship);



            }
            Toast toast = new Toast(context);
            //need to create an new instance of Person activity for people
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){

                    if(groupPosition == 0) {
                        //setting the icon
                        Event Currentevent = (Event) getChild(groupPosition, childPosition);

                        Intent intent = new Intent(context, main_activity_container.class);
                        Bundle extras = new Bundle();
                        extras.putString("eventID", Currentevent.getEventID());
                        extras.putString("personID", Currentevent.getPersonID());

                        intent.putExtras(extras);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Person familyMember = (Person)getChild(groupPosition,childPosition);
                        Intent intent = new Intent(context,person_activity.class);

                        intent.putExtra("personID", familyMember.getPersonID());
                        context.startActivity(intent);
                    }
                }

            });


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


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