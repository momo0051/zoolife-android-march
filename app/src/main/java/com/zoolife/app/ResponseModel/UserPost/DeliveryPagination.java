package com.zoolife.app.ResponseModel.UserPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class DeliveryPagination {

    @SerializedName("current_page")
    @Expose
    private int current_page;


    @SerializedName("first_page_url")
    @Expose
    private String first_page_url;



    @SerializedName("from")
    @Expose
    private int from;



    @SerializedName("last_page")
    @Expose
    private int last_page;



    @SerializedName("last_page_url")
    @Expose
    private String last_page_url;

    @SerializedName("next_page_url")
    @Expose
    private String next_page_url;



    @SerializedName("path")
    @Expose
    private String path;



    @SerializedName("per_page")
    @Expose
    private int per_page;



    @SerializedName("prev_page_url")
    @Expose
    private String prev_page_url;



    @SerializedName("to")
    @Expose
    private int to;



    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("data")
    @Expose
    private List<DataItemNew> data = null;

    /**
     * No args constructor for use in serialization
     */
    public DeliveryPagination() {
    }

    /**
     * @param data
     * @param error
     * @param status
     */
    public DeliveryPagination(int status, List<DataItemNew> data, boolean error) {
        super();
//        this.status = status;
        this.data = data;
    }

    public int getCurrentPage() {
        return current_page;
    }

    public String getNextPageUrl() {
        return next_page_url;
    }

//    public void setStatus(int status) {
//        this.status = status;
//    }

    public List<DataItemNew> getData() {
        return data;
    }

    public void setData(List<DataItemNew> data) {
        this.data = data;
    }

}