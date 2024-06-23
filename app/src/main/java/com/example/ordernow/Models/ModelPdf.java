package com.example.ordernow.Models;

public class ModelPdf {
    // Variables
    private String uid;
    private String id;
    private String firstName;
    private String lastName;
    private String url;


    private String Age;
    private long timestamp;

    // Empty constructor, required for Firebase
    public ModelPdf() {}

    // Constructor for all parameters
    public ModelPdf(String uid, String id, String firstName, String lastName, String url, long timestamp, String Age) {
        this.uid = uid;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.url = url;
        this.timestamp = timestamp;
        this.Age = Age;
    }

    // Getter and setter methods
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

}
