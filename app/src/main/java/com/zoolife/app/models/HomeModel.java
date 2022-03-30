package com.zoolife.app.models;


import java.io.Serializable;

public class HomeModel implements Serializable {

    public String title, postedDate, postedBy, location, username;
    public String image, id, priority;
    private int viewType = 0;

    public HomeModel() {
    }

    public HomeModel(String title, String postedDate, String location, String username, String image, String id, String priority) {
        this.title = title;
        this.postedDate = postedDate;
        this.location = location;
        this.username = username;
        this.image = image;
        this.id = id;
        this.priority = priority;
        viewType = 0;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
