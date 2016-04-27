package com.example.my.moviesapp.Network;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.example.my.moviesapp.Constants.Constants;
import com.example.my.moviesapp.Utilities.Sender;


public class MoviesDataTask extends AsyncTask<String, Void, String> {

    String restOFuRL;
    Sender sender;
    int type;

    public MoviesDataTask(String restOFuRL, int type, Sender sender) {

        this.restOFuRL = restOFuRL;
        this.type = type;
        this.sender = sender;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String Url;
            if (type == Constants.TYPE_MOVIES) {
                Url = Constants.BASE_URL_API + restOFuRL + Constants.API_KEY;
            } else {
                Url = Constants.BASE_URL_API + params[0] + restOFuRL + Constants.API_KEY;
            }

            HttpURLConnection connection = ConnectionManager.openConnection(Url);
            String responseData = ConnectionManager.getDataFromConnection(connection);
            ConnectionManager.closeConnection();
            return responseData;

        } catch (IOException e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(String data) {
        //send response data back to the activity
        sender.sendData(data);

    }
}



