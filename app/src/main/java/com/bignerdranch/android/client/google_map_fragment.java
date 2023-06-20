package com.bignerdranch.android.client;


import static androidx.databinding.DataBindingUtil.setContentView;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.Event;
import model.Person;

public class google_map_fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private View view;

    private Space space;
    private TextView text;

    private MenuItem upButton;

    private Polyline line;

    private ArrayList<Polyline> list_of_lines = new ArrayList<>();
    private Map<String, Marker> map_of_markers = new HashMap<>();

    private Map<String, String> map_event_to_person = new HashMap<>();

    private Map<String, Event> map_event_id_to_event = new HashMap<>();

    private String BUNDLE_EVENT_ID = "eventID";

    private String BUNDLE_PERSON_ID = "personID";
    private String eventID = "";

    private String personID = "";

    private String ID_for_person_activity = "personID";

    private settings_data settings;

    private ArrayList<Integer> colorsArray;

    private ArrayList<String> different_line_strings;

    private final int GREEN = 0;
    private final int RED = 2;
    private final int BLUE = 1;

    private HashMap<String, Integer> lineColorMap;

    private dataCache family_tree_data;

    public static google_map_fragment getNewInstanceOfMapFrag(String eventID, String personID) {
        //initializing map for events to click markers programmatically


        google_map_fragment google_map_frag = new google_map_fragment();

        Bundle items = new Bundle();
        items.putString("eventID", eventID);
        items.putString("personID", personID);
        google_map_frag.setArguments(items);


        return google_map_frag;
    }

    private void setLineColors() {
        colorsArray = new ArrayList<>(Arrays.asList(GREEN, RED, BLUE));

        different_line_strings = new ArrayList<>(Arrays.asList("life story", "family tree", "spouse line"));


        for (int i = 0; i < different_line_strings.size(); i++) {
            lineColorMap.put(different_line_strings.get(i), colorsArray.get(i));

        }

        if (getArguments() != null) {
            Bundle args = getArguments();
            eventID = args.getString(BUNDLE_EVENT_ID);
            personID = args.getString(BUNDLE_PERSON_ID);
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(layoutInflater, container, savedInstanceState);
        //map with three line colors
        lineColorMap = new HashMap<>();
        //create line colors
        setLineColors();

        //if a user has clicked on an event in person activity activate the
        //menu bar
        if (eventID.equals("")) {
            setHasOptionsMenu(true);
        }

        //info on switches states
        settings = settings_data.getInstance();
        Log.d("onChecked_life_story","when in on create Boolean = " + settings.getLife_story_line());
        family_tree_data = dataCache.getInstance();


        view = layoutInflater.inflate(R.layout.fragment_maps, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);




    }


    private MarkerOptions setEventTypeColors(MarkerOptions newMarker, String eventType, dataCache data) {
        String colorHue = null;
        if (!data.getEventMarkerColors().containsKey(eventType)) {

            boolean colorDoesExist = true;

            do {
                Random rand = new Random();
                int upperBound = 10;
                int random_int = rand.nextInt(upperBound);

                colorHue = data.getColorHues().get(random_int);

                if (!data.getEventMarkerColors().containsValue(colorHue)) {
                    data.getEventMarkerColors().put(eventType, colorHue);
                    colorDoesExist = false;

                }

            } while (colorDoesExist);
        } else {

            colorHue = (String) data.getEventMarkerColors().get(eventType);
        }

        setColorHueForMarker(newMarker, colorHue);


        return newMarker;
    }

    private MarkerOptions setColorHueForMarker(MarkerOptions newMarker, String colorHue) {

        switch (colorHue) {
            case "HUE_BLUE":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;
            case "HUE_MAGENTA":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case "HUE_RED":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
            case "HUE_CYAN":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                break;
            case "HUE_GREEN":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            case "HUE_AZURE":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                break;
            case "HUE_ORANGE":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                break;
            case "HUE_ROSE":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                break;
            case "HUE_VIOLET":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                break;
            case "HUE_YELLOW":
                newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;


        }


        return newMarker;
    }


    @Override
    public void onMapLoaded() {
//        try {


        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.




        text = view.findViewById(R.id.mapTextView);

        dataCache data = dataCache.getInstance();
        data.getFamilyTree().get(data.getUsername());

        //markers are being added
        for (Person person : data.getListOfPeople()) {
            Person currentPerson = (Person) data.getPersonMap().get(person.getPersonID());

            ArrayList<model.Event> eventArray = (ArrayList<model.Event>) data.getEventMap().get(person.getPersonID());

            for (int i = 0; i < eventArray.size(); i++) {
                int currentIndex = i;


                //marker that is null below
//                if(eventArray.get(i).getEventID().equals("af5f472c-d979-4199-b12f-0c501517da08"))
//                {
//                    Log.d("Inside if statement","Found matching EVENTID for invisible marker");
//                }
                LatLng city = new LatLng(eventArray.get(i).getLatitude(), eventArray.get(i).getLongitude());


                //New marker is being set and then color being set as well
                MarkerOptions newMarker = new MarkerOptions();

                String eventType = eventArray.get(i).getEventType().toLowerCase();

                String title = person.getFirstName() + " " + person.getLastName() + "\n"
                        + eventType.toUpperCase() + ": " + (String) (eventArray.get(i).getCity() + ", " + (String) (eventArray.get(i).getCountry()))
                        + " (" + eventArray.get(i).getYear() + ")";


                //setting dynamic color for eventMarker
                setEventTypeColors(newMarker, eventType, data);


                String MoreInfo = title;


                Marker marker = map.addMarker(newMarker.position(city).snippet(MoreInfo));

                marker.setVisible(true);
                marker.setTag(eventArray.get(i));

                marker.setTitle(person.getPersonID());


                //passing digit that represents male or female to onClickListener using setAlpha
//                if (person.getGender().equals("m")) {
//                    marker.setAlpha(1);
//                } else {
//                    marker.setAlpha(0);
//                }

                //hiding title and using title as transportation for PersonID
                marker.hideInfoWindow();

                //adding all markers to a map
                map_event_id_to_event.put(eventArray.get(i).getEventID(),eventArray.get(i));
                map_of_markers.put(eventArray.get(i).getEventID(), marker);
                map_event_to_person.put(eventArray.get(i).getEventID(), currentPerson.getPersonID());

            }


        }



        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {


                text.setText(marker.getSnippet());
                Person person = (Person) family_tree_data.getPersonMap().get(marker.getTitle());

                //using alpha method and digit passed to determine male or female
                if (person.getGender().equalsIgnoreCase("m")) {
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male_48_xhdpi, 0, 0, 0);
                } else {
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female_48_xhdpi, 0, 0, 0);
                }
                //end of if statement


                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));


                // Log.d("onChecked_life_story","Boolean = " + settings.getLife_story_line());

                //removes any lines before traversing again
                if(line != null) {
                    Log.d("Line","Line is being called to be removed");

                    for (Polyline currentLine: list_of_lines) {

                        Log.d("Line_for_loop","CurrentLine = " + currentLine);
                        currentLine.remove();
                    }
                }

