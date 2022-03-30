package com.zoolife.app.ResponseModel.Bid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class BidModel {

    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("status")
    private int status;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("total")
        private int total;
        @Expose
        @SerializedName("to")
        private int to;
        @Expose
        @SerializedName("per_page")
        private int per_page;
        @Expose
        @SerializedName("path")
        private String path;
        @Expose
        @SerializedName("last_page_url")
        private String last_page_url;
        @Expose
        @SerializedName("last_page")
        private int last_page;
        @Expose
        @SerializedName("from")
        private int from;
        @Expose
        @SerializedName("first_page_url")
        private String first_page_url;
        @Expose
        @SerializedName("data")
        private List<MData> data;
        @Expose
        @SerializedName("current_page")
        private int current_page;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getLast_page_url() {
            return last_page_url;
        }

        public void setLast_page_url(String last_page_url) {
            this.last_page_url = last_page_url;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public String getFirst_page_url() {
            return first_page_url;
        }

        public void setFirst_page_url(String first_page_url) {
            this.first_page_url = first_page_url;
        }

        public List<MData> getData() {
            return data;
        }

        public void setData(List<MData> data) {
            this.data = data;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }
    }

    public static class MData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("bid_amount")
        @Expose
        private Integer bidAmount;
        @SerializedName("is_winner")
        @Expose
        private Integer isWinner;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("auction_expiry_time")
        @Expose
        private String auctionExpiryTime;
        @SerializedName("readable_time")
        @Expose
        private String readableTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getBidAmount() {
            return bidAmount;
        }

        public void setBidAmount(Integer bidAmount) {
            this.bidAmount = bidAmount;
        }

        public Integer getIsWinner() {
            return isWinner;
        }

        public void setIsWinner(Integer isWinner) {
            this.isWinner = isWinner;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getAuctionExpiryTime() {
            return auctionExpiryTime;
        }

        public void setAuctionExpiryTime(String auctionExpiryTime) {
            this.auctionExpiryTime = auctionExpiryTime;
        }

        public String getReadableTime() {
            return readableTime;
        }

        public void setReadableTime(String readableTime) {
            this.readableTime = readableTime;
        }
    }
}
