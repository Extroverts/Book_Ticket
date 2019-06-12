package com.webtech.developers.bookmyticket.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webtech.developers.bookmyticket.Models.EventModel;
import com.webtech.developers.bookmyticket.Models.HistoryModel;
import com.webtech.developers.bookmyticket.R;
import com.webtech.developers.bookmyticket.adapter.MyViewHolder;

import java.util.ArrayList;

public class BookingHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back;
    private FirebaseRecyclerOptions<HistoryModel> options;
    private FirebaseRecyclerAdapter<HistoryModel,MyViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking_history );
        back=findViewById( R.id.back );
        recyclerView=findViewById( R.id.recyclerview );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        databaseReference=FirebaseDatabase.getInstance().getReference("usersHistory");
        options=new FirebaseRecyclerOptions.Builder<HistoryModel>().setQuery( databaseReference,HistoryModel.class ).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter <HistoryModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder (@NonNull MyViewHolder holder, int position, @NonNull HistoryModel model) {
                holder.name.setText( model.getName() );
                holder.date.setText( model.getDate() );
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder( (LayoutInflater.from( BookingHistory.this ).inflate( R.layout.event_card_view,viewGroup,false )) );
            }
        };
        recyclerView.setAdapter( firebaseRecyclerAdapter );


        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity( new Intent( BookingHistory.this,MainActivity.class ) );
            }
        } );
    }
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
}
