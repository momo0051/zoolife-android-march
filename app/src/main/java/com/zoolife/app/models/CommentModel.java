package com.zoolife.app.models;

public class CommentModel {

    public String title, id, username, co;
    public String bid="";

    public CommentModel(String title, String id, String username, String co) {
        this.title = title;
        this.id = id;
        this.username = username;
        this.co = co;
    }

    public CommentModel(String title, String id, String username, String co, String bid) {
        this.title = title;
        this.id = id;
        this.username = username;
        this.co = co;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", co='" + co + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }
}
