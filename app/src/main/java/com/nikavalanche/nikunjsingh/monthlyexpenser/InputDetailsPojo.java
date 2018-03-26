package com.nikavalanche.nikunjsingh.monthlyexpenser;

/**
 * Created by nikunjsingh on 3/7/18.
 */


public class InputDetailsPojo {


 private String reason;
 private String money;
 private String dateTime;
 public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InputDetailsPojo() {
    }

    public InputDetailsPojo(String reason, String money, String dateTime) {
        this.reason = reason;
        this.money = money;
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

