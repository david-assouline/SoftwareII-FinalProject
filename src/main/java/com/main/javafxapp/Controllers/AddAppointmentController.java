package com.main.javafxapp.Controllers;

import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Random;

public class AddAppointmentController {
    @FXML
    public TextField addAppointmentTitle;
    @FXML
    public TextField addAppointmentDescription;
    @FXML
    public TextField addAppointmentLocation;
    @FXML
    public TextField addAppointmentType;
    @FXML
    public DatePicker addAppointmentStartDate;
    @FXML
    public DatePicker addAppointmentEndDate;
    @FXML
    public Spinner<Integer> addAppointmentStartTimeHours;
    @FXML
    public Spinner<Integer> addAppointmentStartTimeMinutes;
    @FXML
    public Spinner<Integer> addAppointmentEndTimeHours;
    @FXML
    public Spinner<Integer> addAppointmentEndTimeMinutes;
    @FXML
    public ComboBox addAppointmentContactCB;
    @FXML
    public ComboBox addAppointmentCustomerCB;
    @FXML
    public ComboBox addAppointmentUserCB;

    public void addAppointmentSaveButtonClicked(ActionEvent actionEvent) {
        Random rand = new Random();
        try {
            int id = rand.nextInt(1000000);
            String appointmentTitle = addAppointmentTitle.getText();
            String appointmentDescription = addAppointmentDescription.getText();
            String appointmentLocation = addAppointmentLocation.getText();
            String appointmentType = addAppointmentType.getText();
            LocalDate appointmentStartDate = addAppointmentStartDate.getValue();
            LocalDate appointmentEndDate = addAppointmentEndDate.getValue();
            int appointmentStartTimeHour = addAppointmentStartTimeHours.getValue();
            int appointmentEndTimeHour = addAppointmentEndTimeHours.getValue();
            int appointmentStartTimeMinute = addAppointmentStartTimeMinutes.getValue();
            int appointmentEndTimeMinute = addAppointmentEndTimeMinutes.getValue();

        } catch (Exception e) {
            Utility.errorAlert("","Invalid Data");
            return;
        }
    }

    public void addAppointmentCancelButtonClicked(ActionEvent actionEvent) {
    }
}
