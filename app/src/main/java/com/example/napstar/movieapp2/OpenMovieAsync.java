package com.example.napstar.movieapp2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public   class OpenMovieAsync extends AsyncTask {
    private Context mContext;
    private MainActivity mainActivity;
    public OpenMovieAsync(Context context,MainActivity ma)
    {
        mContext = context;
        mainActivity=ma;
    }
    private final String API_KEY = "a347ef1926ae724eb261182a8de57b59";
    private static final String DEBUG_TAG = "MovieApp2";
    private static final String strURL="https://api.themoviedb.org/3/movie/now_playing";
    protected ArrayList<MovieModel> doInBackground(Object...params)
    {
        try {
            return getNowPlayingMovies();
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
            //MainActivity ma= new MainActivity();
            mainActivity.updateMainViewWithResults((ArrayList<MovieModel>) result,mainActivity);
        }catch(Exception e)
        {
            Log.d(DEBUG_TAG,e.getMessage());
        }

    }





    private ArrayList<MovieModel> getNowPlayingMovies() throws IOException {
        //get now playing movies from open movies DB
        try{
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(strURL);
            stringBuilder.append("?api_key=" + API_KEY);
            stringBuilder.append("&query=" + "language=en-US&page=1");
            URL url = new URL(stringBuilder.toString());
            InputStream inputStream = null;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept", "application/json");
            conn.setDoInput(true);

            // Establish a connection
            conn.connect();

            int response = conn.getResponseCode();
            if(response==200)
            {
                Log.d(DEBUG_TAG, "The response code is: " + response + " " + conn.getResponseMessage());

                inputStream = conn.getInputStream();

                return parseMovieResults(stringify(inputStream));
            }
            else
            {
                throw new IOException("Error:Server  Returned "+Integer.toString(response));
            }

        }
        finally {

        }
    }

    private String stringify(InputStream  stream)throws IOException {
        Reader rdr = null;
        rdr = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedRdr = new BufferedReader(rdr);
        return bufferedRdr.readLine();
    }

    private ArrayList<MovieModel> parseMovieResults(String result)
    {
        ArrayList<MovieModel> results = new ArrayList<MovieModel>();
        String streamAsString = result;
        try{
            JSONObject jsonObject = new JSONObject(streamAsString);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject c = array.getJSONObject(i);

                JSONObject jsonMovie = array.getJSONObject(i);
                MovieModel movieModel= new MovieModel();
                movieModel.setMovieTitle(jsonMovie.getString("title"));
                movieModel.setMovieGenre("na");;
                Log.d(DEBUG_TAG,Integer.toString(jsonMovie.length()));
                String strImgURL="https://image.tmdb.org/t/p/w500"+jsonMovie.getString("poster_path");
                movieModel.setImgURL(strImgURL);
                movieModel.setMovieYear(jsonMovie.getString("release_date"));
                movieModel.setMovieID(jsonMovie.getString("id"));


                results.add((movieModel));
            }
        }
        catch(JSONException j)
        {
            System.err.println(j);
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + j.toString());
        }

        return results;
    }
}
