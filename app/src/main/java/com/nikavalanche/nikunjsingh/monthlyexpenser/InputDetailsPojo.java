package com.nikavalanche.nikunjsingh.monthlyexpenser;

/**
 * Created by nikunjsingh on 3/7/18.
 */


public class InputDetailsPojo {


 private String reason;
 private String money;

    public InputDetailsPojo() {
    }

    public InputDetailsPojo(String reason, String money) {
        this.reason = reason;
        this.money = money;
    }

    public String getReason() {
        return reason;
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

