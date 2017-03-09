package com.example.napstar.movieapp2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Napstar on 3/8/2017.
 */

public class GenreModel {

    //instance variables
    private Integer genreID;
    private String _genre;

    //getters and setters
    public Integer getGenreID() {
        return genreID;
    }

    public void setGenreID(Integer genreID) {
        this.genreID = genreID;
    }

    public String get_genre() {
        return _genre;
    }

    public void set_genre(String _genre) {
        this._genre = _genre;
    }

    //constructor
    public GenreModel()
    {

    }
    public GenreModel(Integer id,String genre)
    {
        this.genreID=id;
        this._genre = genre;
    }
    public ArrayList<GenreModel> parseGenreResults(String strResult)
    {
        ArrayList<GenreModel> genreModelList = new ArrayList<GenreModel>();
        try{
            String streamAsString = strResult;
            JSONObject jsonObject = new JSONObject(streamAsString);
            JSONArray array = (JSONArray) jsonObject.get("genres");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject c = array.getJSONObject(i);

                JSONObject jsonMovie = array.getJSONObject(i);
                GenreModel genreModel= new GenreModel();
                String strID=jsonMovie.getString("id");
                if(strID !=null && !strID.isEmpty())
                {
                    genreModel.setGenreID(Integer.parseInt(strID));
                }
                else{
                    //throw exception empty or error in Genre ID
                    throw new IllegalArgumentException("Invalid argument in Genre ID");
                }
                String strGenreName=jsonMovie.getString("name");
                genreModel.set_genre(strGenreName);
                Log.d(MyUtilClass.getGenreDebugTag(),Integer.toString(jsonMovie.length()));


                genreModelList.add((genreModel));
            }

        }catch (JSONException j)
        {
            Log.d(MyUtilClass.getGenreDebugTag(),j.getMessage());
        }
        catch (Exception e)
        {
            Log.d(MyUtilClass.getGenreDebugTag(),e.getMessage());
        }
        return  genreModelList;
    }
    public ArrayList<GenreModel> getallGenres() throws IOException{
        try
        {
            InputStream inputStream = null;
            inputStream=HTTPConnectionObj.connectHTTP(MyUtilClass.getGenreURL());
            return parseGenreResults(MyUtilClass.stringify(inputStream));
        }
        finally
        {

        }
    }

    public String findGenre(ArrayList<GenreModel> genreList,String searchGenreID)
    {
        String strGenre="";
        try
        {
            //check to see if list is null
            if(genreList!=null)
            {
                //are there items in the genre list?
                if(!genreList.isEmpty())
                {
                    for(GenreModel genre:genreList)
                    {
                        Integer currentGenreID=Integer.parseInt(searchGenreID);
                        if(currentGenreID==genre.getGenreID())
                        {
                            strGenre=genre.get_genre();

                        }
                    }
                    //did we get back a value?
                    if(strGenre.isEmpty() )
                    {
                        strGenre="na";
                    }
                }
                else
                {
                    //list is empty,throw error
                    throw new IllegalArgumentException("Genre List is Empty");
                }
            }
            else
            {
                //list is null,throw error
                throw new IllegalArgumentException("Genre List is null");
            }
        }
        catch (Exception ex)
        {
            Log.d(MyUtilClass.getGenreDebugTag(),ex.getMessage());
        }

        return strGenre;
    }
}
