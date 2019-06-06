package com.webtech.developers.bookmyticket.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.webtech.developers.bookmyticket.R;

public class EventDetails extends AppCompatActivity {

    TextView Movie_name,Movie_date,Movie_venue,Movie_desc;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_event_details );
        Movie_name=findViewById( R.id.movie_name );
        Movie_date=findViewById( R.id.date );
        Movie_desc=findViewById( R.id.desc );
        Movie_venue=findViewById( R.id.venue );


        String name=getIntent().getStringExtra( "event_name" );
        String date=getIntent().getStringExtra( "event_date" );
        String desc=getIntent().getStringExtra( "event_desc" );
        String venue=getIntent().getStringExtra( "event_venue" );

        Movie_name.setText( name );
        Movie_date.setText( date );
        Movie_desc.setText( desc );
        Movie_venue.setText( venue);


    }
}
