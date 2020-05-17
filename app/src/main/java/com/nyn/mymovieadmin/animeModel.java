package com.nyn.mymovieadmin;

public class animeModel {
    String animeTitle,animeImage,animeVideo,animeSeries,animeType;

    public animeModel() {
    }

    public animeModel(String animeTitle, String animeImage, String animeVideo, String animeSeries, String animeType) {
        this.animeTitle = animeTitle;
        this.animeImage = animeImage;
        this.animeVideo = animeVideo;
        this.animeSeries = animeSeries;
        this.animeType = animeType;
    }

    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }

    public String getAnimeImage() {
        return animeImage;
    }

    public void setAnimeImage(String animeImage) {
        this.animeImage = animeImage;
    }

    public String getAnimeVideo() {
        return animeVideo;
    }

    public void setAnimeVideo(String animeVideo) {
        this.animeVideo = animeVideo;
    }

    public String getAnimeSeries() {
        return animeSeries;
    }

    public void setAnimeSeries(String animeSeries) {
        this.animeSeries = animeSeries;
    }

    public String getAnimeType() {
        return animeType;
    }

    public void setAnimeType(String animeType) {
        this.animeType = animeType;
    }
}
