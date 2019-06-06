package com.webtech.developers.bookmyticket.Activities;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class EventBookingDetails extends AppCompatActivity {

    private static final String TAG = "Adding data";
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView eventname,eventdate,total_amt;
    String name,date,desc,venue;
    int positions;
    HashMap<String,String> qr=new HashMap<String, String>();
    int Dimension=1000;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button book;
    Bitmap bitmap ;
    Spinner spinner;
    Double amount;
    QRGEncoder movie_details;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_event_booking_details );


        eventdate=findViewById( R.id.eventdate );
        eventname=findViewById( R.id.eventname );
        total_amt=findViewById( R.id.total_amt );
        book=findViewById( R.id.book );
        name=getIntent().getStringExtra( "event_name" );
         date=getIntent().getStringExtra( "event_date" );
         desc=getIntent().getStringExtra( "event_desc" );
        venue=getIntent().getStringExtra( "event_venue" );

        spinner=findViewById( R.id.spinner );


        total_amt=findViewById( R.id.total_amt );
        book=findViewById( R.id.book );


        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {
                positions=position;
                switch (position)
                    {
                        case 0:
                            amount=180*1.0;
                            total_amt.setText( amount.toString() );
                            break;
                        case 1:
                            amount=180*2.0;
                            total_amt.setText( amount.toString() );
                            break;

                        case 2:
                            amount=180*3.0;
                            total_amt.setText( amount.toString() );
                            break;
                        case 3:
                            amount=180*3.0;
                            total_amt.setText( amount.toString() );
                            break;
                        case 4:
                            amount=180*4.0;
                            total_amt.setText( amount.toString() );
                            break;
                        case 5:
                            amount=180*5.0;
                            total_amt.setText( amount.toString() );
                            break;
                    }

            }

            @Override
            public void onNothingSelected (AdapterView <?> parent) {

            }
        } );


        eventname.setText( name );
        eventdate.setText( date );

        book.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //QR Code Generation
                QRCode();
                // PDF file Generation
                GeneratePdf();
                //insert into db for user booking
                insertdatatodatabase();
                try
                    {
                        QRGSaver.save( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/", name, bitmap, QRGContents.ImageType.IMAGE_JPEG );
                        Toast.makeText( getApplication(), "Booking Successful.", Toast.LENGTH_SHORT ).show();
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

        qr.put("User Details",user.getDisplayName());
        qr.put("User Email",user.getEmail());
        qr.put("name",name);
        qr.put( "Amount",amount.toString() );
        qr.put( "People", String.valueOf( positions ) );
        qr.put("date",date);
        qr.put("Movie Theator Name",venue);

        movie_details =new QRGEncoder(qr.toString(), null, QRGContents.Type.TEXT, Dimension);
        try {

            // Getting QR-Code as Bitmap
            bitmap=movie_details.encodeAsBitmap();
        } catch (WriterException e) {
            Log.v("QR Code Error ", e.toString());
        }
    }

    public void GeneratePdf(){
        Document document=new Document(  );

        try
            {
                File file= new File( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/" + name + ".pdf" );
                PdfWriter.getInstance( document,new FileOutputStream( file ) );
                document.open();

                document.add( new Paragraph( "Book My Ticket" ) );

                PdfPTable table = new PdfPTable(2);

                PdfPCell cell1 = new PdfPCell(new Phrase("User Details"));
                PdfPCell cell2 = new PdfPCell(new Phrase(user.getDisplayName()));
                table.addCell(cell1);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Phrase("User Email"));
                PdfPCell cell4 = new PdfPCell(new Phrase(user.getEmail()));
                table.addCell(cell3);
                table.addCell(cell4);

                PdfPCell cell5 = new PdfPCell(new Phrase("event Name"));
                PdfPCell cell6 = new PdfPCell(new Phrase(name));
                table.addCell(cell5);
                table.addCell(cell6);


                PdfPCell cell7 = new PdfPCell(new Phrase("People"));
                PdfPCell cell8 = new PdfPCell(new Phrase( String.valueOf( positions ) ));
                table.addCell(cell7);
                table.addCell(cell8);


                PdfPCell cell9 = new PdfPCell(new Phrase("Total Amount"));
                PdfPCell cell10 = new PdfPCell(new Phrase( amount.toString() ));
                table.addCell(cell9);
                table.addCell(cell10);


                PdfPCell cell13 = new PdfPCell(new Phrase("Movie Date"));
                PdfPCell cell14 = new PdfPCell(new Phrase(date));
                table.addCell(cell13);
                table.addCell(cell14);


                document.add(table);
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
        db.collection("usersHistory")
                .add(qr)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    }

