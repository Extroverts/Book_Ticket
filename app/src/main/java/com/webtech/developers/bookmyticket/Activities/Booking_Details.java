package com.webtech.developers.bookmyticket.Activities;


import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webtech.developers.bookmyticket.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Booking_Details extends AppCompatActivity  {


    TextView movie_name,date_set,seat_number,total_amt,tax_amount,total,th_name;
    String movie_names,seats,dates,theator_name,calculate,tax,total_am;
    int Dimension=1000;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button book;
    Bitmap bitmap ;
    Spinner spinner;
    Double amount;
    QRGEncoder movie_details;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking__details );

        spinner=findViewById( R.id.spinner );

        total=findViewById( R.id.total );
        total_amt=findViewById( R.id.total_amt );
        book=findViewById( R.id.book );

                movie_names = getIntent().getStringExtra( "Movie_name" );
                dates = getIntent().getStringExtra( "Movie_date" );
               theator_name = getIntent().getStringExtra( "Movie_theaator_name" );
                movie_name = findViewById( R.id.movie_name );
                    date_set = findViewById( R.id.date_s );

        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {

                if(position==0){
                    total.setText( "180" );
                    amount=180*1.18;

                    total_amt.setText( amount.toString() );
                }
                else if(position==1){
                    total.setText( "150" );
                    amount=150*1.18;

                    total_amt.setText( amount.toString() );
                } else if(position==2){
                    total.setText( "100" );
                    amount=100*1.18;
                    total_amt.setText( amount.toString() );

                }
            }

            @Override
            public void onNothingSelected (AdapterView <?> parent) {

            }
        } );



                movie_name.setText( movie_names );
                date_set.setText( dates );

                book.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {


                        //QR Code Generation
                        QRCode();
                        // PDF file Generation
                        GeneratePdf();
                        try
                            {
                                QRGSaver.save( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/", movie_names, bitmap, QRGContents.ImageType.IMAGE_JPEG );
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
        HashMap<String,String> qr=new HashMap<String, String>();
        qr.put("User Details",user.getDisplayName());
        qr.put("User Email",user.getEmail());
        qr.put("Movie Name :",movie_names);
        qr.put("Seats :",seats);
        qr.put("Movie Date :",dates);
        qr.put("Movie Theator Name",theator_name);

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
        File file= new File( Environment.getExternalStorageDirectory().getPath() + "/Book My Ticket/"+ movie_names+".pdf" );
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

        PdfPCell cell5 = new PdfPCell(new Phrase("Movie Name"));
        PdfPCell cell6 = new PdfPCell(new Phrase(movie_names));
        table.addCell(cell5);
        table.addCell(cell6);

        PdfPCell cell9 = new PdfPCell(new Phrase("Tax Amount"));
        PdfPCell cell10 =  new PdfPCell(new Phrase("18%"));
        table.addCell(cell9);
        table.addCell(cell10);

        PdfPCell cell7 = new PdfPCell(new Phrase("Total Ticket Cost"));
        PdfPCell cell8 = new PdfPCell(new Phrase( amount.toString() ));
        table.addCell(cell7);
        table.addCell(cell8);


        PdfPCell cell13 = new PdfPCell(new Phrase("Movie Date"));
        PdfPCell cell14 = new PdfPCell(new Phrase(dates));
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

}

