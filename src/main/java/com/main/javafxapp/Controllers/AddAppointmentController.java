package com.main.javafxapp.Controllers;

import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Toolkit.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
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
    public ComboBox<String> addAppointmentContactCB = new ComboBox<String>();
    @FXML
    public ComboBox<Integer> addAppointmentCustomerCB = new ComboBox<Integer>();

    @FXML
    public ComboBox<Integer> addAppointmentUserCB = new ComboBox<Integer>();

    public ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
    public ObservableList<String> contactNameList = FXCollections.observableArrayList();
    public ObservableList<Integer> userIDList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            ResultSet resultSet = Utility.getCustomers();
            while (resultSet.next()) {
                customerIDList.add(Integer.parseInt(resultSet.getString("Customer_ID")));
            }
            addAppointmentCustomerCB.setItems(customerIDList);

            resultSet = Utility.getContacts();
            while (resultSet.next()) {
                contactNameList.add(resultSet.getString("Contact_Name"));
            }
            addAppointmentContactCB.setItems(contactNameList);

            resultSet = Utility.getUsers();
            while (resultSet.next()) {
                userIDList.add(Integer.parseInt(resultSet.getString("User_ID")));
            }
            addAppointmentUserCB.setItems(userIDList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAppointmentSaveButtonClicked(ActionEvent actionEvent) {
        Random rand = new Random();
        try {
            int id = rand.nextInt(1000000);
            String appointmentTitle = addAppointmentTitle.getText();
            String appointmentDescription = addAppointmentDescription.getText();
            String appointmentLocation = addAppointmentLocation.getText();
            String appointmentContact = addAppointmentContactCB.getValue();
            String appointmentType = addAppointmentType.getText();
            LocalDate appointmentStartDate = addAppointmentStartDate.getValue();
            LocalDate appointmentEndDate = addAppointmentEndDate.getValue();
            int appointmentStartTimeHour = addAppointmentStartTimeHours.getValue();
            int appointmentEndTimeHour = addAppointmentEndTimeHours.getValue();
            int appointmentStartTimeMinute = addAppointmentStartTimeMinutes.getValue();
            int appointmentEndTimeMinute = addAppointmentEndTimeMinutes.getValue();
            int appointmentCustomerID = addAppointmentCustomerCB.getValue();
            int appointmentUserID = addAppointmentUserCB.getValue();

            Appointment appointment = new Appointment();
            appointment.setAppointmentID(id);
            appointment.setAppointmentTitle(appointmentTitle);
            appointment.setAppointmentDescription(appointmentDescription);
            appointment.setAppointmentLocation(appointmentLocation);
            appointment.setAppointmentContactName(appointmentContact);
            appointment.setAppointmentType(appointmentType);
            appointment.setAppointmentCustomerID(appointmentCustomerID);
            appointment.setAppointmentUserID(appointmentUserID);

            Appointment.addAppointment(appointment);


        } catch (Exception e) {
            Utility.errorAlert("","Invalid Data");
            return;
        }
    }

    public void addAppointmentCancelButtonClicked(ActionEvent actionEvent) {
    }
}
