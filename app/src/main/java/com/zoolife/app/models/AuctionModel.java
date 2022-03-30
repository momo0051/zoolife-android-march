package com.zoolife.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class AuctionModel {

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
        int viewType=0;

        @Expose
        @SerializedName("remaining_time")
        private String remaining_time;
        @Expose
        @SerializedName("latest_bid")
        private String latest_bid;
        @Expose
        @SerializedName("expiry_days")
        private int expiry_days;
        @Expose
        @SerializedName("expiry_hours")
        private int expiry_hours;
        @Expose
        @SerializedName("max_bid")
        private int max_bid;
        @Expose
        @SerializedName("min_bid")
        private int min_bid;
        @Expose
        @SerializedName("auction_expiry_time")
        private String auction_expiry_time;
        @Expose
        @SerializedName("post_type")
        private String post_type;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("removeAt")
        private int removeAt;
        @Expose
        @SerializedName("modifyAt")
        private int modifyAt;
        @Expose
        @SerializedName("phoneViewsCount")
        private int phoneViewsCount;
        @Expose
        @SerializedName("showWhatsapp")
        private int showWhatsapp;
        @Expose
        @SerializedName("showComments")
        private int showComments;
        @Expose
        @SerializedName("showMessage")
        private int showMessage;
        @Expose
        @SerializedName("showPhoneNumber")
        private int showPhoneNumber;
        @Expose
        @SerializedName("passport")
        private String passport;
        @Expose
        @SerializedName("vaccine_detail")
        private String vaccine_detail;
        @Expose
        @SerializedName("sex")
        private String sex;
        @Expose
        @SerializedName("age")
        private String age;
        @Expose
        @SerializedName("city")
        private String city;
        @Expose
        @SerializedName("country")
        private String country;
        @Expose
        @SerializedName("area")
        private String area;
        @Expose
        @SerializedName("videoUrl")
        private String videoUrl;
        @Expose
        @SerializedName("imgUrl")
        private String imgUrl;
        @Expose
        @SerializedName("itemDesc")
        private String itemDesc;
        @Expose
        @SerializedName("itemTitle")
        private String itemTitle;
        @Expose
        @SerializedName("likesCount")
        private int likesCount;
        @Expose
        @SerializedName("subCategory")
        private int subCategory;
        @Expose
        @SerializedName("category")
        private int category;
        @Expose
        @SerializedName("fromUserId")
        private int fromUserId;
        @Expose
        @SerializedName("allowComments")
        private int allowComments;
        @Expose
        @SerializedName("priority")
        private String priority;
        @Expose
        @SerializedName("id")
        private int id;

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public String getRemaining_time() {
            return remaining_time;
        }

        public void setRemaining_time(String remaining_time) {
            this.remaining_time = remaining_time;
        }

        public String getLatest_bid() {
            return latest_bid;
        }

        public void setLatest_bid(String latest_bid) {
            this.latest_bid = latest_bid;
        }

        public int getExpiry_days() {
            return expiry_days;
        }

        public void setExpiry_days(int expiry_days) {
            this.expiry_days = expiry_days;
        }

        public int getExpiry_hours() {
            return expiry_hours;
        }

        public void setExpiry_hours(int expiry_hours) {
            this.expiry_hours = expiry_hours;
        }

        public int getMax_bid() {
            return max_bid;
        }

        public void setMax_bid(int max_bid) {
            this.max_bid = max_bid;
        }

        public int getMin_bid() {
            return min_bid;
        }

        public void setMin_bid(int min_bid) {
            this.min_bid = min_bid;
        }

        public String getAuction_expiry_time() {
            return auction_expiry_time;
        }

        public void setAuction_expiry_time(String auction_expiry_time) {
            this.auction_expiry_time = auction_expiry_time;
        }

        public String getPost_type() {
            return post_type;
        }

        public void setPost_type(String post_type) {
            this.post_type = post_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getRemoveAt() {
            return removeAt;
        }

        public void setRemoveAt(int removeAt) {
            this.removeAt = removeAt;
        }

        public int getModifyAt() {
            return modifyAt;
        }

        public void setModifyAt(int modifyAt) {
            this.modifyAt = modifyAt;
        }

        public int getPhoneViewsCount() {
            return phoneViewsCount;
        }

        public void setPhoneViewsCount(int phoneViewsCount) {
            this.phoneViewsCount = phoneViewsCount;
        }

        public int getShowWhatsapp() {
            return showWhatsapp;
        }

        public void setShowWhatsapp(int showWhatsapp) {
            this.showWhatsapp = showWhatsapp;
        }

        public int getShowComments() {
            return showComments;
        }

        public void setShowComments(int showComments) {
            this.showComments = showComments;
        }

        public int getShowMessage() {
            return showMessage;
        }

        public void setShowMessage(int showMessage) {
            this.showMessage = showMessage;
        }

        public int getShowPhoneNumber() {
            return showPhoneNumber;
        }

        public void setShowPhoneNumber(int showPhoneNumber) {
            this.showPhoneNumber = showPhoneNumber;
        }

        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        public String getVaccine_detail() {
            return vaccine_detail;
        }

        public void setVaccine_detail(String vaccine_detail) {
            this.vaccine_detail = vaccine_detail;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public int getLikesCount() {
            return likesCount;
        }

        public void setLikesCount(int likesCount) {
            this.likesCount = likesCount;
        }

        public int getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(int subCategory) {
            this.subCategory = subCategory;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(int fromUserId) {
            this.fromUserId = fromUserId;
        }

        public int getAllowComments() {
            return allowComments;
        }

        public void setAllowComments(int allowComments) {
            this.allowComments = allowComments;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
