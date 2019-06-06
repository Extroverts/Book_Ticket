package com.webtech.developers.bookmyticket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webtech.developers.bookmyticket.R;

public class MyViewHolder extends RecyclerView.ViewHolder  {

    public TextView name,date;
    public Button btn;

    public MyViewHolder(View itemView) {
        super(itemView);

        name=itemView.findViewById( R.id.eventname );
        date=itemView.findViewById( R.id.eventdate );
        btn=itemView.findViewById( R.id.bookeventbutton );
    }
}