package com.main.javafxapp.Models;

import java.time.ZoneId;

public class User {
    private String userID;
    private String userName;
    private String password;
    private ZoneId zoneID;

    public User() {
        this.userID = null;
        this.userName = null;
        this.password = null;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZoneId getZoneID() {
        return zoneID;
    }

    public void setZoneID(ZoneId zoneID) {
        this.zoneID = zoneID;
    }
}
