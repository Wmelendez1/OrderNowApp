package com.example.ordernow.Models;


public class AlertItem {
    private String title;
    private String message;
    private boolean isRead;

    public AlertItem(String title, String message) {
        this.title = title;
        this.message = message;
        this.isRead = false;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }


}
