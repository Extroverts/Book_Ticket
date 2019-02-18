package com.webtech.developers.bookmyticket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Booking_Details extends AppCompatActivity {

    TextView movie_name,date_set,seat_number,total_amount,tax_amount,total,th_name;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking__details );

        String Dates=getIntent().getStringExtra( "movie_name" );
        String seats=getIntent().getStringExtra( "seat_number" );
        String dates=getIntent().getStringExtra( "dates" );
        String theator_name=getIntent().getStringExtra( "movie_th_name" );

        movie_name=findViewById( R.id.movie_name );
        date_set=findViewById( R.id.date_s);
        seat_number=findViewById( R.id.seat_numbers );
        total_amount=findViewById( R.id.amount );
        tax_amount=findViewById( R.id.tax_amount );
        total=findViewById( R.id.total );
        th_name=findViewById( R.id.th_name );

        movie_name.setText( Dates );
        date_set.setText( dates );
        seat_number.setText("100 x"+seats);
        String calculate= String.valueOf( (100 * Integer.parseInt( seats )) );
        total_amount.setText( calculate );
        String tax= String.valueOf( (((Integer.parseInt( calculate )* 2) / 100) * 4) );
        tax_amount.setText( tax );

        String total_am= String.valueOf( Integer.parseInt( calculate ) + Integer.parseInt( tax ) );

        total.setText( total_am );

        th_name.setText( theator_name );


    }
}
