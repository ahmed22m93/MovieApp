package com.example.my.moviesapp.DataBase;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favoriteMovies.db";

    ////////////////////////////
    protected static final String TABLE_MOVIE_NAME = "movie";
    protected static final String TABLE_TRAILER_NAME = "trailer";
    protected static final String TABLE_REVIEW_NAME = "review";

    ///////////////////////////
    protected static final String COLUMN_ID = "_id";
    protected static final String COLUMN_TITLE = "title";
    protected static final String COLUMN_POSTER_PATH = "posterPath";
    protected static final String COLUMN_OVERVIEW = "overview";
    protected static final String COLUMN_VOTE_AVERAGE = "voteAverage";
    protected static final String COLUMN_RELEASE_DATE = "releaseDate";

    ///////////////////////////
    protected static final String COLUMN_TRAILER_URL = "trailerUrl";

    ///////////////////////////
    protected static final String COLUMN_REVIEW_AUTHOR = "author";
    protected static final String COLUMN_REVIEW_CONTENT = "content";


    public FavoriteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryMovie = "CREATE TABLE " + TABLE_MOVIE_NAME + "(" +
                COLUMN_ID + " TEXT  PRIMARY KEY," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_POSTER_PATH + " TEXT ," +
                COLUMN_OVERVIEW + " TEXT ," +
                COLUMN_VOTE_AVERAGE + " REAL ," +
                COLUMN_RELEASE_DATE + " TEXT " +
                ");";

        String queryTrailer = "CREATE TABLE " + TABLE_TRAILER_NAME + "(" +
                COLUMN_ID + " TEXT ," +
                COLUMN_TRAILER_URL + " TEXT " +
                ");";

        String queryReview = "CREATE TABLE " + TABLE_REVIEW_NAME + "(" +
                COLUMN_ID + " TEXT ," +
                COLUMN_REVIEW_AUTHOR + " TEXT ," +
                COLUMN_REVIEW_CONTENT + " TEXT " +
                ");";

        db.execSQL(queryMovie);
        db.execSQL(queryTrailer);
        db.execSQL(queryReview);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW_NAME);
        onCreate(db);
    }

}
