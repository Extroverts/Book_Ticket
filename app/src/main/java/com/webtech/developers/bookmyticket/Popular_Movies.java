package com.webtech.developers.bookmyticket;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.webtech.developers.bookmyticket.Models.MovieResponse;
import com.webtech.developers.bookmyticket.Models.Movies;
import com.webtech.developers.bookmyticket.adapter.MovieAdapter;
import com.webtech.developers.bookmyticket.api.Client;
import com.webtech.developers.bookmyticket.api.Service;
import com.webtech.developers.bookmyticket.data.FavoriteDbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class Popular_Movies extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{


    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movies> moviesList;
    private AppCompatActivity activity=Popular_Movies.this;
    ProgressDialog progressDialog;
    private FavoriteDbHelper favoriteDbHelper;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular__movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressDialog = new ProgressDialog(Popular_Movies.this);
        progressDialog.setTitle("Fetching Movies"); // Setting Message
        progressDialog.setMessage("Loading ....."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        initViews();

    }


    public void initViews() {
            progressDialog.show();
            recyclerView = findViewById(R.id.recycle_view);
            moviesList = new ArrayList <>();
            adapter = new MovieAdapter(this, moviesList);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            favoriteDbHelper = new FavoriteDbHelper(activity);
            swipeContainer = findViewById(R.id.main_content);
            swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initViews();
                    Toast.makeText(Popular_Movies.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
                }
            });
            checkSortOrder();
        }
        private void initViews2(){

            progressDialog.show(); // Display Progress Dialog
            recyclerView=findViewById(R.id.recycle_view);
            moviesList=new ArrayList<>();
            adapter=new MovieAdapter(this,moviesList);
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            favoriteDbHelper=new FavoriteDbHelper(activity);
            getAllFavorite();
        }




    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite(){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                moviesList.clear();
                moviesList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

        }.execute();
    }
    private void loadMovies(){
        try{
            if(BuildConfig.movie_db_api_key.isEmpty()){
                Toast.makeText(this,"Get an api key First",Toast.LENGTH_LONG).show();
                return;
            }
            Client client=new Client();
            com.webtech.developers.bookmyticket.api.Service apiService=Client.getClient().create(com.webtech.developers.bookmyticket.api.Service.class);
            retrofit2.Call<MovieResponse> call=apiService.getPopularMovies(BuildConfig.movie_db_api_key);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movies> movies=response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }progressDialog.dismiss();

                }

                @Override
                public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                    Log.d("Error","Error fetching");
                    Toast.makeText(Popular_Movies.this,"Error Fetching data",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
            });



        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

    }
    private void loadMovies1(){
        try{
            if(BuildConfig.movie_db_api_key.isEmpty()){
                Toast.makeText(this,"Get an api key First",Toast.LENGTH_LONG).show();
                return;
            }
            Client client=new Client();
            com.webtech.developers.bookmyticket.api.Service apiService=Client.getClient().create(com.webtech.developers.bookmyticket.api.Service.class);
            retrofit2.Call<MovieResponse> call=apiService.getTopRatedMovies(BuildConfig.movie_db_api_key);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movies> movies=response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                    Log.d("Error","Error fetching");
                    Toast.makeText(Popular_Movies.this,"Error Fetching data",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
            });



        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                Intent intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        checkSortOrder();

    }
    private void checkSortOrder(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder=preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_sort_popular)
        );
        if(sortOrder.equals(this.getString(R.string.pref_sort_popular))){
            loadMovies();
        }
        else if(sortOrder.equals(this.getString(R.string.favorite))){
            initViews2();
        }
        else {
                loadMovies1();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moviesList.isEmpty()){
            checkSortOrder();
        }else{
            checkSortOrder();


        }
    }







}
