package com.example.napstar.movieapp2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.napstar.movieapp2.com.napstar.movieapp.com.napstar.movieapp.async.MoviesAsyncTask;
import com.example.napstar.movieapp2.com.napstar.movieapp.com.napstar.movieapp.async.TestMovieAsync;
import com.example.napstar.movieapp2.com.napstar.movieapp.model.MovieModel;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity   implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ArrayList<MovieModel> moviesList;
    Context context;
    private Typeface tfMedium,tfLight;

    private static final String DEBUG_TAG = "MainActivity";

    private ProgressDialog loading = null;
    private SwipeRefreshLayout swipeContainer = null;
    MoviesArrayAdapter adapter =null;
    // variable to toggle city values on refresh
   // boolean refreshToggle = true;
    Handler handler=null;
    ListView listViewMovies=null;

    boolean refreshToggle = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            listViewMovies=(ListView)findViewById(R.id.movies_list);
            boolean refrshToggle=true;

        moviesList= new ArrayList<MovieModel>();
        context=getApplicationContext();
        this.setFinishOnTouchOutside(true);
        // Check if the NetworkConnection is active and connected.
        try
        {
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe);
            // Setup refresh listener which triggers new data loading

            swipeContainer.setSize(SwipeRefreshLayout.LARGE);
            swipeContainer.setProgressViewOffset(false, 0,400);
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.background_dark,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null &&
                    networkInfo.isConnectedOrConnecting();
            if (isConnected) {

          //  new MoviesAsyncTask(context,MainActivity.this).execute();
                moviesList= new TestMovieAsync().execute().get();
                ListView listView = (ListView)  findViewById(R.id.movies_list);
                Log.d("DEBUG_TAG", moviesList.toString());
                // Add results to listView.
                Context ctx=getContext();
                  adapter = new MoviesArrayAdapter (getContext(), moviesList,MainActivity.this);
                listView.setAdapter(adapter);




            } else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Cannot connect to Internet");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

                Log.d(DEBUG_TAG,"No Network Connection");

            }


            //set on click listener

            listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,long id)
                {

                    // selected item
                    TextView tvID=(TextView) view.findViewById(R.id.movie_ID);
                    String selectedMovieID = (tvID).getText().toString();
                    Intent intent = new Intent(context, MovieDetailsActivity.class);

                    intent.putExtra("movieID", selectedMovieID);
                    startActivity(intent);
                }
            });

            swipeContainer.setOnRefreshListener(this);

            /**
             * Showing Swipe Refresh animation on activity create
             * As animation won't start on onCreate, post runnable is used
             */
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {

                 swipeContainer.setRefreshing(true);
                    listViewMovies.setAdapter(adapter);

                    swipeContainer.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            testSwipe();
                            /*Toast.makeText(getApplicationContext(),
                                    "city list refreshed", Toast.LENGTH_SHORT).show();*/
                           swipeContainer.setRefreshing(false);
                        }
                    }, 500);
                }

                ;
            };

        }
        catch(Exception ex)
        {
            Log.d(DEBUG_TAG,ex.getMessage());
        }


    }

    private void testSwipe() {

       // adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),
                "list refreshed", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG_TAG", "Swipe refresh called");

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    private Context getContext()
    {
        return getApplicationContext();
    }
    public  void updateMainViewWithResults(ArrayList<MovieModel> result,MainActivity mainActivity) {
        try
        {

            if(moviesList==null)
            {
                moviesList= new ArrayList<MovieModel>();
            }
            moviesList=result;
         //  swipeContainer.setRefreshing(false);
            //update  main activity here
            ListView listView = (ListView)  mainActivity.findViewById(R.id.movies_list);
            Log.d("DEBUG_TAG", moviesList.toString());
            // Add results to listView.
            Context ctx=mainActivity.getContext();
            MoviesArrayAdapter adapter =
                    new MoviesArrayAdapter (mainActivity.getContext(), moviesList,mainActivity);
            listView.setAdapter(adapter);
            if(listView.getParent()!=null)
            {
                ((ViewGroup)listView.getParent()).removeView(listView);
            }
            // Update Activity to show listView
            mainActivity.setContentView(listView);

        }
        catch (Exception ex)
        {
            Log.d(DEBUG_TAG,ex.getMessage());
        }

    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onRefresh() {

        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
              //  swipeContainer.setRefreshing(true);
                testSwipe();
                handler.sendEmptyMessage(0);
            }
        }, 1000);

    }





   /* @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                new MoviesAsyncTask(context, MainActivity.this).execute();
                handler.sendEmptyMessage(0);
            }
        }, 1000);

    }*/

    //inner class
    private class MoviesArrayAdapter extends ArrayAdapter<MovieModel>{
        MainActivity _mainActivity;
        public MoviesArrayAdapter(Context context, ArrayList<MovieModel> moviesList,MainActivity mainActivity) {
            super(context,0 , moviesList);
            _mainActivity=mainActivity;
            tfMedium =Typeface.createFromAsset(_mainActivity.getAssets(),"fonts/Raleway-Medium.ttf");
            tfLight =Typeface.createFromAsset(_mainActivity.getAssets(),"fonts/Raleway-Light.ttf");
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try
            {
                Log.d(DEBUG_TAG, "...in getView");
                MovieModel movie= getItem(position);
                if(convertView==null)
                {

                    LayoutInflater mInflater = (LayoutInflater) _mainActivity.getContext()
                                                                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = mInflater.inflate(R.layout.movie_row_item, parent, false);
                }

                //set title
                TextView tvTitle=(TextView)convertView.findViewById(R.id.movie_title);
                tvTitle.setTypeface(tfMedium);

                 TextView tvGenre=(TextView)convertView.findViewById(R.id.movie_genre);
                tvGenre.setTypeface(tfLight);

                TextView tvYear=(TextView)convertView.findViewById(R.id.movie_year);

                tvYear.setTypeface(tfLight);
               ImageView ivPosterImage = (ImageView) convertView.findViewById(R.id.imageView);
                Log.d(DEBUG_TAG, movie.getImgURL());
                String imgURL=movie.getImgURL();

                TextView tvID=(TextView)convertView.findViewById(R.id.movie_ID);

               Picasso.with(getContext())
                        .load(movie.getImgURL())
                         .into(ivPosterImage);

                tvTitle.setText(movie.getMovieTitle());
                 tvGenre.setText(movie.getMovieGenre());
                tvID.setText(movie.getMovieID());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
                Date releaseDate=simpleDateFormat.parse(movie.getMovieYear());

                simpleDateFormat = new SimpleDateFormat("YYYY");
                simpleDateFormat.format(releaseDate).toUpperCase();
                tvYear.setText( simpleDateFormat.format(releaseDate).toUpperCase());
            }
            catch(Exception ex)
            {
                Log.d(DEBUG_TAG, ex.getMessage());
            }

            return convertView;
        }
    }



}
