package com.example.napstar.movieapp2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDetailsActivity_OLD extends AppCompatActivity {
    private static final String DEBUG_TAG = "MovieDetials";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_old);
        //get the movie id from intent
        try
        {
            context=getApplicationContext();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String strMovieID = extras.getString("movieID");
                //The key argument here must match that used in the other activity
                Toast.makeText(getApplicationContext(),strMovieID,Toast.LENGTH_SHORT).show();
                 //is this valid id?
                 Integer movieID=0;
                movieID=Integer.parseInt(strMovieID);
                if(movieID>=1)
                {
                    // Check if the NetworkConnection is active and connected.
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        new OpenMovieDetailAsync_OLD(context,this).execute(movieID);

                    } else {
                        Log.d(DEBUG_TAG,"No Network Connection");

                    }
                }
                else
                {
                    //invalid movie id
                }

            }
        }catch(Exception ex)
        {
            Log.d(DEBUG_TAG,ex.getMessage());
        }

    }

    public void updateMainViewWithResults(ArrayList<MovieDetails> results, MovieDetailsActivity_OLD movieDetailsActivity) {

        try
        {
            ListView listView = (ListView)  movieDetailsActivity.findViewById(R.id.moviedetails_list);
            //get context
            MoviesDetailsArrayAdapter adapter =
                                    new MoviesDetailsArrayAdapter
                                            (movieDetailsActivity.getApplicationContext(),results,movieDetailsActivity);
            listView.setAdapter(adapter);
            if(listView.getParent()!=null)
            {
                ((ViewGroup)listView.getParent()).removeView(listView);
            }
            // Update Activity to show listView
            movieDetailsActivity.setContentView(listView);

        }
        catch (Exception ex)
        {
            Log.d(DEBUG_TAG,ex.getMessage());
        }
    }

    //adapter class
    private class MoviesDetailsArrayAdapter extends ArrayAdapter<MovieDetails> {
        MovieDetailsActivity_OLD _moviesDetailsActivity;
        public MoviesDetailsArrayAdapter(Context context, ArrayList<MovieDetails> moviesList, MovieDetailsActivity_OLD movieDetailsActivity) {
            super(context,0 , moviesList);
            _moviesDetailsActivity=movieDetailsActivity;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try
            {
                Typeface   tfMedium = Typeface.createFromAsset(_moviesDetailsActivity.getAssets(),"fonts/Raleway-Medium.ttf");
                Typeface   tfLight =Typeface.createFromAsset(_moviesDetailsActivity.getAssets(),"fonts/Raleway-Light.ttf");
                Log.d(DEBUG_TAG, "...in getView");
                MovieDetails movie= getItem(position);
                if(convertView==null)
                {

                    LayoutInflater mInflater = (LayoutInflater) _moviesDetailsActivity.getApplicationContext()
                            .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = mInflater.inflate(R.layout.movie_row_details_old, parent, false);
                }
                //Get title textbox and set typeface
                TextView tvTitle=(TextView)convertView.findViewById(R.id.txt_movie_title);
                tvTitle.setTypeface(tfMedium);
                //Get genre textbox and set typeface
                TextView tvGenre=(TextView)convertView.findViewById(R.id.txt_movie_genre);
                tvGenre.setTypeface(tfLight);

                //Set Image
                ImageView ivPosterImage = (ImageView) convertView.findViewById(R.id.img_Poster);
                 if(movie.getImgURL()!=null && !movie.getImgURL().isEmpty())
                 {
                     String imgURL=movie.getImgURL();

                     Picasso.with(getContext())
                             .load(movie.getImgURL())
                             .into(ivPosterImage);
                 }

                //set title,genre
                tvTitle.setText(movie.getMovieTitle());
                tvGenre.setText(movie.getMovieGenre());

                //set txtyear typeface and value
                TextView tvYear=(TextView)convertView.findViewById(R.id.txtYear);
                tvYear.setTypeface(tfLight);
                //format date to show only year
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
                Date releaseDate=simpleDateFormat.parse(movie.getMovieYear());

                simpleDateFormat = new SimpleDateFormat("YYYY");
                simpleDateFormat.format(releaseDate).toUpperCase();
                tvYear.setText( simpleDateFormat.format(releaseDate).toUpperCase());
                //set overview
                TextView tvOverview=(TextView)convertView.findViewById(R.id.txt_movie_overview);
                tvOverview.setTypeface(tfMedium);
                tvOverview.setText(movie.getOverview());
            }
            catch(Exception ex)
            {
                Log.d(DEBUG_TAG, ex.getMessage());
            }

            return convertView;
        }
    }
}
