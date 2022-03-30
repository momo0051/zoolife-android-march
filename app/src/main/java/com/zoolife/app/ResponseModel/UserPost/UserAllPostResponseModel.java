package com.zoolife.app.ResponseModel.UserPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserAllPostResponseModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private DeliveryPagination data = null;

    @SerializedName("error")
    @Expose
    private boolean error;

    /**
     * No args constructor for use in serialization
     */
    public UserAllPostResponseModel() {
    }

    /**
     * @param data
     * @param error
     * @param status
     */
    public UserAllPostResponseModel(int status, DeliveryPagination data, boolean error) {
        super();
        this.status = status;
//        this.data = data;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataItemNew> getData() {
        return data.getData();
    }
    public String getNextPageUrl()
    {
        return data.getNextPageUrl();
    }
//    public List<DataItem> getData() {
//        return new ArrayList<>();
//    }

    public void setData(List<DataItemNew> data) {
        this.data.setData(data);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


}