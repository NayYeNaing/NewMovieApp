package com.nyn.mymovieadmin;

public class movieModel {
    String movieTitle,movieImage,movieVideo,movieCategory,movieType;

    public movieModel() {
    }

    public movieModel(String movieTitle, String movieImage, String movieVideo, String movieCategory, String movieType) {
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.movieVideo = movieVideo;
        this.movieCategory = movieCategory;
        this.movieType = movieType;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieVideo() {
        return movieVideo;
    }

    public void setMovieVideo(String movieVideo) {
        this.movieVideo = movieVideo;
    }

    public String getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(String movieCategory) {
        this.movieCategory = movieCategory;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }
}
