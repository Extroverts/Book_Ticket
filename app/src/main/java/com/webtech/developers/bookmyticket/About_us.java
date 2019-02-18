package com.webtech.developers.bookmyticket;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ImageView;
import android.widget.TextView;



public class About_us extends AppCompatActivity {

    ImageView back;
    TextView term;

    WebView webView;
    Context mContext;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about_us );
        back = findViewById( R.id.back );
        term = findViewById( R.id.terms );
        mContext = getApplicationContext();
        webView = findViewById( R.id.web );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity( new Intent( About_us.this, MainActivity.class ) );
            }
        } );




        term.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                webView.setVisibility( View.VISIBLE );
                startWebView( "https://www.google.com" );
            }
        } );
    }

    //Rules and regulations
    private void startWebView (String url) {
        webView.setWebViewClient( new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                view.loadUrl( url );
                return true;
            }

            public void onLoadResource (WebView view, String url) {
                if ( progressDialog == null )
                    {
                        progressDialog = new ProgressDialog( About_us.this );
                        progressDialog.setMessage( "Loading..." );
                        progressDialog.show();
                    }
            }

            public void onPageFinished (WebView view, String url) {
                try
                    {
                        if ( progressDialog.isShowing() )
                            {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                    } catch ( Exception exception )
                    {
                        exception.printStackTrace();
                    }
            }

        } );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.loadUrl( url );


    }



}
