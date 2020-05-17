package com.nyn.mymovieadmin;

public class categoryModel {
    String categoryName;

    public categoryModel() {
    }

    public categoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
