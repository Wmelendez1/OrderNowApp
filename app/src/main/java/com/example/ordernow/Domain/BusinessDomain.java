package com.example.ordernow.Domain;

import java.util.HashMap;
import java.util.Map;

public class BusinessDomain {

    private String businessPic;
    private String businessName;
    private String businessAddress;
    private String contactNumber;

    public BusinessDomain() {
    }

    public BusinessDomain(String businessPic, String businessName, String businessAddress, String contactNumber) {
        this.businessPic = businessPic;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.contactNumber = contactNumber;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("businessPic", businessPic);
        result.put("businessName", businessName);
        result.put("businessAddress", businessAddress);
        result.put("contactNumber", contactNumber);
        return result;
    }
}
