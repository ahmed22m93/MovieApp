package com.example.my.moviesapp.Models;


public class Review {

    private String author;
    private String comment;

    public Review(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public void setComment(String content) {
        this.comment = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {

        return author;
    }

    public String getComment() {
        return comment;
    }
}
