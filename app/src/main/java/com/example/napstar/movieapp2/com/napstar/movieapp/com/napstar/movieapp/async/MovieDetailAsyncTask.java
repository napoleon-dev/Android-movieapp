package com.example.napstar.movieapp2.com.napstar.movieapp.com.napstar.movieapp.async;

import android.os.AsyncTask;

import com.example.napstar.movieapp2.com.napstar.movieapp.model.MovieDetails;

/**
 * Created by Napstar on 3/17/2017.
 */

public class MovieDetailAsyncTask extends AsyncTask  <Integer,Void,MovieDetails> {

    @Override
    protected MovieDetails doInBackground(Integer... params) {
          MovieDetails movieDetailsModel = new MovieDetails();
        try
        {
            Integer id=params[0];

            movieDetailsModel=  movieDetailsModel.getMovieDetailsModelByID(id);


        }catch(Exception ex)
        {
            return null;
        }

        return movieDetailsModel;
    }
}
