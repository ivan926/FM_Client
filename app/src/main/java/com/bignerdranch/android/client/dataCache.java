package com.bignerdranch.android.client;

import android.provider.Contacts;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class dataCache {

    private static dataCache instance;
    private String username;


    static dataCache getInstance()
    {
        if(instance == null)
        {
            instance = new dataCache();

        }

        return instance;

    }


    public model.Person[] getListOfPeople() {
        return listOfPeople;
    }

    public model.Event[] getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfPeople(model.Person[] listOfPeople,String username, model.Event[] listOfEvents) {
        this.username = username;
        this.listOfEvents = listOfEvents;
        this.listOfPeople = listOfPeople;
       // this.listOfPeople = (ArrayList<model.Person>)Arrays.asList(listOfPeople);

        sortList(listOfEvents,listOfPeople);
        addToMap(this.listOfPeople,username);
    }

    private void sortList(Event[] listOfEvents, Person[] listOfPeople) {

        PersonMap = new HashMap<>();
        EventMap = new HashMap();

        for(Person person : listOfPeople)
        {
            ArrayList<Event> arrayOfEvents = new ArrayList<>();
            this.PersonMap.put(person.getPersonID(),person);

            for(Event event : listOfEvents)
            {
                if(person.getPersonID().equals(event.getPersonID()))
                {
                    arrayOfEvents.add(event);

                }

            }

            this.EventMap.put(person.getPersonID(), arrayOfEvents);


        }

        setPrimerEventColors();

    }

    private void setPrimerEventColors()
    {
        EventMarkerColors = new HashMap();

        ArrayList<String> arrayOfEventName = new ArrayList<String>()
        {
            {
                add("death");
                add("birth");
                add("marriage");
            }
        };

        int upperBound = 9;

        Random randInt = new Random();

        for(String event : arrayOfEventName) {

            while (true) {

                int chosenInt = randInt.nextInt(upperBound);

                String colorHue = colorHues.get(chosenInt);

                if (!EventMarkerColors.containsValue(colorHue)) {

                    EventMarkerColors.put(event,colorHue);
                    break;
                }
            }

        }





    }


    public String getUsername()
    {
        return this.username;
    }


    private void addToMap(model.Person[] list, String username)
    {
        Familymap.put(username,list);
    }


    public Map getFamilyTree()
    {
        return this.Familymap;
    }

    public Map getEventMap() {
        return EventMap;
    }

    public Map getEventMarkerColors() {
        return EventMarkerColors;
    }

    public ArrayList<String> getColorHues() {
        return colorHues;
    }

    public Map getPersonMap() {
        return PersonMap;
    }

    private model.Person[] listOfPeople;
    private model.Event[] listOfEvents;

    private Map Familymap;

    private Map EventMap;

    private Map PersonMap;

    private ArrayList<String> colorHues;

    private Map EventMarkerColors;



    private dataCache(){

        Familymap = new HashMap();
        colorHues = new ArrayList<>();

        colorHues.add("HUE_BLUE");
        colorHues.add("HUE_MAGENTA");
        colorHues.add("HUE_RED");
        colorHues.add("HUE_CYAN");
        colorHues.add("HUE_GREEN");
        colorHues.add("HUE_AZURE");
        colorHues.add("HUE_ORANGE");
        colorHues.add("HUE_ROSE");
        colorHues.add("HUE_VIOLET");
        colorHues.add("HUE_YELLOW");

    }
}
