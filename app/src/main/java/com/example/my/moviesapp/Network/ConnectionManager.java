package com.example.my.moviesapp.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectionManager {

    private static final String RequestMethod = "GET";
    private static HttpURLConnection connection;


    public static HttpURLConnection openConnection(String urlString) throws IOException {

        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(RequestMethod);
        connection.setReadTimeout(8000);
        connection.setConnectTimeout(8000);
        connection.connect();

        return connection;
    }

    public static String getDataFromConnection(HttpURLConnection connection) throws IOException {

        InputStream inputStream = connection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");

        }
        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        return (buffer.toString());
    }

    public static void closeConnection(){
        connection.disconnect();
    }

}
