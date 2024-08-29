package Models;

import java.io.Serializable;

public class Card implements Serializable {
    private String cardNumber;
    private boolean isDefault;
    private String cardHolderName;
    private String expiryDate;

    // Constructor
    public Card(String cardNumber, boolean isDefault, String cardHolderName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.isDefault = isDefault;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}

