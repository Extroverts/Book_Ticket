package com.webtech.developers.bookmyticket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Booking_Details extends AppCompatActivity {

    TextView movie_name,date_set,seat_number,total_amount,tax_amount,total,th_name;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button book;
    Bitmap bitmap ;
    String movie_names,seats,dates,theator_name,calculate,tax,total_am;
    ImageView qrImage;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking__details );

        movie_names=getIntent().getStringExtra( "movie_name" );
        seats=getIntent().getStringExtra( "seat_number" );
        dates=getIntent().getStringExtra( "dates" );
        theator_name=getIntent().getStringExtra( "movie_th_name" );

        movie_name=findViewById( R.id.movie_name );
        date_set=findViewById( R.id.date_s);
        seat_number=findViewById( R.id.seat_numbers );
        total_amount=findViewById( R.id.amount );
        tax_amount=findViewById( R.id.tax_amount );
        total=findViewById( R.id.total );
        th_name=findViewById( R.id.th_name );
        book=findViewById( R.id.book );

        //calculate the amount into 100
        calculate= String.valueOf( (100 * Integer.parseInt( seats )) );

        //calculate the tax
        tax= String.valueOf( (((Integer.parseInt( calculate )* 2) / 100) * 4) );

        //calculate the total amount
        total_am= String.valueOf( Integer.parseInt( calculate ) + Integer.parseInt( tax ) );

        //set all  text into textView
        movie_name.setText( movie_names );
        date_set.setText( dates );
        seat_number.setText("100 x"+seats);
        total.setText( total_am );
        th_name.setText( theator_name );
        total_amount.setText( calculate );
        tax_amount.setText( tax );

        book.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //Pdf Generration and QR Code Generation
                QRCode();
                try{
                    QRGSaver.save( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/", "12", bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    Toast.makeText( getApplication(),"QR Code Successfully Generated",Toast.LENGTH_SHORT ).show();
                } catch ( WriterException e )
                    {
                        e.printStackTrace();
                    }
            }
        } );
    }


    //QR Code Generation COde
    public void QRCode(){
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        int Dimension=1000;
        QRGEncoder seat =new QRGEncoder(movie_names, null, QRGContents.Type.TEXT, Dimension);
        //QRGEncoder seat =new QRGEncoder(user.getDisplayName()+"\n"+seats+"\n"+movie_names, null, QRGContents.Type.TEXT, Dimension);
        Log.d("Movie_name",movie_names);
        try {

            // Getting QR-Code as Bitmap
            bitmap = seat.encodeAsBitmap();
            //qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("asd", e.toString());
        }
    }



}
