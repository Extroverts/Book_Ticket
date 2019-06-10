package com.webtech.developers.bookmyticket.Activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.webtech.developers.bookmyticket.BuildConfig;
import com.webtech.developers.bookmyticket.Models.Movies;
import com.webtech.developers.bookmyticket.Models.Trailer;
import com.webtech.developers.bookmyticket.Models.TrailerResponse;
import com.webtech.developers.bookmyticket.R;
import com.webtech.developers.bookmyticket.adapter.TrailerAdapter;
import com.webtech.developers.bookmyticket.api.Client;
import com.webtech.developers.bookmyticket.api.Service;
import com.webtech.developers.bookmyticket.data.FavoriteDbHelper;
import com.webtech.developers.bookmyticket.data.FavoriteMovies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView movieName,plotSynopsis,userRating,releaseDate;
    ImageView poster,back_button;
    Button book;
    MaterialFavoriteButton favoriteButton;
    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private List<Trailer> trailerList;

    private FavoriteDbHelper favoriteDbHelper;
    private Movies favoriteMovies;
    private  final AppCompatActivity activity=DetailActivity.this;
    private SQLiteDatabase mDb;
    int movie_id;
    String Dates,moviedate,seatcount,thname,time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        FavoriteDbHelper dbHelper = new FavoriteDbHelper( this );
        mDb = dbHelper.getWritableDatabase();
        poster = findViewById( R.id.thumbnail_header );
        movieName = findViewById( R.id.title );
        plotSynopsis = findViewById( R.id.plot_synopsis );
        releaseDate = findViewById( R.id.release_date );
        userRating = findViewById( R.id.user_rating );
        back_button = findViewById( R.id.back_button );
        book = findViewById( R.id.book );


        Intent previousActivity = getIntent();
        if ( previousActivity.hasExtra( "original_title" ) )
            {
                String thumbnail = getIntent().getExtras().getString( "poster_path" );
                String movieTitle = getIntent().getExtras().getString( "original_title" );
                String synopsis = getIntent().getExtras().getString( "overview" );
                String release = getIntent().getExtras().getString( "release_date" );
                movie_id = getIntent().getExtras().getInt( "id" );
                String rating = getIntent().getExtras().getString( "user_rating" );
                Glide.with( this ).load( "http://image.tmdb.org/t/p/w500/" + thumbnail ).placeholder( R.drawable.load ).into( poster );
                movieName.setText( movieTitle );
                plotSynopsis.setText( synopsis );
                userRating.setText( rating );
                releaseDate.setText( release );
                ((CollapsingToolbarLayout) findViewById( R.id.collapsing_toolbar )).setTitle( movieTitle );
            } else
            {
                Toast.makeText( this, "No api data..", Toast.LENGTH_SHORT ).show();
            }

        back_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        } );
        favoriteButton = findViewById( R.id.favorite );
        final String movieTitle = getIntent().getExtras().getString( "original_title" );
        if ( Exists( movieTitle ) )
            {
                favoriteButton.setFavorite( true );
                favoriteButton.setOnFavoriteChangeListener(
                        new MaterialFavoriteButton.OnFavoriteChangeListener() {
                            @Override
                            public void onFavoriteChanged (MaterialFavoriteButton buttonView, boolean favorite) {
                                if ( favorite == true )
                                    {
                                        saveFavorite();
                                        Snackbar.make( buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT ).show();
                                    } else
                                    {
                                        favoriteDbHelper = new FavoriteDbHelper( DetailActivity.this );
                                        favoriteDbHelper.deleteFavorite( movie_id );
                                        Snackbar.make( buttonView, "Removed from Favorite",Snackbar.LENGTH_SHORT ).show();
                                    }
                            }
                        } );
            } else
            {
                favoriteButton.setOnFavoriteChangeListener(
                        new MaterialFavoriteButton.OnFavoriteChangeListener() {
                            @Override
                            public void onFavoriteChanged (MaterialFavoriteButton buttonView, boolean favorite) {
                                if ( favorite == true )
                                    {
                                        saveFavorite();
                                        Snackbar.make( buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT ).show();
                                    } else
                                    {
                                        int movie_id = getIntent().getExtras().getInt( "id" );
                                        favoriteDbHelper = new FavoriteDbHelper( DetailActivity.this );
                                        favoriteDbHelper.deleteFavorite( movie_id );
                                        Snackbar.make( buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT ).show();
                                    }
                            }
                        } );
            }
        initView();

        book.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder( DetailActivity.this );
                alert.setTitle( movieName.getText().toString() );
                alert.setMessage( R.string.select_seat );
                LayoutInflater layoutInflater = (activity).getLayoutInflater();
                final View dialogView = layoutInflater.inflate( R.layout.select_seat, null );
                alert.setView( dialogView );

                final Spinner spinner1,spinner2,spinner3,spinner4;
                spinner1=dialogView.findViewById( R.id.spinnerdate );
                spinner2=dialogView.findViewById( R.id.spinnerseat );
                spinner3=dialogView.findViewById( R.id.theator_name );
                spinner4=dialogView.findViewById( R.id.time );


                Calendar now=Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "dd/MM/yyyy" );
               final String[] days=new String[7];
                int delte=-now.get( GregorianCalendar.DAY_OF_WEEK )+2;
                now.add( Calendar.DAY_OF_MONTH,delte );
                for (int i=0;i<7;i++){
                    days[i]=simpleDateFormat.format( now.getTime() );
                    now.add( Calendar.DAY_OF_MONTH,1 );

                }

                ArrayAdapter<String> arrayAdapt=new ArrayAdapter<String>( DetailActivity.this,android.R.layout.simple_spinner_item,days );
                spinner1.setAdapter( arrayAdapt );


                spinner1.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {
                        moviedate=parent.getItemAtPosition( position ).toString();

                        }

                    @Override
                    public void onNothingSelected (AdapterView <?> parent) {

                    }
                } );

                spinner2.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {
                        seatcount=parent.getItemAtPosition( position ).toString();

                    }

                    @Override
                    public void onNothingSelected (AdapterView <?> parent) {

                    }
                } );

                spinner3.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {
                        thname=parent.getItemAtPosition( position ).toString();
                    }

                    @Override
                    public void onNothingSelected (AdapterView <?> parent) {

                    }
                } );

                spinner4.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected (AdapterView <?> parent, View view, int position, long id) {
                        time=parent.getItemAtPosition( position ).toString();
                    }

                    @Override
                    public void onNothingSelected (AdapterView <?> parent) {

                    }
                } );


                alert.setPositiveButton( "Book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        Intent i=new Intent( DetailActivity.this,Booking_Details.class );
                        i.putExtra( "movie_name",movieTitle );
                        i.putExtra( "date",moviedate );
                        i.putExtra( "seats",seatcount );
                        i.putExtra( "thname",thname );
                        i.putExtra( "movie_time",time );
                        startActivity( i );
                    }
                } );
                alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                } );
                alert.create().show();
            }
        } );


    }
    public boolean Exists(String searchItem) {

        String[] projection = {

                FavoriteMovies.FavoriteEntry._ID,
                FavoriteMovies.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteMovies.FavoriteEntry.COLUMN_TITLE,
                FavoriteMovies.FavoriteEntry.COLUMN_USERRATING,
                FavoriteMovies.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteMovies.FavoriteEntry.COLUMN_PLOT_SYNOPSIS

        };
        String selection = FavoriteMovies.FavoriteEntry.COLUMN_TITLE + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = mDb.query(FavoriteMovies.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void saveFavorite(){
        favoriteDbHelper=new FavoriteDbHelper(activity);
        favoriteMovies=new Movies();
        int movieId=getIntent().getExtras().getInt("id");
        Double rate= Double.valueOf(getIntent().getExtras().getString("user_rating"));
        Log.d("Rating",String.valueOf(rate));
        String poster=getIntent().getExtras().getString("poster_path");
        String title=getIntent().getExtras().getString("original_title");
        Log.d("Moviee", title);
        favoriteMovies.setId(movieId);
        favoriteMovies.setTitle(title);
        favoriteMovies.setPosterPath(poster);
        favoriteMovies.setOverview(plotSynopsis.getText().toString());
        favoriteMovies.setVoteAverage(rate);
        favoriteDbHelper.addFavorite(favoriteMovies);
    }

    //get data from initViews
    private void initView(){
        trailerList=new ArrayList<>();
        adapter=new TrailerAdapter(this,trailerList);
        recyclerView=findViewById(R.id.recycle_view_trailer);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadTrailer();
    }

    //load Trailers
    private void loadTrailer(){
        int movieId=getIntent().getExtras().getInt("id");
        Log.d("TAG", "loadTrailer: "+movieId);
        try{
            if(BuildConfig.movie_db_api_key.isEmpty()){
                Toast.makeText(this,"Please obtain api key",Toast.LENGTH_SHORT).show();
            }
            Client client=new Client();
            Service apiService=Client.getClient().create(Service.class);
            retrofit2.Call<TrailerResponse> call=apiService.getMovieTrailer(movieId,BuildConfig.movie_db_api_key);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(retrofit2.Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailers=response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailers));
                    recyclerView.smoothScrollToPosition(0);
                    Log.e("POSERR",trailers.toString());
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Toast.makeText(DetailActivity.this,"Error Loading Trailer",Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(DetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

   
}

