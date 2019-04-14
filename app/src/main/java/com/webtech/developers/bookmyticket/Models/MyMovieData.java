package com.webtech.developers.bookmyticket.Models;

public class MyMovieData {
    private String Movie_name;

    public MyMovieData (String movie_name){
        this.Movie_name=movie_name;
    }
    public String getMovie_name ( ) {
        return Movie_name;
    }

    public void setMovie_name (String movie_name) {
        Movie_name = movie_name;
    }
}
