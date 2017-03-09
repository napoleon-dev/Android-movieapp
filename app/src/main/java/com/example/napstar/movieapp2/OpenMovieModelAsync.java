package com.example.napstar.movieapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Napstar on 3/7/2017.
 */

public   class OpenMovieModelAsync extends AsyncTask {
    private Context mContext;
    private MainActivity mainActivity;
    private MovieModel movieModel;
    private static final String DEBUG_TAG = "OpenMovieModelAsync";

    public OpenMovieModelAsync(Context context, MainActivity ma)
    {
        mContext = context;
        mainActivity=ma;
        movieModel= new MovieModel();

        // final ProgressDialog loading= ProgressDialog.show(mainActivity,"Fetching Data","Please wait.....",false,false);
    }


    protected  void onPreExecute(){
       /* loading.setCancelable(false);
        loading.setMessage("Fetching Data ..Please Wait");
        loading.show();*/
    }

    protected ArrayList<MovieModel> doInBackground(Object...params)
    {
        try {
            return movieModel.getNowPlayingMovies();
        }catch(IOException e) {
            Log.d(DEBUG_TAG,e.getMessage());
            return null;
        }
    }


    @Override
    protected void onPostExecute(Object result) {

        try
        {
            //add post execute here
            Log.d(DEBUG_TAG,"Starting Post Excecute");

            mainActivity.updateMainViewWithResults((ArrayList<MovieModel>) result,mainActivity);
            //loading.dismiss();
        }catch(Exception e)
        {
            Log.d(DEBUG_TAG,e.getMessage());
        }

    }










}
