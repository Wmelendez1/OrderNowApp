package com.example.ordernow.Domain;

public class FoodListDomain {
    private String foodname;
    private String pic;
    private String description;
    private Double price;
    private int numberInCart;

    public FoodListDomain(String foodname, String pic, String description, Double price) {
        this.foodname = foodname;
        this.pic = pic;
        this.description = description;
        this.price = price;
    }

    public FoodListDomain(String foodname, String pic, String description, Double price, int numberInCart) {
        this.foodname = foodname;
        this.pic = pic;
        this.description = description;
        this.price = price;
        this.numberInCart = numberInCart;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
