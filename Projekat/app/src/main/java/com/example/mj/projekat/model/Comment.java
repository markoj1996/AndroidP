package com.example.mj.projekat.model;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

public class Comment implements Comparable {

    private int id;
    private String title;
    private String description;
    private User author;
    private Date date;
    private int post;
    private int likes;
    private int dislikes;

    public Comment(){}


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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
