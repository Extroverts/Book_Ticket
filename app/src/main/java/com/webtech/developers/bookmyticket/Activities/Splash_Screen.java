package com.webtech.developers.bookmyticket.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.webtech.developers.bookmyticket.R;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
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
        else{
            Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }
}
