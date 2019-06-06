package com.webtech.developers.bookmyticket.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.webtech.developers.bookmyticket.R;

public class Login_Signup_Screen extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 7;
    GoogleSignInClient googleSignInClient;
    ImageView bookMyTicket_logo;
    TextView skipLogin;
    private FirebaseAuth mAuth;
    Animation bmtLogoAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login__signup__screen );
        bookMyTicket_logo=findViewById( R.id.bmt_logo);
        skipLogin=findViewById( R.id.skip_click );
        bmtLogoAnimation=AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade_in );
        bookMyTicket_logo.startAnimation( bmtLogoAnimation );

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        skipLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity( new Intent( Login_Signup_Screen.this,MainActivity.class ) );
                finish();
            }
        } );

    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText( getApplicationContext(),"Google Sign In failed",Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            Toast.makeText( getApplicationContext(),"Failed",Toast.LENGTH_SHORT ).show();
                            updateUI(null);
                        }
                    }
                });
    }

    void updateUI(FirebaseUser user){
    startActivity( new Intent( Login_Signup_Screen.this,MainActivity.class ) );
    finish();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}

