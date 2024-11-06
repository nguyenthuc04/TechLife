package com.snapco.techlife.data.model.home;

public class Feed {

    int image;
    String username, location, feed_img, status, comment_count, date;

    public Feed(int image, String username, String location, String feed_img, String status, String comment_count, String date) {
        this.image = image;
        this.username = username;
        this.location = location;
        this.feed_img = feed_img;
        this.status = status;
        this.comment_count = comment_count;
        this.date = date;
    }

    public Feed withUpdatedStatus(String newStatus) {
        return new Feed(this.image, this.username, this.location, this.feed_img, newStatus, this.comment_count, this.date);
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeed_img() {
        return feed_img;
    }

    public void setFeed_img(String feed_img) {
        this.feed_img = feed_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

