package com.example.ordernow.Domain;

public class CategoryDomain {
    private String title;
    private String pic;
    private String category;

    public CategoryDomain(String title, String category, String pic) {
        this.title = title;
        this.pic = pic;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public void add(String title, String pic){
        this.title = title;
        this.pic = pic;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String title) {
        this.category = category;
    }
}
