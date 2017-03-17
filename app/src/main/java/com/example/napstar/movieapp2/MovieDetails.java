package com.example.napstar.movieapp2;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Napstar on 3/10/2017.
 */

public class MovieDetails {
    private String movieTitle;
    private String movieGenre;
    private String movieYear;
    private String imgURL;
    private String movieID;
    private List<GenreModel> genreList;

    private Integer budget;
    private String homepage;
    private String imdbID;
    private String overview;
    public Integer runtime;
    public Integer revenue;
    public String tagline;
    private Boolean adult;
    private static final String DEBUG_TAG = "MovieDetailsModel";
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

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public List<GenreModel> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<GenreModel> genreList) {
        this.genreList = genreList;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }




    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    public MovieDetails parseMovieDetailsModelResults(String result)
    {
        ArrayList<GenreModel>genreList= new ArrayList<GenreModel>();
        GenreModel genreModel= new GenreModel();

        //get genre list
        try {
            genreList=genreModel.getallGenres();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "Error parsing Getting Genre List"+" "+e.getMessage());
        }


       // ArrayList<MovieDetails> results = new ArrayList<MovieDetails>();
        MovieDetails movieDetailsModel= new MovieDetails();
        String streamAsString = result;

        //results is an int,query is failing that why you have an int
        try{
            JSONObject jsonObject = new JSONObject(streamAsString);
           //
            // JSONArray array = (JSONArray) jsonObject.get("result");
            //title
            if(jsonObject.getString("title")!=null)
            {
                movieDetailsModel.setMovieTitle(jsonObject.getString("title"));
            }
            //movie year
            movieDetailsModel.setMovieYear(jsonObject.getString("release_date"));
            //movieID
            movieDetailsModel.setMovieID(jsonObject.getString("id"));
            //is movie adult
            String strAdult=jsonObject.getString("adult");
            if(strAdult !=null && !strAdult.isEmpty())
            {
                Boolean adult=Boolean.parseBoolean(strAdult);
                movieDetailsModel.setAdult(adult);

            }
            //movie image poster
            String strImgURL="https://image.tmdb.org/t/p/w500"+jsonObject.getString("poster_path");
            movieDetailsModel.setImgURL(strImgURL);

            //overview
            if(jsonObject.getString("overview")!=null)
            {
                movieDetailsModel.setOverview(jsonObject.getString("overview"));
            }
            //tagline
            if(jsonObject.getString("tagline")!=null)
            {
                movieDetailsModel.setTagline(jsonObject.getString("tagline"));
            }
            //get genre IDs
            JSONArray jsonGenreArr = jsonObject.getJSONArray("genres");
            if(jsonGenreArr.length()>0)
            {
                StringBuilder sbGenres= new StringBuilder();
                for(int j=0;j<jsonGenreArr.length();j++)
                {
                   // Integer _ID  = jsonGenreArr.getInt(j) ;
                    JSONObject jObject=jsonGenreArr.getJSONObject(j);
                    String strJsonID=jObject.getString("id");
                    Integer _ID=Integer.parseInt(strJsonID);
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
                    movieDetailsModel.setMovieGenre(sbGenres.toString());
                }
            }
            else{
                //no genres returned
            }
        }
        catch(JSONException j)
        {
            System.err.println(j);
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + j.toString());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + ex.toString());
        }

        return movieDetailsModel;
    }

    public MovieDetails getMovieDetailsModelByID(Integer movieID) throws IOException
    {
        MovieDetails movieDetails= new MovieDetails();
        try
        {
            //is movie id valid?
            if(movieID>=1)
            {
                //parse movie id
                String strMovieID=Integer.toString(movieID);
                MovieDetails movieDetailsModel= new MovieDetails();
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(MyUtilClass.getMovieDetailsURL(strMovieID));
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

                    return movieDetailsModel.parseMovieDetailsModelResults(MyUtilClass.stringify(inputStream));
                }
                else
                {
                    throw new IOException("Error:Server  Returned "+Integer.toString(response));
                }
            }
            else{
                throw new Exception("Invalid Movie ID");
            }

        } catch (Exception e) {
            Log.d(DEBUG_TAG,e.getMessage());
            return movieDetails;
        } finally{

        }

    }
}
