package com.webtech.developers.bookmyticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class Login_Signup_Screen extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    EditText username,password;
    Button login_btn,forgot_password;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    DialogInterface dialogInterface;

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
        progressBar=(ProgressBar)findViewById(R.id.progress);
        forgot_password=(Button)findViewById(R.id.forgot);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=username.getText().toString();
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

                            Intent mains=new Intent(Login_Signup_Screen.this,Popular_Movies.class);
                            startActivity(mains);
                        }

                    }
                });

            }
        });
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Popular_Movies.class));
        }

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


        handler.postDelayed(runnable, 1000);
    }
}

