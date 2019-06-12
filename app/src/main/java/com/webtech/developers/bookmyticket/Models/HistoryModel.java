package com.webtech.developers.bookmyticket.Models;


public class HistoryModel{
    public String name;
    public String date;
    public String venue;
    public String desc;

    public HistoryModel(){

    }

    public HistoryModel(String name, String date, String venue, String desc){
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

}