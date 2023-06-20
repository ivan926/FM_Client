package com.bignerdranch.android.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import model.Event;
import model.Person;

public class search_recycler_activity extends AppCompatActivity {

    private SearchView searchBar;
    private String event_title = "event";
    private String people_title = "people";
    private dataCache family_tree_data;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        family_tree_data = dataCache.getInstance();

        setContentView(R.layout.recycler_layout_search);




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

                search(newText);
                return true;
            }
        });



    }

    private void search(String query)
    {
        //hashmap that will contain title as key and array of events and people
        HashMap<String, ArrayList> found_items;

        //convert input into lower case
        query = query.toLowerCase();

        //determine whether you will have person or events or both
        ArrayList<String> nameOfGroups = new ArrayList<>();


        //will return hashmap of group_name being the keys and event list or event list
        //also returns arraylist of name of groups which determines if any events or people
        //were even found when iterating through all events or people
        found_items = begin_query(nameOfGroups,query);


        RecyclerView recyclerView = findViewById(R.id.recycler_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search_recycyler adapter = new search_recycyler(this,nameOfGroups,found_items);
        recyclerView.setAdapter(adapter);


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

            String first_name = currentPerson.getFirstName().toLowerCase();
            String last_name =  currentPerson.getLastName().toLowerCase();
            String gender = currentPerson.getGender().toLowerCase();
            if(!query.equals("") && (first_name.contains(query) || last_name.contains(query) || gender.contains(query)))
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

            if(!query.equals("") && (type.contains(query) || city.contains(query) || country.contains(query)
                    || year.contains(query) || associatedUsername.contains(query)))
            {
                events_array.add(currentEvent);
                found_objects.put(event_title,events_array);
            }


        }






    }

    private class search_recycyler extends RecyclerView.Adapter<searchViewHolder> {

        private final int EVENTS = 1;
        private final int PERSON = 0;


        //create variables that will carry data array list and what not
        private final Context context;
        private ArrayList<String> type_of_List;
        private HashMap<String,ArrayList> events_and_people_found;

        search_recycyler(Context context,ArrayList<String>groupNames,HashMap<String,ArrayList> events_and_people_found) {
            this.context = context;
            this.type_of_List = groupNames;
            this.events_and_people_found = events_and_people_found;

        }

        @Override
        public int getItemViewType(int position) {
            int event_size = 0;
            if(events_and_people_found.get(people_title) != null)
            {
                event_size = events_and_people_found.get(people_title).size();
            }
            return position < event_size ? PERSON : EVENTS;

        }

        @NonNull
        @Override
        public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;

            if(viewType == PERSON) {
                view = getLayoutInflater().inflate(R.layout.person_list_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_list_item, parent, false);
            }


            return new searchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull searchViewHolder holder, int position) {

            int event_size = 0;
            if(events_and_people_found.get(people_title) != null)
            {
                event_size = events_and_people_found.get(people_title).size();
            }

            if(position < event_size) {
                holder.bind((Person) events_and_people_found.get(people_title).get(position));
            } else {
                holder.bind((Event)events_and_people_found.get(event_title).get(position - event_size));
            }

        }

        @Override
        public int getItemCount() {

            //if item count is zero, recycler is cleared no other
            //methods are called
            int total_size = 0;
            if(events_and_people_found.get(event_title) != null && events_and_people_found.get(people_title) != null) {
                total_size = events_and_people_found.get(event_title).size() + events_and_people_found.get(people_title).size();
            }
            else if(events_and_people_found.get(event_title) != null)
            {
                total_size = events_and_people_found.get(event_title).size();
            }
            else if(events_and_people_found.get(people_title) != null)
            {
                total_size = events_and_people_found.get(people_title).size();
            }

            return total_size;

        }
    }


    private class searchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView top_text;
        private final TextView bottom_text;

        private final int viewType;

        private Person person;

        private Event event;

        private final int EVENTS = 1;
        private final int PERSON = 0;


        searchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON) {
                icon = itemView.findViewById(R.id.person_image_child);
                top_text = itemView.findViewById(R.id.person_detail_above);
                bottom_text = itemView.findViewById(R.id.person_detail_below);
            } else {
                icon = itemView.findViewById(R.id.event_image_child);
                top_text = itemView.findViewById(R.id.event_detail_above);
                bottom_text = itemView.findViewById(R.id.event_detail_below);
            }
        }

        private void bind(Event event) {
            this.event = event;
            icon.setBackgroundResource(R.drawable.map_marker_48);
            String top_info = event.getEventType() +": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            top_text.setText(top_info);
            Person CurrentPerson = (Person)family_tree_data.getPersonMap().get(event.getPersonID());
            //we need to use this global person later
            this.person = CurrentPerson;
            String bottom_info = CurrentPerson.getFirstName() + " " + CurrentPerson.getLastName();
            bottom_text.setText(bottom_info);
        }

        private void bind(Person person) {
            this.person = person;
            if(person.getGender().equalsIgnoreCase("m")) {
                icon.setBackgroundResource(R.drawable.male_48_xhdpi);
            }
            else {
                icon.setBackgroundResource(R.drawable.female_48_xhdpi);
            }
            String fullName = person.getFirstName() + " " + person.getLastName();
            top_text.setText(fullName);
            bottom_text.setText("");
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON) {
                Person familyMember = this.person;
                Intent intent = new Intent(context,person_activity.class);

                intent.putExtra("personID", familyMember.getPersonID());
                context.startActivity(intent);

            } else {

                Event Currentevent = this.event;

                Intent intent = new Intent(context, main_activity_container.class);
                Bundle extras = new Bundle();
                extras.putString("eventID", Currentevent.getEventID());
                extras.putString("personID", Currentevent.getPersonID());

                intent.putExtras(extras);
                context.startActivity(intent);
            }
        }
    }




}