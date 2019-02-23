package com.webtech.developers.bookmyticket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.webtech.developers.bookmyticket.Models.MovieResponse;
import com.webtech.developers.bookmyticket.Models.Movies;
import com.webtech.developers.bookmyticket.adapter.MovieAdapter;
import com.webtech.developers.bookmyticket.api.Client;
import com.webtech.developers.bookmyticket.data.FavoriteDbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movies> moviesList;
    private AppCompatActivity activity=MainActivity.this;
    ProgressDialog progressDialog;
    private static long back_pressed;
    private TextView username,email;
    private FirebaseAuth auth;

    private FirebaseAuth.AuthStateListener authListener;
    ImageView imgView;

    private FavoriteDbHelper favoriteDbHelper;
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestCameraPermission();
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Login_Signup_Screen.class));
                    finish();
                }
            }
        };
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getString(R.string.fetching_movies));
        progressDialog.setMessage(getString(R.string.loading_movies));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        loadMovies();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem( 0 ).setChecked( true );
        username=header.findViewById(R.id.user_name);
        email=header.findViewById(R.id.user_email);
        imgView=header.findViewById( R.id.imageView );

        //get data from google Signin Profile
        username.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Picasso.with( this ).load( user.getPhotoUrl() ).into(imgView);

    }
    public void initViews() {
        progressDialog.show();
        recyclerView = findViewById(R.id.recycle_view);
        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(this, moviesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(activity);
    }

    private void initViews2(){
        progressDialog.show(); // Display Progress Dialog
        recyclerView=findViewById(R.id.recycle_view);
        moviesList=new ArrayList<>();
        adapter=new MovieAdapter(this,moviesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper=new FavoriteDbHelper(activity);
        getAllFavorite();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if ( drawer.isDrawerOpen(GravityCompat.START) )
            {
                drawer.closeDrawer(GravityCompat.START);
            } else if(back_pressed+2000>System.currentTimeMillis())
            {
                super.onBackPressed();
            } else {
            Toast.makeText( getApplicationContext(),"Press Once Again to Exit !",Toast.LENGTH_SHORT ).show();
            }
        back_pressed=System.currentTimeMillis();
        finish();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.


            } else {
                // Permission request was denied.
               Toast.makeText( getApplicationContext(),"Permission not granted",Toast.LENGTH_SHORT ).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         if(id==R.id.logout)
         {
           signOut();

         }else if ( id==R.id.nav_share){
            Intent share=new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT,R.string.extra_sending_text);
            startActivity(Intent.createChooser(share,getString(R.string.share_intent)));
         } else if(id== R.id.fav_movies){
             initViews2();
         } else if(id==R.id.now_playing){
             loadMovies();
         } else if(id==R.id.upcoming_movies){

         } else if(id==R.id.about_us){
             startActivity(new Intent(MainActivity.this,About_us.class));
         } else if(id==R.id.privacy){
             Toast.makeText( getApplicationContext(),"Updated Soon" ,Toast.LENGTH_SHORT).show();
         }else if(id==R.id.feedback){

         }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                moviesList.clear();
                moviesList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }.execute();
    }

    private void loadMovies(){
        initViews();
        try{
            if(BuildConfig.movie_db_api_key.isEmpty()){
                Toast.makeText(this,"Get an api key First",Toast.LENGTH_LONG).show();
                return;
            }
            Client client=new Client();
            com.webtech.developers.bookmyticket.api.Service apiService=Client.getClient().create(com.webtech.developers.bookmyticket.api.Service.class);
            retrofit2.Call<MovieResponse> call=apiService.getPopularMovies(BuildConfig.movie_db_api_key);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movies> movies=response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                    Log.d("Error","Error fetching");

                    Toast.makeText(MainActivity.this,"Error Fetching Movies check Internet Connection",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void signOut() {
        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CAMERA);


        } else {

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CAMERA);
        }
    }
}
