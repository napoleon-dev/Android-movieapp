package com.example.napstar.movieapp2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Napstar on 3/10/2017.
 */

public class OpenMovieDetailAsync_OLD extends AsyncTask  <Integer,Void,ArrayList<MovieDetails>>{
    private Context mContext;
    private MovieDetailsActivity_OLD movieDetailsActivity;
    private MovieDetailsActivity movieDetails_Activity;
    private MovieDetails movieDetailsModel;
    private static final String DEBUG_TAG = "OpenMovieDetailAsync_OLD";



    public OpenMovieDetailAsync_OLD(Context context, MovieDetailsActivity_OLD ma)
    {
        mContext = context;
        movieDetailsActivity=ma;
        movieDetailsModel= new MovieDetails();

        // final ProgressDialog loading= ProgressDialog.show(mainActivity,"Fetching Data","Please wait.....",false,false);
    }
    public OpenMovieDetailAsync_OLD(Context context, MovieDetailsActivity ma)
    {
        mContext = context;
        movieDetails_Activity =ma;
        movieDetailsModel= new MovieDetails();

        // final ProgressDialog loading= ProgressDialog.show(mainActivity,"Fetching Data","Please wait.....",false,false);
    }
   /* @Override
    protected ArrayList<MovieModel> doInBackground(Object[] params) {
        return null;
    }*/

    @Override
    protected void onPostExecute(ArrayList<MovieDetails> result) {

        try
        {
            //add post execute here
            Log.d(DEBUG_TAG,"Starting Post Excecute");
            //update moviedetails activity here
            movieDetails_Activity.updateMainViewWithResults( result, movieDetails_Activity);

        }catch(Exception e)
        {
            Log.d(DEBUG_TAG,e.getMessage());
        }

    }


    @Override
    protected ArrayList<MovieDetails> doInBackground(Integer... params) {
        ArrayList<MovieDetails> results= new ArrayList<MovieDetails>();
        try
        {
            //add post execute here

            Integer id=params[0];
            String strID=Integer.toString(id);
          //  movieDetailsActivity.updateMainViewWithResults((ArrayList<MovieModel>) results,movieDetailsActivity);
            MovieDetails movieDetail=   movieDetailsModel.getMovieDetailsModelByID(id);
            results.add(movieDetail);


            //loading.dismiss();
        }catch(Exception e)
        {
            Log.d(DEBUG_TAG,e.getMessage());

        }
        finally {

        }

        return results;
    }

}
