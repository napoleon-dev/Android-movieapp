package com.example.napstar.movieapp2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Napstar on 3/8/2017.
 */

public class MyUtilClass {


       public static String stringify(InputStream stream)throws IOException {
        Reader rdr = null;
        rdr = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedRdr = new BufferedReader(rdr);
        return bufferedRdr.readLine();
    }
    public static String getAPIKey()
    {
        return "a347ef1926ae724eb261182a8de57b59";
    }
    private static String getDebugTag()
    {
        final String DEBUG_TAG = "MovieApp2";
        return DEBUG_TAG;
    }
    public static String getGenreDebugTag()
    {
        final String DEBUG_TAG = "Genre";
        return DEBUG_TAG;
    }

    public static String getNowPlayingMoviesURL()
    {
        String strURL="https://api.themoviedb.org/3/movie/now_playing";
        return  strURL;
    }
    public  static URL getGenreURL()
    {
        String strUrl="https://api.themoviedb.org/3/genre/movie/list?api_key="+getAPIKey();
        URL url= null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static String getMovieDetailsURL(String  strMovieID)
    {
        String strUrl="https://api.themoviedb.org/3/movie/"+strMovieID;
        return  strUrl;
    }

}
