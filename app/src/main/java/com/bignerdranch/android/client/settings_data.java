package com.bignerdranch.android.client;

public class settings_data {

    private static settings_data instance;

    static settings_data getInstance()
    {
        if(instance == null)
        {
            instance = new settings_data();

        }

        return instance;

    }

    private settings_data()
    {

    }

    public void setLife_story_line(Boolean life_story_line) {
        this.life_story_line = life_story_line;
    }

    public void setFamily_tree_line(Boolean family_tree_line) {
        this.family_tree_line = family_tree_line;
    }

    public void setSpouse_line(Boolean spouse_line) {
        this.spouse_line = spouse_line;
    }

    public void setFather_side_line(Boolean father_side_line) {
        this.father_side_line = father_side_line;
    }

    public void setMother_side(Boolean mother_side) {
        this.mother_side = mother_side;
    }

    public void setMale_event_side(Boolean male_event_side) {
        this.male_event_side = male_event_side;
    }

    public void setFemale_event_side(Boolean female_event_side) {
        this.female_event_side = female_event_side;
    }

    public Boolean getLife_story_line() {
        return life_story_line;
    }

    public Boolean getFamily_tree_line() {
        return family_tree_line;
    }

    public Boolean getSpouse_line() {
        return spouse_line;
    }

    public Boolean getFather_side_line() {
        return father_side_line;
    }

    public Boolean getMother_side() {
        return mother_side;
    }

    public Boolean getMale_event_side() {
        return male_event_side;
    }

    public Boolean getFemale_event_side() {
        return female_event_side;
    }

    //by default all settings are turned on when first
    private Boolean life_story_line = false;

    private Boolean family_tree_line = false;

    private Boolean spouse_line = false;

    private Boolean father_side_line = false;

    private Boolean mother_side = false;

    private Boolean male_event_side = false;

    private Boolean female_event_side = false;




}
