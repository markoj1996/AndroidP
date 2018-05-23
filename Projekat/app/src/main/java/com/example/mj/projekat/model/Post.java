package com.example.mj.projekat.model;


import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.List;

public class Post {

    private int id;
    private String title;
    private String description;
    private Bitmap photo;
    private String author;
    private String date;
    private String location;
    private List<Tag> tags;
    private List<Comment> comments;
    private int likes;
    private int dislikes;

    public  Post (String title,String description,Bitmap photo)
    {
        this.title = title;
        this.description = description;
        this.photo = photo;
    }

    public Post(){};

    public Post(int id, String title, String description, Bitmap photo, int likes, int dislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Post(int id, String title, String description, Bitmap photo, String author, String date, String location, int likes, int dislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.author = author;
        this.date = date;
        this.location = location;
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
}
