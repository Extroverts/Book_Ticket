package com.webtech.developers.bookmyticket;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash__screen );
       FirebaseAuth Auth=FirebaseAuth.getInstance();
       final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run ( ) {
                if(user==null){
                    startActivity( new Intent( Splash_Screen.this,Login_Signup_Screen.class ) );
                    finish();
                }
                else{
                    startActivity( new Intent( Splash_Screen.this,MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
