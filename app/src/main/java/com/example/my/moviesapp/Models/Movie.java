package com.example.my.moviesapp.Models;


import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Serializable {


    private String id;
    private String title;
    private String posterPath;
    private String overview;
    private float voteAverage;
    private String releaseDate;


    public Movie(String id , String title, String posterPath, String overview, float voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public void setKey (String id){
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public  String getId(){
        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}

