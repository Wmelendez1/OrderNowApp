package com.example.ordernow.Domain;

public class FoodNearYouDomain {
    private String logo;
    private String name;
    private double numberReviews;
    private String distance;
    private String deliveryFee;
    private String time;

    public FoodNearYouDomain(String logo, String name, double numberReviews, String distance, String deliveryFee, String time) {
        this.logo = logo;
        this.name = name;
        this.numberReviews = numberReviews;
        this.distance = distance;
        this.deliveryFee = deliveryFee;
        this.time = time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNumberReviews() {
        return numberReviews;
    }

    public void setNumberReviews(double numberReviews) {
        this.numberReviews = numberReviews;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}