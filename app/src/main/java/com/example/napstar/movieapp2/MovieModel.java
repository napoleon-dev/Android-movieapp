package com.example.napstar.movieapp2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.collection.*;

/**
 * Created by Napstar on 3/6/2017.
 */

public class MovieModel {
    private String movieTitle;
    private String movieGenre;
    private String movieYear;
    private String imgURL;
    private String movieID;
    private List<GenreModel> genreList;
    private static final String DEBUG_TAG = "MovieModel";

    public List<GenreModel> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<GenreModel> genreList) {
        this.genreList = genreList;
    }






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
        ArrayList<GenreModel>genreList= new ArrayList<GenreModel>();
        GenreModel genreModel= new GenreModel();

        //get genre list
        try {
            genreList=genreModel.getallGenres();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "Error parsing Getting Genre List"+" "+e.getMessage());
        }


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
                //get genre IDs
                JSONArray jsonGenreArr = c.getJSONArray("genre_ids");
                if(jsonGenreArr.length()>0)
                {
                    StringBuilder sbGenres= new StringBuilder();
                    for(int j=0;j<jsonGenreArr.length();j++)
                    {
                        Integer _ID  = jsonGenreArr.getInt(j) ;

                        if(_ID>0)
                        {
                            for(GenreModel genre:genreList)
                            {
                                if(genre.getGenreID()==_ID)
                                {
                                    if(sbGenres.length()<=0)
                                    {
                                        sbGenres.append(genre.get_genre());
                                        break;
                                    }
                                    else
                                    {
                                        sbGenres.append(",").append(genre.get_genre());
                                        break;
                                    }


                                }
                            }
                        }
                        //apend genre
                        movieModel.setMovieGenre(sbGenres.toString());
                    }
                }
                else{
                    //no genres returned
                }


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

    public ArrayList<MovieModel> getNowPlayingMovies() throws IOException {
        //get now playing movies from open movies DB
        try{
            MovieModel movieModel= new MovieModel();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(MyUtilClass.getNowPlayingMoviesURL());
            stringBuilder.append("?api_key=" + MyUtilClass.getAPIKey());
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

                return movieModel.parseMovieModelResults(MyUtilClass.stringify(inputStream));
            }
            else
            {
                throw new IOException("Error:Server  Returned "+Integer.toString(response));
            }

        }
        finally {

        }
    }
    public String getAllGenres(ArrayList<GenreModel> genreList,Integer genredID)
    {
        StringBuilder sbGenres= new StringBuilder();
         List<GenreModel> list=genreList;
            for(GenreModel genre:genreList)
            {
                if(genre.getGenreID()==genredID)
                {

                    if(sbGenres.length()<1)
                    {
                        sbGenres.append(genre.get_genre());
                    }
                    else
                    {
                        sbGenres.append(genre.get_genre()).append(",");
                    }
                }
            }


        return sbGenres.toString();
    }
}
