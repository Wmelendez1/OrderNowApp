package com.example.ordernow.Models;


public class AlertItem {
    private String message;
    private boolean isRead;

    public AlertItem(String message) {
        this.message = message;
        this.isRead = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead(){return isRead; }

    public void setRead(boolean read){isRead = read; }


}
