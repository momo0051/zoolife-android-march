package com.zoolife.app.ResponseModel.AllPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPostResponseModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private DataPagination data = null;
    @SerializedName("error")
    @Expose
    private boolean error;

    /**
     * No args constructor for use in serialization
     */
    public AllPostResponseModel() {
    }

    /**
     * @param data
     * @param error
     * @param status
     */
    public AllPostResponseModel(int status, DataPagination data, boolean error) {
        super();
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }



    public String getNextPage() {
        return data.getNextPageUrl();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataItem> getData() {
        return data.getData();
    }

    public void setData(List<DataItem> data) {
        this.data.setData(data);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}