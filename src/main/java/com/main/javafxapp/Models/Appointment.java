package com.main.javafxapp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointment {

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private int ID;
    private int customerID;
    private int userID;
    private String contactName;
    private String title;
    private String location;
    private String description;
    private String type;
    private String startDateTime;
    private String endDateTime;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentType() {
        return type;
    }

    public void setAppointmentType(String appointmentType) {
        this.type = appointmentType;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static void setAllAppointments(ObservableList<Appointment> allAppointments) {
        Appointment.allAppointments = allAppointments;
    }

    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
