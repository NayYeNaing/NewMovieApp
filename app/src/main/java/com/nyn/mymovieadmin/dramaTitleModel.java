package com.nyn.mymovieadmin;

public class dramaTitleModel {
    String dramaTitle,dramaImage,dramaCategory;

    public dramaTitleModel() {
    }

    public dramaTitleModel(String dramaTitle, String dramaImage, String dramaCategory) {
        this.dramaTitle = dramaTitle;
        this.dramaImage = dramaImage;
        this.dramaCategory = dramaCategory;
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

    public String getDramaCategory() {
        return dramaCategory;
    }

    public void setDramaCategory(String dramaCategory) {
        this.dramaCategory = dramaCategory;
    }
}
