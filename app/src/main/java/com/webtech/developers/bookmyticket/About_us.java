package com.webtech.developers.bookmyticket;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.zxing.WriterException;
import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class About_us extends AppCompatActivity {

    ImageView back;
    TextView term;
    Button btnCreate;
    WebView webView;
    Context mContext;
    Bitmap bitmap ;
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

        btnCreate = findViewById( R.id.create );

        btnCreate.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick (View v) {
                Log.d( "Button CLick", "Button Clicked" );
                QRCode();
                try
                    {
                        QRGSaver.save(Environment.getExternalStorageDirectory().getPath()+"/Book/", "12", bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    } catch ( WriterException e )
                    {
                        e.printStackTrace();
                    }

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


    //QR Code Generation COde
    public void QRCode(){
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        int smallerDimension=1000;
        QRGEncoder qrgEncoder = new QRGEncoder("OJhjhkja", null, QRGContents.Type.TEXT, smallerDimension);
        QRGEncoder qrgEncoders = new QRGEncoder("OJhjhkja", null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            bitmap = qrgEncoders.encodeAsBitmap();
            //qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("asd", e.toString());
        }
    }

}
