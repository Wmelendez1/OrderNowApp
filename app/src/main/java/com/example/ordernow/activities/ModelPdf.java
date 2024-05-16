package com.example.ordernow.activities;

public class ModelPdf {
    //variables
    String uid, id, title, description, url, categoryId;
    long timestamp;

    //empty constructor, required for firebase
    public ModelPdf() {
    }

    //constructor for all params
    public ModelPdf(String uid, String id, String title, String description, String url, String categoryId, long timestamp) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.categoryId = categoryId;
        this.timestamp = timestamp;
    }

    /*-----Getter/Setter-----*/
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
