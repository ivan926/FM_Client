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

import com.bignerdranch.android.client.databinding.ActivityMainBinding;
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
    private Map<String, Marker> map_of_markers = new HashMap<>();

    private Map<String, String> map_event_to_person = new HashMap<>();

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
                int upperBound = 9;
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


                marker.setTag(eventArray.get(i));

                marker.setTitle(person.getPersonID());


                //passing digit that represents male or female to onClickListener using setAlpha
                if (person.getGender().equals("m")) {
                    marker.setAlpha(1);
                } else {
                    marker.setAlpha(0);
                }

                //hiding title and using title as transportation for PersonID
                marker.hideInfoWindow();

                //adding all markers to a map
                map_of_markers.put(eventArray.get(i).getEventID(), marker);
                map_event_to_person.put(eventArray.get(i).getEventID(), currentPerson.getPersonID());

            }


        }


        //Use below if you are having issues with your own marker
//            LatLng sydney = new LatLng(-34, 151);
//            map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {


                text.setText(marker.getSnippet());
                //using alpha method and digit passed to determine male or female
                if (marker.getAlpha() == 1) {
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male_48_xhdpi, 0, 0, 0);
                } else {
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female_48_xhdpi, 0, 0, 0);
                }
                //end of if statement


                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

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

        Toast toast = Toast.makeText(this.getContext(), "Going to person activity", Toast.LENGTH_SHORT);

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




    }

    private void checkSettings(Marker marker) {
        if (settings.getLife_story_line()) {
            drawLifeStoryLines(marker);

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
            life_line.color(0x1111).width(5).visible(true);


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


        Polyline line = map.addPolyline(life_line);

        line.isClickable();
        line.setVisible(true);
        if(line.isVisible())
        {
            Log.d("DrawLifeStoryLine","should be visible");
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
//
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


