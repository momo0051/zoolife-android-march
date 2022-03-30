package com.zoolife.app.cstmslider;

public class DtoImageVideo {
    private boolean isImage;
    private String uri;

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
