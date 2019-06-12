package com.webtech.developers.bookmyticket.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webtech.developers.bookmyticket.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Booking_Details extends AppCompatActivity {

    private static final String TAG = "Firebase";
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    HashMap <String, String> qr = new HashMap <String, String>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView movie_name, movie_date, th_name, seat, amount;
    String moviename, moviedate, thname, seats, movietime;
    QRGEncoder movie_details;
    int Dimension = 1000;

    int amounts = 1, seatamount;
    Bitmap bitmap;
    Button book;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking__details );


        movie_name = findViewById( R.id.movie_name );
        movie_date = findViewById( R.id.movie_date );
        th_name = findViewById( R.id.th_name );
        seat = findViewById( R.id.seats );
        amount = findViewById( R.id.amt );
        book = findViewById( R.id.book );

        moviename = getIntent().getStringExtra( "movie_name" );
        moviedate = getIntent().getStringExtra( "date" );
        thname = getIntent().getExtras().getString( "thname" );
        seats = getIntent().getStringExtra( "seats" );
        movietime = getIntent().getStringExtra( "movie_time" );

        String m = seats;

        final int abc = Integer.parseInt( m );

        seatamount = abc * 180;
        movie_name.setText( moviename );
        movie_date.setText( moviedate );
        th_name.setText( thname );
        seat.setText( seats );
        amount.setText( "" + String.valueOf( seatamount ) );


        book.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //QR Code Generation
                QRCode();
                // PDF file Generation
                GeneratePdf();
                //insert into db for user history
                insertdatatodatabase();
                try
                    {
                        QRGSaver.save( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/", moviename, bitmap, QRGContents.ImageType.IMAGE_JPEG );
                        Toast.makeText( getApplication(), "Booking Successful.", Toast.LENGTH_SHORT ).show();

                        AlertDialog.Builder builder=new AlertDialog.Builder( Booking_Details.this );
                        builder.setTitle( "Want to see the Ticket" );
                        builder.setPositiveButton( "Open", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                          Intent intent=new Intent( Intent.ACTION_GET_CONTENT);
                          Uri uri=Uri.parse(  Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/");
                          intent.setDataAndType( uri,"*/*" );
                          startActivity( Intent.createChooser( intent,"Open Folder" ));
                            }
                        } );

                        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                dialog.cancel();
                                startActivity( new Intent( Booking_Details.this,MainActivity.class ) );
                            }
                        } );
                        builder.create().show();

                    } catch ( WriterException e )
                    {
                        e.printStackTrace();
                    }
            }
        } );
    }


    //QR Code Generation COde
    public void QRCode ( ) {

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        qr.put( "User Details", user.getDisplayName() );
        qr.put( "User Email", user.getEmail() );
        qr.put( "name", moviename );
        qr.put( "Seats :", seats );
        qr.put( "date", moviedate );
        qr.put( "Movie Theator Name", thname );
        qr.put( "amount", String.valueOf( seatamount ) );

        movie_details = new QRGEncoder( qr.toString(), null, QRGContents.Type.TEXT, Dimension );
        try
            {

                // Getting QR-Code as Bitmap
                bitmap = movie_details.encodeAsBitmap();
            } catch ( WriterException e )
            {
                Log.v( "QR Code Error ", e.toString() );
            }
    }

    public void GeneratePdf ( ) {
        Document document = new Document();

        try
            {
                File file = new File( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/" + moviename + ".pdf" );
                PdfWriter.getInstance( document, new FileOutputStream( file ) );
                document.open();

                document.add( new Paragraph( "Book My Ticket" ) );

                PdfPTable table = new PdfPTable( 2 );

                PdfPCell cell1 = new PdfPCell( new Phrase( "User Details" ) );
                PdfPCell cell2 = new PdfPCell( new Phrase( user.getDisplayName() ) );
                table.addCell( cell1 );
                table.addCell( cell2 );

                PdfPCell cell3 = new PdfPCell( new Phrase( "User Email" ) );
                PdfPCell cell4 = new PdfPCell( new Phrase( user.getEmail() ) );
                table.addCell( cell3 );
                table.addCell( cell4 );

                PdfPCell cell5 = new PdfPCell( new Phrase( "Movie Name" ) );
                PdfPCell cell6 = new PdfPCell( new Phrase( moviename ) );
                table.addCell( cell5 );
                table.addCell( cell6 );

                PdfPCell cell7 = new PdfPCell( new Phrase( "Total Ticket Cost" ) );
                PdfPCell cell8 = new PdfPCell( new Phrase( String.valueOf( seatamount ) ) );
                table.addCell( cell7 );
                table.addCell( cell8 );

                PdfPCell cell9 = new PdfPCell( new Phrase( "Total Seat" ) );
                PdfPCell cell10 = new PdfPCell( new Phrase( String.valueOf( seats ) ) );
                table.addCell( cell9 );
                table.addCell( cell10 );

                PdfPCell cell13 = new PdfPCell( new Phrase( "Movie Date" ) );
                PdfPCell cell14 = new PdfPCell( new Phrase( moviedate ) );
                table.addCell( cell13 );
                table.addCell( cell14 );


                document.add( table );
                document.add( new Paragraph( "This is Computer Generated Invoice" ) );

            } catch ( DocumentException e )
            {
                e.printStackTrace();

            } catch ( FileNotFoundException e )
            {
                e.printStackTrace();
            }
        document.close();
    }

    private void insertdatatodatabase ( ) {
        db.collection( "usersHistory" ).document( user.getDisplayName() )
                .set( qr )
                .addOnSuccessListener( new OnSuccessListener <Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {
                        Log.d( TAG, "DocumentSnapshot successfully written!" );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        Log.w( TAG, "Error writing document", e );
                    }
                } );

    }

}