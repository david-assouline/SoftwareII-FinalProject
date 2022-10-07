package com.main.javafxapp.Models;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Appointment {
    
    private int appointmentID;
    private int appointmentCustomerID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentType;
    private String appointmentCustomer;
    private String appointmentCreatedBy;
    private String appointmentLastUpdateBy;

    private ZonedDateTime appointmentStartDateTime;
    private ZonedDateTime appointmentEndDateTime;
    private LocalDateTime appointmentCreateDate;
    private Timestamp appointmentLastModified;

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID = appointmentCustomerID;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentCustomer() {
        return appointmentCustomer;
    }

    public void setAppointmentCustomer(String appointmentCustomer) {
        this.appointmentCustomer = appointmentCustomer;
    }

    public String getAppointmentCreatedBy() {
        return appointmentCreatedBy;
    }

    public void setAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
    }

    public String getAppointmentLastUpdateBy() {
        return appointmentLastUpdateBy;
    }

    public void setAppointmentLastUpdateBy(String appointmentLastUpdateBy) {
        this.appointmentLastUpdateBy = appointmentLastUpdateBy;
    }

    public ZonedDateTime getAppointmentStartDateTime() {
        return appointmentStartDateTime;
    }

    public void setAppointmentStartDateTime(ZonedDateTime appointmentStartDate) {
        this.appointmentStartDateTime = appointmentStartDate;
    }

    public ZonedDateTime getAppointmentEndDateTime() {
        return appointmentEndDateTime;
    }

    public void setAppointmentEndDateTime(ZonedDateTime appointmentEndDate) {
        this.appointmentEndDateTime = appointmentEndDate;
    }


    public LocalDateTime getAppointmentCreateDate() {
        return appointmentCreateDate;
    }

    public void setAppointmentCreateDate(LocalDateTime appointmentCreateDate) {
        this.appointmentCreateDate = appointmentCreateDate;
    }

    public Timestamp getAppointmentLastModified() {
        return appointmentLastModified;
    }

    public void setAppointmentLastModified(Timestamp appointmentLastModified) {
        this.appointmentLastModified = appointmentLastModified;
    }
}
