package com.webtech.developers.bookmyticket.PdfGeneration;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfGeneration {

    public PdfGeneration docs(){
        Document document=new Document(  );

        try
            {
                File file= new File( Environment.getExternalStorageDirectory().getPath() + "/01.pdf" );
                PdfWriter.getInstance( document,new FileOutputStream( file ) );
                document.open();
                Paragraph p=new Paragraph( "Hello Wod" );
                document.add( p );

            } catch ( DocumentException e )
            {
                e.printStackTrace();
            } catch ( FileNotFoundException e )
            {
                e.printStackTrace();
            }

        document.close();
        return null;
    }
    }
