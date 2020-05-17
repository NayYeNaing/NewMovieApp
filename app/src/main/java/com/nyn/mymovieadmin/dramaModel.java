package com.nyn.mymovieadmin;

public class dramaModel {
    String dramaTitle, dramaImage, dramaVideo, dramaSeriesTitle,dramaType;
    int favCount, viewCount;

    public dramaModel() {
    }

    public dramaModel(String dramaTitle, String dramaImage, String dramaVideo, String dramaSeriesTitle, String dramaType, int favCount, int viewCount) {
        this.dramaTitle = dramaTitle;
        this.dramaImage = dramaImage;
        this.dramaVideo = dramaVideo;
        this.dramaSeriesTitle = dramaSeriesTitle;
        this.dramaType = dramaType;
        this.favCount = favCount;
        this.viewCount = viewCount;
    }

    public String getDramaTitle() {
        return dramaTitle;
    }

    public void setDramaTitle(String dramaTitle) {
        this.dramaTitle = dramaTitle;
    }

    public String getDramaImage() {
        return dramaImage;
    }

    public void setDramaImage(String dramaImage) {
        this.dramaImage = dramaImage;
    }

    public String getDramaVideo() {
        return dramaVideo;
    }

    public void setDramaVideo(String dramaVideo) {
        this.dramaVideo = dramaVideo;
    }

    public String getDramaSeriesTitle() {
        return dramaSeriesTitle;
    }

    public void setDramaSeriesTitle(String dramaSeriesTitle) {
        this.dramaSeriesTitle = dramaSeriesTitle;
    }

    public String getDramaType() {
        return dramaType;
    }

    public void setDramaType(String dramaType) {
        this.dramaType = dramaType;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
