package com.example.my.moviesapp.Utilities;


import com.example.my.moviesapp.Models.Movie;
import com.example.my.moviesapp.Models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser {

    private static ArrayList<Movie> movies;

    public static ArrayList<Movie> parseApiData(String data) {

        movies = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject movie = result.getJSONObject(i);
                addMovie(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }


    public static void addMovie(JSONObject movie) throws JSONException {

        String id = movie.getString("id");
        String originalTitle = movie.getString("original_title");
        String posterPath = movie.getString("poster_path");
        String overview = movie.getString("overview");
        float voteAverage = movie.getLong("vote_average");
        String releaseDate = movie.getString("release_date");

        Movie m = new Movie(id, originalTitle, posterPath, overview, voteAverage, releaseDate);
        movies.add(m);
    }


    public static ArrayList<String> parseTrailerData(String data) {
        ArrayList<String> allTrailer = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonTrailer = result.getJSONObject(i);
                String trailer = jsonTrailer.getString("key");
                allTrailer.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allTrailer;
    }

    public static ArrayList<Review> parseReviewData(String data) {
        ArrayList<Review> allTrailer = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonTrailer = result.getJSONObject(i);
                String author = jsonTrailer.getString("author");
                String content = jsonTrailer.getString("content");
                Review review = new Review(author, content);
                allTrailer.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allTrailer;
    }

}
