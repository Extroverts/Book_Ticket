package com.webtech.developers.bookmyticket;


import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Booking_Details extends AppCompatActivity  {


    TextView movie_name,date_set,seat_number,total_amount,tax_amount,total,th_name;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button book;
    Bitmap bitmap ;
    String movie_names,seats,dates,theator_name,calculate,tax,total_am;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_booking__details );

                movie_names = getIntent().getStringExtra( "movie_name" );
                seats = getIntent().getStringExtra( "seat_number" );
                dates = getIntent().getStringExtra( "dates" );
                theator_name = getIntent().getStringExtra( "movie_th_name" );

                movie_name = findViewById( R.id.movie_name );
                date_set = findViewById( R.id.date_s );
                seat_number = findViewById( R.id.seat_numbers );
                total_amount = findViewById( R.id.amount );
                tax_amount = findViewById( R.id.tax_amount );
                total = findViewById( R.id.total );
                th_name = findViewById( R.id.th_name );
                book = findViewById( R.id.book );

                //calculate the amount into 100
                calculate = String.valueOf( (100 * Integer.parseInt( seats )) );

                //calculate the tax
                tax = String.valueOf( (((Integer.parseInt( calculate ) * 2) / 100) * 4) );

                //calculate the total amount
                total_am = String.valueOf( Integer.parseInt( calculate ) + Integer.parseInt( tax ) );

                //set all  text into textView
                movie_name.setText( movie_names );
                date_set.setText( dates );
                seat_number.setText( "100 x" + seats );
                total.setText( total_am );
                th_name.setText( theator_name );
                total_amount.setText( calculate );
                tax_amount.setText( tax );

                book.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        //Pdf Generration and QR Code Generation
                        QRCode();
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
        qr.put("Movie Theator Namr",theator_name);


        int Dimension=1000;
        QRGEncoder movie_details =new QRGEncoder(qr.toString(), null, QRGContents.Type.TEXT, Dimension);
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

        PdfPTable table = new PdfPTable(2);

        // Header
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

        PdfPCell cell7 = new PdfPCell(new Phrase("Ticket Cost"));
        PdfPCell cell8 = new PdfPCell(new Phrase(calculate));
        table.addCell(cell7);
        table.addCell(cell8);

        PdfPCell cell9 = new PdfPCell(new Phrase("Tax Amount"));
        PdfPCell cell10 =  new PdfPCell(new Phrase(tax));
        table.addCell(cell9);
        table.addCell(cell10);

        PdfPCell cell11 = new PdfPCell(new Phrase("Total Ticket Amount"));
        PdfPCell cell12 = new PdfPCell(new Phrase(total_am));
        table.addCell(cell11);
        table.addCell(cell12);

        PdfPCell cell13 = new PdfPCell(new Phrase("Movie Date"));
        PdfPCell cell14 = new PdfPCell(new Phrase(dates));
        table.addCell(cell13);
        table.addCell(cell14);


        document.add(table);

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

