package com.main.javafxapp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Appointment.
 */
public class Appointment {

    /**
     * The All appointments.
     */
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


    /**
     * Gets id.
     *
     * @return the id
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets id.
     *
     * @param ID the id
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets customer id.
     *
     * @param customerID the customer id
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets appointment type.
     *
     * @return the appointment type
     */
    public String getAppointmentType() {
        return type;
    }

    /**
     * Sets appointment type.
     *
     * @param appointmentType the appointment type
     */
    public void setAppointmentType(String appointmentType) {
        this.type = appointmentType;
    }

    /**
     * Gets start date time.
     *
     * @return the start date time
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets start date time.
     *
     * @param startDateTime the start date time
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets end date time.
     *
     * @return the end date time
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets end date time.
     *
     * @param endDateTime the end date time
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets all appointments.
     *
     * @return the all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Sets all appointments.
     *
     * @param allAppointments the all appointments
     */
    public static void setAllAppointments(ObservableList<Appointment> allAppointments) {
        Appointment.allAppointments = allAppointments;
    }

    /**
     * Add appointment.
     *
     * @param newAppointment the new appointment
     */
    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }
}
