package com.example.rasmus.busster;

/**
 * Created by Rasmus on 2015-09-12.
 */
public class Item {




    public enum CATEGORY{
        IMPORTANT,
        VERYIMPORTANT,
        COOL,
        LATER;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    public void setText(String text) {
        this.text = text;
    }

    private CATEGORY category;
    private String text;

    public Item(String text, CATEGORY category){
        this.text = text;
        this.category = category;

    }


}
