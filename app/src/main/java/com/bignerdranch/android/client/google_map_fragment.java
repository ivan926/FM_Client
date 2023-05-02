package com.bignerdranch.android.client;


import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bignerdranch.android.client.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import model.Event;
import model.Person;

public class google_map_fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
        private GoogleMap map;
        private View view;
        private TextView text;


        @Override
        public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(layoutInflater, container, savedInstanceState);

            view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            return view;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setOnMapLoadedCallback(this);








        }


        private MarkerOptions setEventTypeColors(MarkerOptions newMarker,String eventType,dataCache data)
        {
            String colorHue = null;
            if(!data.getEventMarkerColors().containsKey(eventType))
            {

                boolean colorDoesExist = true;

                do{
                    Random rand = new Random();
                    int upperBound = 9;
                    int random_int = rand.nextInt(upperBound);

                    colorHue = data.getColorHues().get(random_int);

                    if(!data.getEventMarkerColors().containsValue(colorHue))
                    {
                        data.getEventMarkerColors().put(eventType,colorHue);
                        colorDoesExist = false;

                    }

                }while(colorDoesExist);
            }
            else {

                colorHue = (String)data.getEventMarkerColors().get(eventType);
            }

            setColorHueForMarker(newMarker,colorHue);


            return newMarker;
        }

        private MarkerOptions setColorHueForMarker(MarkerOptions newMarker,String colorHue)
        {

            switch (colorHue){
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


            for(Person person : data.getListOfPeople() )
            {
                Person currentPerson = (Person) data.getPersonMap().get(person.getPersonID());

                ArrayList<model.Event> eventArray = (ArrayList<model.Event>) data.getEventMap().get(person.getPersonID());

                for(int i = 0; i < eventArray.size();i++)
                {  int currentIndex = i;


                    LatLng city = new LatLng(eventArray.get(i).getLatitude(), eventArray.get(i).getLongitude());
                    String title = (String)(eventArray.get(i).getCity() + ", " + (String)(eventArray.get(i).getCountry()));


                    //New marker is being set and then color being set as well
                    MarkerOptions newMarker = new MarkerOptions();

                    String eventType = eventArray.get(i).getEventType().toLowerCase();
                    //setting dynamic color for eventMarker
                    //newMarker = setEventTypeColors(newMarker,eventType,data);

                    String colorHue = null;
                    if(!data.getEventMarkerColors().containsKey(eventType))
                    {

                        boolean colorDoesExist = true;

                        do{
                            Random rand = new Random();
                            int upperBound = 9;
                            int random_int = rand.nextInt(upperBound);

                            colorHue = data.getColorHues().get(random_int);

                            if(!data.getEventMarkerColors().containsValue(colorHue))
                            {
                                data.getEventMarkerColors().put(eventType,colorHue);
                                colorDoesExist = false;

                            }

                        }while(colorDoesExist);
                    }
                    else {

                        colorHue = (String)data.getEventMarkerColors().get(eventType);
                    }





                    //NEXT FUNCTIONS STARTS HERE

                    switch (colorHue){
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


                    //ENDS RIGHT HERE

                  String MoreInfo = "";

                    Marker marker = map.addMarker(newMarker.position(city).snippet(MoreInfo));
                    marker.setTag(eventArray.get(i));






                }


                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        text.setText(marker.getSnippet());

                        marker.showInfoWindow();


                        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        return true;
                    }
                });




            }



        }
    }


