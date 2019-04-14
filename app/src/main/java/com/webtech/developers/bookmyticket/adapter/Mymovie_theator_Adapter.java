package com.webtech.developers.bookmyticket.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.webtech.developers.bookmyticket.Activities.Booking_Details;
import com.webtech.developers.bookmyticket.Activities.Date_and_Theator_Selection;
import com.webtech.developers.bookmyticket.R;
import com.webtech.developers.bookmyticket.Models.MyMovieData;

import java.util.List;

public class Mymovie_theator_Adapter extends RecyclerView.Adapter<Mymovie_theator_Adapter.MovieAdapter>  {


    Context mcontext;
    private List<MyMovieData> bookList;

    public Mymovie_theator_Adapter (Context context,List<MyMovieData> bookList) {
        this.mcontext=context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public MovieAdapter onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View itemView=LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.date_theator_selection_card,viewGroup,false );
        return new MovieAdapter( itemView );
    }

    @Override
    public void onBindViewHolder (@NonNull MovieAdapter movieAdapter, int i) {
            movieAdapter.movie_name.setText( bookList.get( i ).getMovie_name() );
    }

    @Override
    public int getItemCount ( ) {
        return bookList.size();
    }

    public class MovieAdapter extends RecyclerView.ViewHolder {

        TextView movie_name;
        RadioGroup radioGroup;
        String mn,dates;

        public MovieAdapter (@NonNull View itemView) {
            super( itemView );
            movie_name=itemView.findViewById( R.id.movie_theator_names );
            radioGroup=itemView.findViewById( R.id.radio_group );

            mn=Date_and_Theator_Selection.movie_names;
            dates=Date_and_Theator_Selection.dates;

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    final int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                      final MyMovieData myMovieData=bookList.get( pos );
                        Intent i=new Intent( mcontext,Booking_Details.class );
                        i.putExtra( "Movie_name",mn );
                        i.putExtra( "Movie_date",dates );
                        i.putExtra( "Movie_theaator_name",myMovieData.getMovie_name());
                        mcontext.startActivity( i );


                    }
                }
            } );
        }
    }
}


