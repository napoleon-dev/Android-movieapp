package com.example.napstar.movieapp2;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Napstar on 3/8/2017.
 */

public class HTTPConnectionObj {

    public static InputStream connectHTTP(URL url) throws IOException
    {

        InputStream inputStream = null;
        try
        {
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
                Log.d(getDebugTag(), "The response code is: " + response + " " + conn.getResponseMessage());

                inputStream = conn.getInputStream();

                return inputStream;
            }
            else
            {
                throw new IOException("Error:Server  Returned "+Integer.toString(response));
            }
        }
        finally
        {

        }
    }

    private static String getDebugTag() {
        return      "HTTPConnectionObj";
    }
}
