package com.webtech.developers.bookmyticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Signup_Screen extends AppCompatActivity  {

    private static final int RC_SIGN_IN =8 ;
    private static final String TAG ="" ;
    RelativeLayout rellay1, rellay2;
    EditText username,password;
    Button login_btn,forgot_password,signup_btn;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    static SharedPreferences.Editor editor;
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__signup__screen);
        firebaseAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login_btn=(Button)findViewById(R.id.login);
        signup_btn=(Button)findViewById(R.id.signup);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        forgot_password=(Button)findViewById(R.id.forgot);



        if(firebaseAuth.getCurrentUser()==null && checkInternetConenction()==true){
            handler.postDelayed(runnable, 2000);
        }
        else if(firebaseAuth.getCurrentUser()!=null  && checkInternetConenction()==true ){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else if(checkInternetConenction()==false) {
            Toast.makeText(getApplicationContext(),"Connect to the internet",Toast.LENGTH_LONG).show();
        }else {
            handler.postDelayed(runnable, 2000);
        }

        // user login button click
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail=username.getText().toString();
                final String pass=password.getText().toString();

                if(TextUtils.isEmpty(mail) ){
                    Toast.makeText(getApplicationContext(),"Please enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( TextUtils.isEmpty(pass) ){
                    Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(Login_Signup_Screen.this, new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(!task.isSuccessful()){
                            if(pass.length()<6){
                                Toast.makeText(Login_Signup_Screen.this, "enter correct password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Login_Signup_Screen.this, "Authetication failed", Toast.LENGTH_LONG).show();
                            }
                            } else {
                                Intent mains=new Intent(Login_Signup_Screen.this,MainActivity.class);
                                editor = getSharedPreferences(mail, MODE_PRIVATE).edit();
                                editor.putString("mail", mail);
                                editor.apply();
                                startActivity(mains);
                            }
                    }
                });
            }
        });

        //user registration button
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //forgot password click
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert=new AlertDialog.Builder(Login_Signup_Screen.this);
                alert.setTitle("Forgot Password ?");
                LayoutInflater layoutInflater=Login_Signup_Screen.this.getLayoutInflater();
                final View dialog=layoutInflater.inflate(R.layout.forgot_password,null);
                alert.setView(dialog);
                final EditText forgot_pass=dialog.findViewById(R.id.textInputEditText);
                Button btn=dialog.findViewById(R.id.send);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (!forgot_pass.getText().toString().trim().equals("")) {
                            firebaseAuth.sendPasswordResetEmail(forgot_pass.getText().toString().trim())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Login_Signup_Screen.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(Login_Signup_Screen.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            forgot_pass.setError("Enter email");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                AlertDialog aa=alert.create();
                aa.show();
            }

        });

    }
    //register user method
    private void registerUser(){
        String email = username.getText().toString().trim();
        String passwords  = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(passwords)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            Intent movies=new Intent(Login_Signup_Screen.this,MainActivity.class);
                            startActivity(movies);
                        }else{
                            Toast.makeText(Login_Signup_Screen.this,"Registration failed",Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

    }

    //check internet connection
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

