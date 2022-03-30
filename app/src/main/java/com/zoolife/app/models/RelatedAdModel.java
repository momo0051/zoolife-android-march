package com.zoolife.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedAdModel implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("itemTitle")
    @Expose
    private String itemTitle;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    public final static Parcelable.Creator<RelatedAdModel> CREATOR = new Creator<RelatedAdModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RelatedAdModel createFromParcel(Parcel in) {
            return new RelatedAdModel(in);
        }

        public RelatedAdModel[] newArray(int size) {
            return (new RelatedAdModel[size]);
        }

    }
            ;

    protected RelatedAdModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.imgUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.itemTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RelatedAdModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(imgUrl);
        dest.writeValue(city);
        dest.writeValue(itemTitle);
        dest.writeValue(created_at);
    }

    public int describeContents() {
        return 0;
    }

}
