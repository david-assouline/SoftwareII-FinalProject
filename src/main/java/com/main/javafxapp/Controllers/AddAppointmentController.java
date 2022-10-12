package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Toolkit.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static com.main.javafxapp.Controllers.LoginController.utcZone;
import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

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

    public Map<String, Integer> contactMap = new HashMap<String, Integer>();

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
                contactMap.put(resultSet.getString("Contact_Name"), resultSet.getInt("Contact_ID"));
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
            int id = rand.nextInt(1000);
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

            appointment.setID(id);
            appointment.setTitle(appointmentTitle);
            appointment.setDescription(appointmentDescription);
            appointment.setLocation(appointmentLocation);
            appointment.setContactName(appointmentContact);
            appointment.setAppointmentType(appointmentType);
            appointment.setCustomerID(appointmentCustomerID);
            appointment.setUserID(appointmentUserID);

            Instant startInstant = Utility.instantBuilder(appointmentStartDate, appointmentStartTimeHour, appointmentStartTimeMinute);
            appointment.setStartDateTime(dateFormatter(startInstant, utcZone));

            if (startInstant.atZone(utcZone).getHour() >= 2 && startInstant.atZone(utcZone).getHour() < 12) {
                errorAlert("Invalid start time", "Start time is outside of business hours!");
                return;
            }

            Instant endInstant = Utility.instantBuilder(appointmentEndDate, appointmentEndTimeHour, appointmentEndTimeMinute);
            appointment.setEndDateTime(dateFormatter(endInstant, utcZone));

            if (endInstant.atZone(utcZone).getHour() == 2 && endInstant.atZone(utcZone).getMinute() > 0 ) {
                errorAlert("Invalid start time", "End time is outside of business hours!");
                return;
            } else if (endInstant.atZone(utcZone).getHour() > 2 && endInstant.atZone(utcZone).getHour() < 12) {
                errorAlert("Invalid start time", "End time is outside of business hours!");
                return;
            }

            if (hasOverlappingAppointment(startInstant, endInstant, appointmentCustomerID)) {
                errorAlert("Appointment Conflict","The appointment you are attempting to create overlaps with an" +
                        " existing appointment!");
                return;
            }

            String query = String.format("""
                    INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By,  Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)
                    VALUES ("%1$d", "%2$s", "%3$s", "%4$s", "%5$s", "%6$s", "%7$s", NULL, NULL, NULL, NULL, "%8$d", "%9$d", "%10$d");
                    """,
            appointment.getID(), appointment.getTitle(), appointment.getDescription(), appointment.getLocation(),
            appointment.getAppointmentType(), appointment.getStartDateTime(), appointment.getEndDateTime(),
            appointment.getCustomerID(), appointment.getUserID(), contactMap.get(appointment.getContactName()));

            Statement stmt = connection.createStatement();
            int response = stmt.executeUpdate(query);

            if (response == 1) {
                informationAlert("Confirmation", "Successfully added","Appointment has been saved to the database");
                Utility.closeWindow(actionEvent);
                Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Appointment Schedule");
            } else {
                errorAlert("Error","Could not save appointment to the database");
                return;
            }

        } catch (Exception e) {
            Utility.errorAlert("Error","Invalid Data");
            return;
        }
    }

    public void addAppointmentCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Appointment Schedule");
    }
}
