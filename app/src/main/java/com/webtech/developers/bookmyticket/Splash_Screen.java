package com.webtech.developers.bookmyticket;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash_Screen extends AppCompatActivity {


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);

        if(checkInternetConenction()==true){
            Intent login=new Intent(Splash_Screen.this,Login_Signup_Screen.class);
            startActivity(login);
        }
        else {
            Toast.makeText(getApplicationContext(),"Please Connect to Internet",Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkInternetConenction() {

        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() ==
             android.net.NetworkInfo.State.CONNECTED ||
             connec.getNetworkInfo(0).getState() ==
             android.net.NetworkInfo.State.CONNECTING ||
             connec.getNetworkInfo(1).getState() ==
             android.net.NetworkInfo.State.CONNECTING ||
             connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED )
            {

                return true;
            } else if (
                connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() ==
                android.net.NetworkInfo.State.DISCONNECTED )
            {

                return false;
            }
        return false;
    }
}
