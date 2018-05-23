package com.example.mj.projekat.model;

import android.graphics.Bitmap;

import java.util.List;

public class User {

    private int id;
    private String name;
    private Bitmap photo;
    private String username;
    private String password;
    private List<Post> posts;
    private List<Comment> comments;
    private List<Integer> likedPosts;

    public User()
    {

    }

    public User(int id,String name, String username, String password) {
        this.id = id;
        this.username = username;
        this.name=name;
        this.password = password;
    }

    public User(int id, String name, Bitmap photo, String username, String password) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public List<Integer> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
