package com.main.javafxapp.Models;

import java.time.ZoneId;

/**
 * The type User.
 */
public class User {
    private String userID;
    private String userName;
    private String password;
    private ZoneId zoneID;

    /**
     * Instantiates a new User.
     */
    public User() {
        this.userID = null;
        this.userName = null;
        this.password = null;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets zone id.
     *
     * @return the zone id
     */
    public ZoneId getZoneID() {
        return zoneID;
    }

    /**
     * Sets zone id.
     *
     * @param zoneID the zone id
     */
    public void setZoneID(ZoneId zoneID) {
        this.zoneID = zoneID;
    }
}
