package com.main.javafxapp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Appointment {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private int appointmentID;
    private int appointmentCustomerID;
    private int appointmentUserID;
    private String appointmentContactName;
    private String appointmentTitle;
    private String appointmentLocation;
    private String appointmentDescription;
    private String appointmentType;
    private String appointmentCreatedBy;
    private String appointmentLastUpdateBy;
    private ZonedDateTime appointmentStartDate;
    private ZonedDateTime appointmentStartTime;
    private ZonedDateTime appointmentEndDate;
    private ZonedDateTime appointmentEndTime;
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

    public ZonedDateTime getAppointmentStartDate() {
        return appointmentStartDate;
    }

    public void setAppointmentStartDate(ZonedDateTime appointmentStartDate) {
        this.appointmentStartDate = appointmentStartDate;
    }

    public ZonedDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(ZonedDateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public ZonedDateTime getAppointmentEndDate() {
        return appointmentEndDate;
    }

    public void setAppointmentEndDate(ZonedDateTime appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }

    public ZonedDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(ZonedDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
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

    public String getAppointmentContactName() {
        return appointmentContactName;
    }

    public void setAppointmentContactName(String appointmentContactName) {
        this.appointmentContactName = appointmentContactName;
    }

    public int getAppointmentUserID() {
        return appointmentUserID;
    }

    public void setAppointmentUserID(int appointmentUserID) {
        this.appointmentUserID = appointmentUserID;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
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

}