//                Marker marker1 = map_of_markers.get("af5f472c-d979-4199-b12f-0c501517da08");
//
//                marker.setVisible(true);
//                if(marker1 == null)
//                {
//                    Log.d("Marker_null","Marker which was set is now null");
//                }

                //check current switch states
                checkSettings(marker);

                //textview on click listener below
                text.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), person_activity.class);


                        intent.putExtra(ID_for_person_activity, marker.getTitle());
                        startActivity(intent);

                    }
                });
                //end of textView box click listener


                return true;


            }
        });



        //Technically or at least in the past textViews were not automatically clickable
        //text.setClickable(true);

        //move camera to event that was clicked on person activity if
        //eventID exist which was passed from personActivity
        if (!eventID.equalsIgnoreCase("")) {

            ArrayList<Event> eventList = (ArrayList<Event>) data.getEventMap().get(personID);

            for (Event currentEvent : eventList) {
                if (currentEvent.getEventID().equals(eventID)) {
                    //setting arguments for CameraUpdateFactory
                    //   Marker currentMarker = map_of_markers.get(eventID);
                    LatLng latLng = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                    float zoom = 3;

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                    Marker currentMarker = map_of_markers.get(eventID);

                    text.setText(currentMarker.getSnippet());
                    //using alpha method and digit passed to determine male or female
                    if (currentMarker.getAlpha() == 1) {
                        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male_48_xhdpi, 0, 0, 0);
                    } else {
                        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female_48_xhdpi, 0, 0, 0);
                    }
                    //end of if statement


                }
            }


        }




