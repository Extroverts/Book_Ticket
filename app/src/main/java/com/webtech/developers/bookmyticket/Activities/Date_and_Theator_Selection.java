package com.webtech.developers.bookmyticket.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.webtech.developers.bookmyticket.R;
import com.webtech.developers.bookmyticket.adapter.Mymovie_theator_Adapter;
import com.webtech.developers.bookmyticket.Models.MyMovieData;

import java.util.ArrayList;
import java.util.List;

public class Date_and_Theator_Selection extends AppCompatActivity {


    public static String movie_names,dates;
    RecyclerView movie_theator_name;
    Mymovie_theator_Adapter mymovie_theator_adapter;
    Toolbar toolbar;
    TextView textView,movie_namess;
    private List<MyMovieData> myMovieDataList=new ArrayList <>(  );

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_date_and__theator__selection );
        textView=findViewById( R.id.date );
        movie_namess=findViewById( R.id.movie_namess );

        movie_theator_name=findViewById( R.id.movie_theator_names );
        movie_names = getIntent().getStringExtra( "movie_name" );
        dates = getIntent().getStringExtra( "dates" );
        movie_namess.setText( movie_names );
        textView.setText( dates );

        mymovie_theator_adapter=new Mymovie_theator_Adapter( Date_and_Theator_Selection.this,myMovieDataList );
        RecyclerView.LayoutManager manager=new LinearLayoutManager( getApplicationContext() );
        movie_theator_name.setLayoutManager( manager );
        movie_theator_name.setItemAnimator( new DefaultItemAnimator() );
        movie_theator_name.setAdapter( mymovie_theator_adapter );

        initBookData();
    }
    private void initBookData() {
        MyMovieData one=new MyMovieData("INOX Movies");
        myMovieDataList.add( one );
        MyMovieData two=new MyMovieData("PVR Premier Shows");
        myMovieDataList.add( two );
        MyMovieData three=new MyMovieData("Jai Ganesh Movie Theator");
        myMovieDataList.add(three );

        mymovie_theator_adapter.notifyDataSetChanged();
    }
}
