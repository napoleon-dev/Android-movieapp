package com.example.napstar.movieapp2.com.napstar.movieapp.com.napstar.movieapp.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.napstar.movieapp2.MainActivity;
import com.example.napstar.movieapp2.com.napstar.movieapp.model.MovieModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Napstar on 3/7/2017.
 */

public   class MoviesAsyncTask extends AsyncTask {
    private Context mContext;
    private MainActivity mainActivity;
    private MovieModel movieModel;
    private static final String DEBUG_TAG = "MoviesAsyncTask";
    private ProgressDialog loading=null;

    private MoviesAsyncTask asyncObject;

    //private ProgressDialog loading= ProgressDialog.show(mainActivity,"Fetching Data","Please wait.....",false,false);
    public MoviesAsyncTask(Context context, MainActivity ma)
    {
        mContext = context;
        mainActivity=ma;
        movieModel= new MovieModel();
        this.loading= ProgressDialog.show(mainActivity,"Fetching Data","Please wait.....",false,false);

    }


    protected  void onPreExecute(){

        asyncObject = this;
       this.loading.setCancelable(false);
     //wait for 7 seconds ..assume poor connection
        new CountDownTimer(7000, 7000) {
            public void onTick(long millisUntilFinished) {

                loading.show();
            }
            public void onFinish() {
                // stop async task
                if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {

                    //Connection timed out or taking too long
                    if(loading.isShowing()) {
                        loading.dismiss();
                        loading.setCancelable(true);
                        loading.setMessage("Fetching of movies took too long.Please check your connection and try again");
                    }

                    asyncObject.cancel(false);

                    loading.show();
                }
            }
        }.start();

    }

    protected ArrayList<MovieModel> doInBackground(Object...params)
    {
        try {
            //get now playing moives from url
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

            //update main activity view
            mainActivity.updateMainViewWithResults((ArrayList<MovieModel>) result,mainActivity);
            if(this.loading.isShowing())
            {
                Thread.sleep(5000);
                this.loading.dismiss();
            }

        }catch(Exception e)
        {
            Log.d(DEBUG_TAG,e.getMessage());
        }

    }










}
