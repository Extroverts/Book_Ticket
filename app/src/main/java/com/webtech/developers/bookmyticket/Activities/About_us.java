package com.webtech.developers.bookmyticket.Activities;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ImageView;
import android.widget.TextView;

import com.webtech.developers.bookmyticket.R;


public class About_us extends AppCompatActivity {

    ImageView back;
    TextView term;
    Context mContext;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about_us );
        back = findViewById( R.id.back );
        term = findViewById( R.id.terms );
        mContext = getApplicationContext();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity( new Intent( About_us.this, MainActivity.class ) );
            }
        } );


        term.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/studio/terms"));
                startActivity(browserIntent);
            }
        } );
    }

        }

