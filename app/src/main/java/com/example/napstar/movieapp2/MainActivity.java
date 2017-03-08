package com.example.napstar.movieapp2;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ArrayList<MovieModel> moviesList;
    Context context;
    private final String API_KEY = "a347ef1926ae724eb261182a8de57b59";
    private static final String DEBUG_TAG = "MovieApp2";
    private static final String strURL="https://api.themoviedb.org/3/movie/now_playing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //movielis
        moviesList= new ArrayList<MovieModel>();
        context=getApplicationContext();
        // Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new OpenMovieAsync(context,this).execute();
        } else {
            Log.d(DEBUG_TAG,"No Network Connection");

        }
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
            //update  main activity here
          //  ListView listView = (ListView)findViewById(R.id.movies_list);
            //TextView textView = (TextView) ((Activity) context).findViewById(R.id.textView1);

            ListView listView = (ListView)  mainActivity.findViewById(R.id.movies_list);
            Log.d("upd8MainViewWithResults", result.toString());
            // Add results to listView.
            Context ctx=mainActivity.getContext();
            MoviesArrayAdapter adapter =
                    new MoviesArrayAdapter (mainActivity.getContext(), result,mainActivity);
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
//inner class


    private class MoviesArrayAdapter extends ArrayAdapter<MovieModel>{
        MainActivity _mainActivity;
        public MoviesArrayAdapter(Context context, ArrayList<MovieModel> moviesList,MainActivity mainActivity) {
            super(context,0 , moviesList);
            _mainActivity=mainActivity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try
            {
                Log.d(DEBUG_TAG, "...in getView");
                MovieModel movie= getItem(position);
                if(convertView==null)
                {
                    //((ViewGroup)convertView.getParent()).removeView(convertView);
                   // LayoutInflater inflater = LayoutInflater.from(getContext());
                    LayoutInflater mInflater = (LayoutInflater) _mainActivity.getContext()
                                                                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = mInflater.inflate(R.layout.movie_row_item, parent, false);
                }
                //set title
                TextView tvTitle=(TextView)convertView.findViewById(R.id.movie_title);
               // TextView tvGenre=(TextView)convertView.findViewById(R.id.movie_genre);
                TextView tvYear=(TextView)convertView.findViewById(R.id.movie_year);

               ImageView ivPosterImage = (ImageView) convertView.findViewById(R.id.imageView);
                Log.d(DEBUG_TAG, movie.getImgURL());
                String imgURL=movie.getImgURL();
               //Picasso.with(getContext()).load(movie.getImgURL()).into(ivPosterImage);

               Picasso.with(getContext())
                        .load(movie.getImgURL())
                         .into(ivPosterImage);

                tvTitle.setText(movie.getMovieTitle());
                //tvGenre.setText(movie.getMovieGenre());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
                Date date1=simpleDateFormat.parse(movie.getMovieYear());

                simpleDateFormat = new SimpleDateFormat("YYYY");
                simpleDateFormat.format(date1).toUpperCase();
                tvYear.setText( simpleDateFormat.format(date1).toUpperCase());
            }
            catch(Exception ex)
            {
                Log.d(DEBUG_TAG, ex.getMessage());
            }

            return convertView;
        }
    }


}
