package com.example.my.moviesapp.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.my.moviesapp.Models.Movie;
import com.example.my.moviesapp.Models.Review;

import java.util.ArrayList;

public class FavoriteDBParser {

    private FavoriteDB dbHelper;

    public FavoriteDBParser(Context context) {
        this.dbHelper = new FavoriteDB(context, null, null, 1);
    }

    //////////////////// work with movie table ///////////////////
    public int addMovie(Movie movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues rowValues = new ContentValues();
        rowValues.put(FavoriteDB.COLUMN_ID, movie.getId());
        rowValues.put(FavoriteDB.COLUMN_TITLE, movie.getTitle());
        rowValues.put(FavoriteDB.COLUMN_POSTER_PATH, movie.getPosterPath());
        rowValues.put(FavoriteDB.COLUMN_OVERVIEW, movie.getOverview());
        rowValues.put(FavoriteDB.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        rowValues.put(FavoriteDB.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        try {
            db.insertOrThrow(FavoriteDB.TABLE_MOVIE_NAME, null, rowValues);
            return 1;
        } catch (Exception e) {
            return 0;
        } finally {
            db.close();
        }
    }

    public boolean checkMovieExist(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FavoriteDB.TABLE_MOVIE_NAME + " WHERE " + FavoriteDB.COLUMN_ID + " = ?", new String[]{movieId});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public ArrayList<Movie> displayMovies() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM " + FavoriteDB.TABLE_MOVIE_NAME;
        ArrayList<Movie> restaurantList = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);    //Cursor point to a location in your results.
        c.moveToFirst();     //Move to the first in your results.

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(FavoriteDB.COLUMN_ID)) != null) {

                Movie movie = new Movie(c.getString(c.getColumnIndex(FavoriteDB.COLUMN_ID))
                        , c.getString(c.getColumnIndex(FavoriteDB.COLUMN_TITLE))
                        , c.getString(c.getColumnIndex(FavoriteDB.COLUMN_POSTER_PATH))
                        , c.getString(c.getColumnIndex(FavoriteDB.COLUMN_OVERVIEW))
                        , c.getFloat(c.getColumnIndex(FavoriteDB.COLUMN_VOTE_AVERAGE))
                        , c.getString(c.getColumnIndex(FavoriteDB.COLUMN_RELEASE_DATE)));

                restaurantList.add(movie);
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return restaurantList;
    }

    public int deleteMovie(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.delete(FavoriteDB.TABLE_MOVIE_NAME, FavoriteDB.COLUMN_ID + " = ? ;", new String[]{movieId}) == 0) {
            db.close();
            return 0;
        }

        db.close();
        return 1;
    }


    //////////////////// work with trailer table ///////////////////
    public int addMovieTrailers(String movieId, ArrayList<String> trailersUrlList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = trailersUrlList.size();
        for (int i = 0; i < count; i++) {
            String movieTrailer = trailersUrlList.get(i);
            ContentValues rowValues = new ContentValues();
            rowValues.put(FavoriteDB.COLUMN_ID, movieId);
            rowValues.put(FavoriteDB.COLUMN_TRAILER_URL, movieTrailer);
            try {
                db.insertOrThrow(FavoriteDB.TABLE_TRAILER_NAME, null, rowValues);
            } catch (Exception e) {
                db.close();
                return 0;
            }
        }
        db.close();
        return 1;
    }

    public ArrayList<String> displayMovieTrailers(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<String> MovieTrailersList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + FavoriteDB.TABLE_TRAILER_NAME + " WHERE " + FavoriteDB.COLUMN_ID + " = ?", new String[]{movieId});    //Cursor point to a location in your results.
        c.moveToFirst();     //Move to the first in your results.

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(FavoriteDB.COLUMN_ID)) != null) {
                String trailerUrl = c.getString(c.getColumnIndex(FavoriteDB.COLUMN_TRAILER_URL));
                MovieTrailersList.add(trailerUrl);
                c.moveToNext();
            }
        }

        c.close();
        db.close();
        return MovieTrailersList;
    }

    public int deleteMovieTrailers(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.delete(FavoriteDB.TABLE_TRAILER_NAME, FavoriteDB.COLUMN_ID + " = ? ;", new String[]{movieId}) == 0) {
            db.close();
            return 0;
        }

        db.close();
        return 1;
    }

    //////////////////// work with review table ///////////////////
    public int addMovieReviews(String movieId, ArrayList<Review> reviewsList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = reviewsList.size();
        for (int i = 0; i < count; i++) {
            Review movieReview = reviewsList.get(i);
            ContentValues rowValues = new ContentValues();
            rowValues.put(FavoriteDB.COLUMN_ID, movieId);
            rowValues.put(FavoriteDB.COLUMN_REVIEW_AUTHOR, movieReview.getAuthor());
            rowValues.put(FavoriteDB.COLUMN_REVIEW_CONTENT, movieReview.getComment());
            try {
                db.insertOrThrow(FavoriteDB.TABLE_REVIEW_NAME, null, rowValues);
            } catch (Exception e) {
                db.close();
                return 0;
            }
        }
        db.close();
        return 1;
    }

    public ArrayList<Review> displayMovieReviews(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Review> MovieReviewsList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + FavoriteDB.TABLE_REVIEW_NAME + " WHERE " + FavoriteDB.COLUMN_ID + " = ?", new String[]{movieId});    //Cursor point to a location in your results.
        c.moveToFirst();     //Move to the first in your results.

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(FavoriteDB.COLUMN_ID)) != null) {
                Review review = new Review(c.getString(c.getColumnIndex(FavoriteDB.COLUMN_REVIEW_AUTHOR))
                ,c.getString(c.getColumnIndex(FavoriteDB.COLUMN_REVIEW_CONTENT))) ;
                MovieReviewsList.add(review);
                c.moveToNext();
            }
        }

        c.close();
        db.close();
        return MovieReviewsList;
    }

    public int deleteMovieReviews(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.delete(FavoriteDB.TABLE_REVIEW_NAME, FavoriteDB.COLUMN_ID + " = ? ;", new String[]{movieId}) == 0) {
            db.close();
            return 0;
        }

        db.close();
        return 1;
    }
}
