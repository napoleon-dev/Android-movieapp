package com.example.napstar.movieapp2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Napstar on 3/6/2017.
 */

public class MovieModel {
    String movieTitle;
    String movieGenre;
    String movieYear;
    String imgURL;
    String movieID;
    private static final String DEBUG_TAG = "MovieApp2";
    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }



    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public ArrayList<MovieModel> parseMovieModelResults(String result)
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