//        }
//        catch(Exception e) {
//
//            e.printStackTrace();
//        }
    }

    private void checkSettings(Marker marker) {
        if (settings.getLife_story_line()) {
            drawLifeStoryLines(marker);
        }
        if (settings.getFamily_tree_line())
        {
            drawFamilyLines(marker);
        }
        if (settings.getSpouse_line())
        {
            drawSpouseLines(marker);
        }
        if(settings.getFather_side_line())
        {
            //map.clear();
            //filter out
            filterOutFatherSide(settings.getFather_side_line());

        }
        if(!settings.getFather_side_line())
        {
            filterOutFatherSide(settings.getFather_side_line());
        }
        if(settings.getMother_side())
        {
            filterOutMotherSide(settings.getMother_side());

        }
        if(!settings.getMother_side())
        {
            filterOutMotherSide(settings.getMother_side());

        }
//        if(settings.getMale_event_side())
//        {
//            filterOnlyMaleEvents(settings.getMale_event_side());
//        }
//
//        if(!settings.getMale_event_side())
//        {
//            filterOnlyMaleEvents(settings.getMale_event_side());
//        }
//        if(settings.getFemale_event_side())
//        {
//            filterOnlyFemaleEvents(settings.getFemale_event_side());
//        }
//
//        if(!settings.getFemale_event_side())
//        {
//            filterOnlyFemaleEvents(settings.getFemale_event_side());
//        }



    }

    void filterOnlyFemaleEvents(Boolean isVisible)
    {
        if(family_tree_data.getEventMap() != null) {

            Map<String,ArrayList<Event>> eventMap = family_tree_data.getEventMap();
            for (Map.Entry<String, Event> entry : map_event_id_to_event.entrySet())
            {
                String personAssociatedWithEvent = entry.getValue().getPersonID();

                if(family_tree_data.getPersonMap().containsKey(personAssociatedWithEvent))
                {
                    Person person = (Person) family_tree_data.getPersonMap().get(personAssociatedWithEvent);
                    if(person.getGender().equalsIgnoreCase("m"))
                    {
                        map_of_markers.get(entry.getKey()).setVisible(false);
                    }

                }

            }
        }

    }

    void filterOnlyMaleEvents(Boolean isVisible)
    {
        if(family_tree_data.getEventMap() != null) {

            Map<String,ArrayList<Event>> eventMap = family_tree_data.getEventMap();
            for (Map.Entry<String, Event> entry : map_event_id_to_event.entrySet())
            {
                String personAssociatedWithEvent = entry.getValue().getPersonID();

                if(family_tree_data.getPersonMap().containsKey(personAssociatedWithEvent))
                {
                    Person person = (Person) family_tree_data.getPersonMap().get(personAssociatedWithEvent);
                    if(person.getGender().equalsIgnoreCase("f"))
                    {
                        map_of_markers.get(entry.getKey()).setVisible(false);
                    }

                }

            }
        }

    }

    void filter_invisible_by_gender_helper(ArrayList<Event> list_of_events_to_remove)
    {
        for (Event currentEvent: list_of_events_to_remove) {

            if(map_of_markers.containsKey(currentEvent.getEventID()))
            {
                map_of_markers.get(currentEvent.getEventID()).setVisible(false);
            }

        }

    }

    void filter_visible_by_gender_helper(ArrayList<Event> list_of_events_to_remove)
    {
        for (Event currentEvent: list_of_events_to_remove) {

            if(map_of_markers.containsKey(currentEvent.getEventID()))
            {
                map_of_markers.get(currentEvent.getEventID()).setVisible(true);
            }

        }

    }

    void filterOutFatherSide(Boolean filterOn)
    {

        if(family_tree_data.getMaternal_events() != null)
        {
          //  ArrayList<Event> list_of_events =
           Map<String,ArrayList<Event>> maternal_events = family_tree_data.getMaternal_events();



            for (Map.Entry<String,ArrayList<Event>> entry : maternal_events.entrySet())
            {


                if(entry.getValue() != null && filterOn)
                {
                    filter_helper_make_invisible(entry.getValue());
                }
                else if(entry.getValue() != null && !filterOn)
                {
                    filter_helper_make_visible(entry.getValue());
                }

            }
        }
    }

    void filterOutMotherSide(Boolean filterOn)
    {

        if(family_tree_data.getPaternal_events() != null)
        {
            //  ArrayList<Event> list_of_events =
            Map<String,ArrayList<Event>> paternal_events = family_tree_data.getPaternal_events();



            for (Map.Entry<String,ArrayList<Event>> entry : paternal_events.entrySet())
            {


                if(entry.getValue() != null && filterOn)
                {
                    filter_helper_make_invisible(entry.getValue());
                }
                else if(entry.getValue() != null && !filterOn)
                {
                    filter_helper_make_visible(entry.getValue());
                }

            }
        }
    }

    private void filter_helper_make_invisible(ArrayList<Event> events_list)
    {
        for (Event CurrentEvent: events_list) {

            if(map_of_markers.containsKey(CurrentEvent.getEventID()))
            {

                Marker marker = map_of_markers.get(CurrentEvent.getEventID());
                marker.setVisible(false);


            }
        }

    }
    private void filter_helper_make_visible(ArrayList<Event> events_list)
    {
        for (Event CurrentEvent: events_list) {

            if(map_of_markers.containsKey(CurrentEvent.getEventID()))
            {

                Marker marker = map_of_markers.get(CurrentEvent.getEventID());
                marker.setVisible(true);


            }
        }

    }


    private void drawLifeStoryLines(Marker marker) {
        //map.setMyLocationEnabled(true);

        Log.d("DrawLifeStoryLine","function called value = " + marker.getTitle());

        String personId = marker.getTitle();
        ArrayList<Event> persons_events;

        persons_events = (ArrayList<Event>) family_tree_data.getEventMap().get(personId);

        //check to see if array of events returns exists
        Log.d("DrawLifeStoryLine","person_events array value = " + persons_events);

            PolylineOptions life_line = new PolylineOptions();
            life_line.color(R.color.purple_200).width(10).visible(true);


            for (Event currentEvent : persons_events) {
                LatLng lat = new LatLng(currentEvent.getLatitude(),currentEvent.getLongitude());
                Log.d("DrawLifeStoryLine","EventID = " + currentEvent.getEventID());
                Log.d("DrawLifeStoryLine","personID = " + currentEvent.getPersonID());
                Log.d("DrawLifeStoryLine","Event Type = " + currentEvent.getEventType());

                if (map_of_markers.get(currentEvent.getEventID()).getPosition() != null) {

                    Log.d("DrawLifeStoryLine","Position of event = " + map_of_markers.get(currentEvent.getEventID()).getPosition() );
                    life_line.add(lat);

                }


            }

            Log.d("DrawLifeStoryLine","PolyLine = " + life_line.getPoints()  +" plus boolean = " +  life_line.isVisible()+ "width = "+ life_line.getWidth());


         line = map.addPolyline(life_line);

        list_of_lines.add(line);

        line.isClickable();
        line.setVisible(true);
        if(line.isVisible())
        {
            Log.d("DrawLifeStoryLine","should be visible");
        }
    }

    //pass Latlng instead of marker
    private LatLng drawLineToFathersMostRecentEvent(LatLng sourcePosition, Person father,int width)
    {

        LatLng updatedPosition = null;
        //array not null check
        if(family_tree_data.getEventMap().get(father.getPersonID()) != null)
        {
            ArrayList<Event> fathers_events = (ArrayList<Event>) family_tree_data.getEventMap().get(father.getPersonID());
            //just an extra check to make sure array is not null
            if(fathers_events.get(0) != null) {

                Event MostRecentevent = fathers_events.get(0);
                LatLng mostCurrentEventPosition = new LatLng(MostRecentevent.getLatitude(),MostRecentevent.getLongitude());


                PolylineOptions life_line = new PolylineOptions();
                life_line.color(R.color.black).width(width).visible(true);

                //start point is at selected marker
                life_line.add(sourcePosition);
                //next point is to father most recent events
                life_line.add(mostCurrentEventPosition);

                //this will return as the current position to branch off in recursion
                updatedPosition = mostCurrentEventPosition;

                line = map.addPolyline(life_line);

                list_of_lines.add(line);

                line.setVisible(true);
            }


        }

        return updatedPosition;

    }

    private LatLng drawLineToMothersMostRecentEvent(LatLng sourcePosition, Person mother,int width)
    {

        LatLng updatedPosition = null;
        //array not null check
        if(family_tree_data.getEventMap().get(mother.getPersonID()) != null)
        {
            ArrayList<Event> mothers_events = (ArrayList<Event>) family_tree_data.getEventMap().get(mother.getPersonID());
            //just an extra check to make sure array is not null
            if(mothers_events.get(0) != null) {

                Event MostRecentevent = mothers_events.get(0);
                LatLng mostCurrentEventPosition = new LatLng(MostRecentevent.getLatitude(),MostRecentevent.getLongitude());


                PolylineOptions life_line = new PolylineOptions();
                life_line.color(R.color.black).width(width).visible(true);

                //start point is at selected marker
                life_line.add(sourcePosition);
                //next point is to father most recent events
                life_line.add(mostCurrentEventPosition);

                updatedPosition = mostCurrentEventPosition;

                line = map.addPolyline(life_line);

                list_of_lines.add(line);

                line.setVisible(true);
            }


        }

        return updatedPosition;

    }

    private void familyLineRecursion(LatLng sourcePosition, Person Person, int width)
    {
        //decrement the width
        if(width>=0)
        {
            int tempVal = width = width - 4;
            if(tempVal > 0) {
                width = width - 4;
            }
        }
        //get father and mother object of person thats been passed through
        String personID = Person.getPersonID();
        ArrayList<Person> array_of_parents;

        array_of_parents = family_tree_data.getParents_of_person().get(personID);

        Person father = null;
        Person mother = null;

        //iterate through all possible parents
        for (Person currentPerson: array_of_parents)
        {
            if(currentPerson.getGender().equalsIgnoreCase("m"))
            {
                father = currentPerson;
            }
            else if(currentPerson.getGender().equalsIgnoreCase("f"))
            {
                mother = currentPerson;
            }
        }

        if(father != null)
        {

            LatLng newPosition;
            newPosition = drawLineToFathersMostRecentEvent(sourcePosition,father,width);
            if(newPosition != null)
            {
                familyLineRecursion(newPosition,father,width);
            }
        }

        if(mother != null)
        {
            LatLng newPosition;
            newPosition = drawLineToMothersMostRecentEvent(sourcePosition,mother,width);

            if(newPosition != null)
            {
                familyLineRecursion(newPosition,mother,width);
            }
        }


    }

    private void drawFamilyLines(Marker marker) {

    //get father and mother object
        LatLng currentPosition = marker.getPosition();
        String personID = marker.getTitle();
        ArrayList<Person> array_of_parents;
        int width = 30;

        array_of_parents = family_tree_data.getParents_of_person().get(personID);

        Person father = null;
        Person mother = null;

        //iterate through all possible parents
        for (Person currentPerson: array_of_parents)
        {
                if(currentPerson.getGender().equalsIgnoreCase("m"))
                {
                    father = currentPerson;
                }
                else if(currentPerson.getGender().equalsIgnoreCase("f"))
                {
                    mother = currentPerson;
                }
        }

        if(father != null)
        {
           LatLng nextPosition = drawLineToFathersMostRecentEvent(currentPosition,father,width);


           if(nextPosition != null)
           {
               familyLineRecursion(nextPosition,father,width);

           }

        }

        if(mother != null)
        {
            LatLng nextPosition = drawLineToMothersMostRecentEvent(currentPosition,mother,width);
            if(nextPosition != null)
            {
                familyLineRecursion(nextPosition,mother,width);

            }
        }



        //start of our roots for recursion mother and father






    }


    private void drawSpouseLines(Marker marker) {
        //map.setMyLocationEnabled(true);

        Log.d("SpouseLine","function called value = " + marker.getTitle());

        String personId = marker.getTitle();
        ArrayList<Person> persons_events;
        //getting hashmapp that finds a persons spouse
        if(family_tree_data.getSpouse_of_person().get(personId) != null)
        {
            persons_events = family_tree_data.getSpouse_of_person().get(personId);

            //unless there is polygamy array should only have one spouse

            Person spouse = persons_events.get(0);


            Log.d("SpouseLine", "Spouse in array id = " + spouse.getPersonID());
            //preparing array of events of spouse
            ArrayList<Event> spouses_events;
            //using spouse id to find all her events in sorted order from most recent
            if (family_tree_data.getEventMap().get(spouse.getPersonID()) != null) {
                spouses_events = (ArrayList<Event>) family_tree_data.getEventMap().get(spouse.getPersonID());

                //the events should be sorted regardless of whether there is birth
                //so first should be the most recent event
                if (spouses_events.get(0) != null) {
                    Event mostRecentEvent = spouses_events.get(0);

                    //check to see if array of events returns exists
                    Log.d("SpouseLine", "spouses event type is = " + mostRecentEvent.getEventType());

                    PolylineOptions life_line = new PolylineOptions();
                    life_line.color(R.color.teal_200).width(10).visible(true);
                    LatLng spousesEventLocation = new LatLng(mostRecentEvent.getLatitude(), mostRecentEvent.getLongitude());
                    life_line.add(marker.getPosition());
                    life_line.add(spousesEventLocation);


                    Log.d("SpouseLine", "PolyLine = " + life_line.getPoints() + " plus boolean = " + life_line.isVisible() + "width = " + life_line.getWidth());


                    line = map.addPolyline(life_line);

                    list_of_lines.add(line);

                    line.isClickable();
                    line.setVisible(true);
                }
            }
        }
    }


    @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
        {
            Log.d("OnCreateOptionsMenu","onCreateOptionsMenu Called");
            menuInflater.inflate(R.menu.main_menu, menu);

        }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
            Toast toast = new Toast(getContext());
            toast.setDuration(Toast.LENGTH_SHORT);

            switch (item.getItemId()) {
            case R.id.SearchMenuItem:  {

                //Intent intent = new Intent(getContext(),searchActivity.class);
                Intent intent = new Intent(getContext(),search_recycler_activity.class);
                startActivity(intent);

                // Navigate to search screen.
                return true;
            }
            case R.id.SettingMenuItem: {
                // Settings

                Intent intent = new Intent(getContext(),settings_activity.class);

                startActivity(intent);
                return true;
            }
                default:
                    return super.onOptionsItemSelected(item);


        }

    }

    }


