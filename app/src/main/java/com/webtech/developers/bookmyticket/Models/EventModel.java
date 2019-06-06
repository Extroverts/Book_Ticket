package com.webtech.developers.bookmyticket.Models;

public class EventModel {

    public String name;
    public String date;
    public String venue;
    public String desc;

    public EventModel(){

    }

    public EventModel(String name, String date, String venue, String desc){
        this.name=name;
        this.date=date;
        this.venue=venue;
        this.desc=desc;
    }

    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDate ( ) {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }


    public String getVenue ( ) {
        return venue;
    }

    public void setVenue (String venue) {
        this.venue = venue;
    }

    public String getDesc ( ) {
        return desc;
    }

    public void setDesc (String desc) {
        this.desc = desc;
    }


}
