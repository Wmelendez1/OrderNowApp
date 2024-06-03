package com.example.ordernow.activities;

public class ModelPdf {
    // Variables
    private String uid;
    private String id;
    private String firstName;
    private String lastName;
    private String url;
    private String username;

    private String Bio;


    private String Age;
    private long timestamp;

    // Empty constructor, required for Firebase
    public ModelPdf() {}


    // Constructor for all parameters
    public ModelPdf(String uid, String id, String firstName, String lastName, String url, long timestamp, String Age, String email) {
        this.uid = uid;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.url = url;
        this.timestamp = timestamp;
        this.Age = Age;
        this.username = username;
        this.Bio = Bio;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

}
