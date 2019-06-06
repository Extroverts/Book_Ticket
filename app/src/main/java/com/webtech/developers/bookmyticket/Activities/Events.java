package com.webtech.developers.bookmyticket.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webtech.developers.bookmyticket.Models.EventModel;
import com.webtech.developers.bookmyticket.R;
import com.webtech.developers.bookmyticket.adapter.MyViewHolder;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView btn;
    private FirebaseRecyclerOptions<EventModel> options;
    private FirebaseRecyclerAdapter<EventModel,MyViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onStart ( ) {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop ( ) {
        super.onStop();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_events );

        recyclerView= findViewById( R.id.rv );
        btn=findViewById( R.id.back_press);
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        databaseReference=FirebaseDatabase.getInstance().getReference("Events");
        options=new FirebaseRecyclerOptions.Builder<EventModel>().setQuery( databaseReference,EventModel.class ).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter <EventModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder (@NonNull MyViewHolder holder, int position, @NonNull final EventModel model) {


                holder.name.setText( model.getName() );
                holder.date.setText( model.getDate() );

                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {

                        Intent eventdetails=new Intent( Events.this,EventDetails.class );
                        eventdetails.putExtra( "event_name",model.getName() );
                        eventdetails.putExtra( "event_date",model.getDate() );
                        eventdetails.putExtra( "event_desc",model.getDesc() );
                        eventdetails.putExtra( "event_venue" ,model.getVenue());
                        startActivity( eventdetails );
                    }
                } );

                holder.btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        Intent eventdetails=new Intent( Events.this,EventBookingDetails.class );
                        eventdetails.putExtra( "event_name",model.getName() );
                        eventdetails.putExtra( "event_date",model.getDate() );
                        startActivity( eventdetails );
                    }
                } );
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder( (LayoutInflater.from( Events.this ).inflate( R.layout.event_card_view,viewGroup,false )) );
            }
        };

        recyclerView.setAdapter( firebaseRecyclerAdapter );

    }
}
