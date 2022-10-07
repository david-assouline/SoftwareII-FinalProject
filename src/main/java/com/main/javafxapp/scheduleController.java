package com.main.javafxapp;

import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class scheduleController implements Initializable {

    @FXML
    public TableColumn apptIDColumn;
    @FXML
    public TableColumn apptTitleColumn;
    @FXML
    public TableColumn apptDescriptionColumn;
    @FXML
    public TableColumn apptLocationColumn;
    @FXML
    public TableColumn apptContactColumn;
    @FXML
    public TableColumn apptTypeColumn;
    @FXML
    public TableColumn apptStartDateColumn;
    @FXML
    public TableColumn apptEndDateColumn;
    @FXML
    public TableColumn apptStartTimeColumn;
    @FXML
    public TableColumn apptEndTimeColumn;
    @FXML
    public TableColumn apptCustomerID;
    @FXML
    public TableColumn apptUserIDColumn;
    public RadioButton viewByWeekRadio;
    public ToggleGroup viewSelector;
    public RadioButton viewByMonthRadio;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        populateAllAppointmentTable();
//        populateCustomerTable();


        Appointment nextAppointment = Utility.appointmentWithinFifteen();
        if(nextAppointment != null){
            String message = String.format("Your appointment (ID: %1$s) is scheduled for %2$s",
                    nextAppointment.getAppointmentID(),
                    nextAppointment.getAppointmentStartDateTime());

            Utility.informationAlert(
                    "Upcoming Appointment",
                    "You have an appointment in the next 15 minutes",
                    message);
        } else {
            Utility.informationAlert(
                    "Upcoming Appointment",
                    "You have no upcoming appointments in the next 15 minutes",
                    "");
        }
    }

    public void addButtonClicked(ActionEvent actionEvent) {
    }

    public void modifyButtonClicked(ActionEvent actionEvent) {
    }

    public void deleteButtonClicked(ActionEvent actionEvent) {
    }
}

