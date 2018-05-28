package com.example.mj.projekat.model;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

public class Comment implements Comparable {

    private int id;
    private String title;
    private String description;
    private String author;
    private String date;
    private int post;
    private int likes;
    private int dislikes;

    public Comment(){}

    public Comment(int id, String title, String description, String author, String date, int post, int likes, int dislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.post = post;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareLike = ((Comment)o).getLikes();
        return compareLike-this.getLikes();
    }
}
