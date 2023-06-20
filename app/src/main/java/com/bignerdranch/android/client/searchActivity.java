package com.bignerdranch.android.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import model.Event;
import model.Person;

public class searchActivity extends AppCompatActivity {

    Context context = this;
    private SearchView searchBar;

    String event_title = "event";
    String people_title = "people";

    private dataCache family_tree_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        family_tree_data = dataCache.getInstance();

        //getting searchbar view
        this.searchBar = findViewById(R.id.search_bar);
        //attaching correct event listener to extract typed data.
        this.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("OnQueryTextSubmit","Inside query listener");
                search(newText);
                return false;
            }
        });



    }


    //use for arraylist to determine if query has found either a person or event

    private void search(String query)
    {
        //hashmap that will contain title as key and array of events and people
        HashMap<String,ArrayList> found_items;

        //convert input into lower case
        query = query.toLowerCase();

        //determine whether you will have person or events or both
        ArrayList<String> nameOfGroups = new ArrayList<>();


        //will return hashmap of group_name being the keys and event list or event list
        //also returns arraylist of name of groups which determines if any events or people
        //were even found when iterating through all events or people
        found_items = begin_query(nameOfGroups,query);




        ExpandableListView expandableListView = findViewById(R.id.explandable_view_search);


        expandableListView.setAdapter(new ExpandableListAdapter(this,nameOfGroups,found_items));


    }

    HashMap<String,ArrayList> begin_query(ArrayList<String> nameOfGroups,String query)
    {
        HashMap<String,ArrayList> found_objects = new HashMap<>();

        contains(found_objects,query);
        //after finding all potential events see if which types are found
        if(found_objects.containsKey(people_title))
        {
            nameOfGroups.add(people_title);
        }
        if(found_objects.containsKey(event_title))
        {
            nameOfGroups.add(event_title);
        }


        return found_objects;

    }

    private void contains(HashMap<String,ArrayList> found_objects,String query)
    {
        ArrayList<Person> people_array = new ArrayList<>();
        ArrayList<Event> events_array = new ArrayList<>();
        //probably not necessary but just in case
        people_array.clear();
        events_array.clear();
        Event[] eventList = family_tree_data.getListOfEvents();
        Person[] personList = family_tree_data.getListOfPeople();

        for (Person currentPerson : personList) {

            String first_name = currentPerson.getFirstName() + currentPerson.getLastName();
            first_name = first_name.toLowerCase();
            String gender = currentPerson.getGender();
            gender = gender.toLowerCase();
            if(first_name.contains(query) || gender.contains(query))
            {
                people_array.add(currentPerson);
                found_objects.put(people_title,people_array);
            }

        }



        //type, city, country, date, and the associated person’s name
        for (Event currentEvent : eventList) {

            String type = currentEvent.getEventType().toLowerCase();
            String city = currentEvent.getCity().toLowerCase();
            String country = currentEvent.getCountry().toLowerCase();
            String year = currentEvent.getYear().toString();
            String associatedUsername = currentEvent.getAssociatedUsername().toLowerCase();

            if(type.contains(query) || city.contains(query) || country.contains(query) || year.contains(query) || associatedUsername.contains(query))
            {
                events_array.add(currentEvent);
                found_objects.put(event_title,events_array);
            }


        }






    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final Context context;


        private ArrayList<String> type_of_List;

        private HashMap<String,ArrayList> events_and_people_found;


        private ExpandableListAdapter(Context context,ArrayList<String>groupNames,HashMap<String,ArrayList> events_and_people_found)
        {
            this.context = context;
            this.type_of_List = groupNames;
            this.events_and_people_found = events_and_people_found;
        }

        @Override
        public int getGroupCount() {
            return type_of_List.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            //it is a life event
            //NEED TO CHANGE BELOW
            if(getGroupCount() == 2) {
                if (groupPosition == 0) {
                    return events_and_people_found.get(people_title).size();
                } else {
                    return events_and_people_found.get(event_title).size();
                }
            }
            else if(getGroupCount() == 1)
            {
                //if hashmap.contains person_key
                    //return hashmap.get(person_key).size() should return array size
                //else if hashmap.contains event_key
                    //return  //return hashmap.get(event_key).size() should return array size
                if(events_and_people_found.containsKey(people_title))
                {
                    return events_and_people_found.get(people_title).size();
                }
                else {
                    return events_and_people_found.get(event_title).size();
                }

            }
            else {

                return 0;
            }

        }

        //specify group position return the title or string of events or family
        //make a string in the string resource file R.String.example

        //if else to determine if its events or family
        @Override
        public Object getGroup(int groupPosition) {

            if(getGroupCount() == 2) {
                if (groupPosition == 0) {
                    //this will be a person
                    return getString(R.string.group_title_family);

                } else {
                    //else the this group is events
                    return getString(R.string.group_title_event);
                }
            }
            else if(getGroupCount() == 1)
            {
                if(events_and_people_found.containsKey(people_title))
                {
                    return getString(R.string.group_title_family);
                }
                else {
                    return getString(R.string.group_title_event);
                }


            }
            else{
                //possibly change
                return null;
            }

        }

        //given the group position you will know if its an event or family
        //
        @Override
        public Object getChild(int groupPosition, int childPosition) {

           //every scenerio captured if two titles
            if(type_of_List.size() == 2)
            {
                if(type_of_List.get(0).equals(people_title))
                {
                    //get array of children from hashmap
                    //get child either event or person
                    return events_and_people_found.get(people_title).get(childPosition);
                }
                else {

                    //get child either event or person
                    return events_and_people_found.get(event_title).get(childPosition);
                }


            }
            else if(type_of_List.size() == 1 && type_of_List.contains(people_title)) {

                return events_and_people_found.get(people_title).get(childPosition);
            }
            else {
                return events_and_people_found.get(event_title).get(childPosition);
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



            if((getGroupCount() == 2 && groupPosition == 1) || (getGroupCount() == 1 && type_of_List.contains(event_title)) )
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
            else if(getGroupCount() == 2 && groupPosition == 0 || (getGroupCount() == 1 && type_of_List.contains(people_title)))
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