package com.webtech.developers.bookmyticket.api;


import com.webtech.developers.bookmyticket.Models.MovieResponse;
import com.webtech.developers.bookmyticket.Models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);
}