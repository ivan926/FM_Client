package com.bignerdranch.android.client;

import android.os.Build;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class dataCache {

    private static dataCache instance;
    private String username;

    private String users_personID;



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



    public void setListOfPeople(model.Person[] listOfPeople,String username, model.Event[] listOfEvents,String users_personID) {
        this.users_personID = users_personID;
        this.username = username;
        this.listOfEvents = listOfEvents;
        this.listOfPeople = listOfPeople;
       // this.listOfPeople = (ArrayList<model.Person>)Arrays.asList(listOfPeople);

        map_events_to_Person(listOfEvents,listOfPeople);
        map_person_to_children();
        map_person_to_parents();
        map_person_to_spouse();
        create_maternal_side_for_user();
        create_paternal_side_for_user();
        addToMap(this.listOfPeople,username);
    }




    private void create_specific_family_side(String users_personID,Map<String,ArrayList<Event>> eventMap)
    {

            if(PersonMap.get(users_personID).getFatherID() != null)
            {
                String personID = PersonMap.get(users_personID).getFatherID();
                eventMap.put(personID,EventMap.get(personID));
                create_specific_family_side(personID,eventMap);
            }
            if(PersonMap.get(users_personID).getMotherID() != null)
            {

                String personID = PersonMap.get(users_personID).getMotherID();
                eventMap.put(personID,EventMap.get(personID));
                create_specific_family_side(personID,eventMap);

            }



    }

    private void create_paternal_side_for_user()
    {

        paternal_events = new HashMap<>();
        String root_father_id = PersonMap.get( this.users_personID).getFatherID();
        paternal_events.put(root_father_id,EventMap.get(root_father_id));
        create_specific_family_side(root_father_id,paternal_events);



    }

    private void create_maternal_side_for_user()
    {

        maternal_events = new HashMap<>();
        String root_mother_id = PersonMap.get( this.users_personID).getMotherID();
        maternal_events.put(root_mother_id,EventMap.get(root_mother_id));
        create_specific_family_side(root_mother_id,maternal_events);



    }

    private void map_person_to_spouse()
    {
        spouse_of_person = new HashMap<String, ArrayList<Person>>();
        ArrayList<Person> spouse_container;
        //when trying to find an individual with no spouse use contains method for map
        for (Map.Entry<String,Person> entry : PersonMap.entrySet())
        {

            if(entry.getValue().getSpouseID() != null)
            {
                String spouseID = entry.getValue().getSpouseID();
                Person spouseObject = PersonMap.get(spouseID);
                spouse_container = new ArrayList<>();

                spouse_container.add(spouseObject);

                spouse_of_person.put(entry.getKey(),spouse_container);



            }


        }

    }

    private void map_person_to_parents()
    {
        parents_of_person = new HashMap<>();

        for (Map.Entry<String,Person> entry : PersonMap.entrySet())
        {

            parents_of_person.put(entry.getKey(),person_to_parents_helper(entry.getValue()));


        }

    }

    private ArrayList<Person> person_to_parents_helper(Person person)
    {
        ArrayList<Person> tempArrayOfparents = new ArrayList<>();


        if(PersonMap.containsKey(person.getFatherID()))
        {
            tempArrayOfparents.add(PersonMap.get(person.getFatherID()));
        }

        if(PersonMap.containsKey(person.getMotherID()))
        {
            tempArrayOfparents.add(PersonMap.get(person.getMotherID()));
        }




        return tempArrayOfparents;

    }

    private void map_person_to_children(){

        children_of_person = new HashMap<>();

            for (Map.Entry<String,Person> entry : PersonMap.entrySet())
            {

                children_of_person.put(entry.getKey(),person_to_children_helper(entry.getKey()));

            }




    }

    private ArrayList<Person> person_to_children_helper(String personID)
    {
            ArrayList<Person> tempArrayOfChildren = new ArrayList<>();


        for (Map.Entry<String,Person> entry : PersonMap.entrySet())
        {


            if(entry.getValue().getFatherID() != null && entry.getValue().getFatherID().equals(personID))
            {
                tempArrayOfChildren.add(entry.getValue());
            }
            else if(entry.getValue().getMotherID() != null && entry.getValue().getMotherID().equals(personID))
            {
                tempArrayOfChildren.add(entry.getValue());
            }
            //maybe add a zero as the first element to indicate no children for individual

        }

        return tempArrayOfChildren;

    }

    //sorting birth and death events if they exist
    private void sortEventshelper(String eventID,ArrayList<Event> arrayOfEvents)
    {
        Boolean swapped = false;

        int birth_index = -1;
        int death_index = 0;

        do{
            swapped = false;
            for(int i = 0 ; i < arrayOfEvents.size()-1;i++)
            {
                if(arrayOfEvents.get(i).getYear() > arrayOfEvents.get(i+1).getYear())
                {   Event tempEvent = arrayOfEvents.get(i);
                    arrayOfEvents.set(i,arrayOfEvents.get(i+1));
                    arrayOfEvents.set(i+1,tempEvent);


                    swapped = true;
                }

                if(arrayOfEvents.get(i).getEventType().equalsIgnoreCase("death"))
                {
                    death_index = i;
                }
                else if(arrayOfEvents.get(i+1).getEventType().equalsIgnoreCase("death"))
                {
                    death_index = i+1;
                }
                if(arrayOfEvents.get(i).getEventType().equalsIgnoreCase("birth"))
                {
                    birth_index = i;
                }
                else if(arrayOfEvents.get(i+1).getEventType().equalsIgnoreCase("birth"))
                {
                    birth_index = i+1;
                }

            }


        }while(swapped);


        if(!arrayOfEvents.get(arrayOfEvents.size()-1).getEventType().equalsIgnoreCase("death") && arrayOfEvents.size() != 1 && death_index != 0)
        {
            Event tempEvent = arrayOfEvents.get(arrayOfEvents.size()-1);
            arrayOfEvents.set(arrayOfEvents.size()-1,arrayOfEvents.get(death_index));
            arrayOfEvents.set(death_index,tempEvent);
        }

        if(!arrayOfEvents.get(0).getEventType().equalsIgnoreCase("birth") && arrayOfEvents.size() != 1 && birth_index != -1)
        {
            Event tempEvent = arrayOfEvents.get(0);
            arrayOfEvents.set(0,arrayOfEvents.get(birth_index));
            arrayOfEvents.set(birth_index,tempEvent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && arrayOfEvents.size() != 1) {
            EventMap.replace(eventID,arrayOfEvents);
        }


    }

    private void sortEvents()
    {

        for (Map.Entry<String,ArrayList<Event>> entry : EventMap.entrySet())
        {

            sortEventshelper(entry.getKey(),entry.getValue());
            //maybe add a zero as the first element to indicate no children for individual

        }

    }

    private void map_events_to_Person(Event[] listOfEvents, Person[] listOfPeople) {
        //also mapping a person ID to a particular person
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

        sortEvents();

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


    private void addToMap(model.Person[] listOfFamilyMembers, String username)
    {
        Familymap.put(username,listOfFamilyMembers);
    }


    public int getSizeOfPersonEvents(String personID)
    {
        return this.EventMap.get(personID).size();
    }

    public ArrayList<Person> getListOfFamilyMembers(String personID)
    {
        ArrayList<Person> list_of_family_person_objects = new ArrayList<Person>();

        for (Person currentMemberOfFamily : this.getParents_of_person().get(personID))
        {
            list_of_family_person_objects.add(currentMemberOfFamily);
        }

        for (Person currentMemberOfFamily : this.getSpouse_of_person().get(personID))
        {
            list_of_family_person_objects.add(currentMemberOfFamily);
        }

        for (Person currentMemberOfFamily : this.getChildren_of_person().get(personID))
        {
            list_of_family_person_objects.add(currentMemberOfFamily);
        }

        return list_of_family_person_objects;

    }

    public String get_relation_to_current_person(String users_personID,String person_in_question)
    {
        String relationship = "";
        //is it a parent?
        if(getParents_of_person().get(users_personID).size() > 0)
        {
            ArrayList<Person> ParentsArray = getParents_of_person().get(users_personID);

            for (Person parent: ParentsArray) {

                if(parent.getPersonID().equals(person_in_question))
                {
                    relationship = parent.getGender().equalsIgnoreCase("m") ? "Father" : "Mother";
                    return relationship;
                }

            }

        }
        if (getSpouse_of_person().get(users_personID).size() != 0)
        {
            ArrayList<Person> tempArray = getSpouse_of_person().get(users_personID);

            for (Person spouse: tempArray) {

                if(spouse.getPersonID().equals(person_in_question))
                {
                    relationship =  "Spouse";

                    return relationship;
                }

            }

        }
        if (getChildren_of_person().get(users_personID).size() > 0)
        {
            ArrayList<Person> ChildArray = getChildren_of_person().get(users_personID);

            for (Person child: ChildArray) {

                if(child.getPersonID().equals(person_in_question))
                {
                    relationship = "Child";
                    return relationship;
                }

            }
        }

        return relationship;

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

    public String getUsers_personID() {
        return users_personID;
    }

    public Map<String,ArrayList<Event>> getMaternal_events()
    {
        return this.maternal_events;
    }

    public Map<String,ArrayList<Event>> getPaternal_events()
    {
        return paternal_events;
    }

    public int getTotalFamilyMembersSize(String personID)
    {
        int family_size = spouse_of_person.get(personID).size() + children_of_person.get(personID).size() + parents_of_person.get(personID).size();

        return family_size;
    }
    private model.Person[] listOfPeople;
    private model.Event[] listOfEvents;

    private Map Familymap;

    private Map<String,ArrayList<Event>> EventMap;

    private Map<String,Person> PersonMap;

    public Map<String, ArrayList<Person>> getChildren_of_person() {
        return children_of_person;
    }

    public Map<String, ArrayList<Person>> getParents_of_person() {
        return parents_of_person;
    }

    public Map<String, ArrayList<Person>> getSpouse_of_person() {
        return spouse_of_person;
    }

    //TODO create getter for maps below
    private Map<String,ArrayList<Person>> children_of_person;
    private Map<String,ArrayList<Person>> parents_of_person;
    private Map<String, ArrayList<Person>> spouse_of_person;

    private Map<String,ArrayList<Event>> paternal_events = new HashMap<>();
    private Map<String,ArrayList<Event>> maternal_events = new HashMap<>();
    //possibly dont need below
    private Map<String,ArrayList<String>> persons_maternal_side;
    //possibly dont need below
    private Map<String,ArrayList<String>> persons_paternal_side;




    private ArrayList<String> colorHues;

    public ArrayList<String> getColorHuesArray()
    {
        return colorHues;
    }

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
