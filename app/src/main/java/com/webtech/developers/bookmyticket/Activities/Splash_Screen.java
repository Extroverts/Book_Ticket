package com.webtech.developers.bookmyticket.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.webtech.developers.bookmyticket.R;

public class Splash_Screen extends AppCompatActivity {

    ImageView bookMyTicket_logo;
    TextView bookMyTicket_name;
    Animation logoAnimation;
    ConstraintLayout constraintLayout;
    ConnectivityManager cm;
    FirebaseUser user;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        bookMyTicket_logo=findViewById( R.id.bmt_logo );
        bookMyTicket_name=findViewById( R.id.bmt_name );
        constraintLayout=findViewById( R.id.constraint_layout );

        logoAnimation=AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade_in );
        bookMyTicket_logo.startAnimation( logoAnimation );

        user= FirebaseAuth.getInstance().getCurrentUser();
        cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm.getActiveNetworkInfo()!=null){
            LoginInfo();
        }
        else{
            ErrorMessage();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
    private void LoginInfo(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user == null) {
                        startActivity(new Intent(Splash_Screen.this, Login_Signup_Screen.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                        finish();
                    }
                }
            }, 2000);
    }

    private void ErrorMessage(){
        final AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setTitle("You are offline");

        builder.setPositiveButton( "Retry", new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick (DialogInterface dialog, int which) {
                if (cm.getActiveNetworkInfo() != null) {
                    startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                    finish();
                }
                else{
                    builder.create().show();
                }
            }
        } );
        builder.setNegativeButton( "Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                finish();
            }
        } );
            builder.create().show();
    }
}
