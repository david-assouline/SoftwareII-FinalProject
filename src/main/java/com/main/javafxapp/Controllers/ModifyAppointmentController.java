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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import static com.main.javafxapp.Controllers.LoginController.utcZone;
import static com.main.javafxapp.Controllers.ScheduleController.selectedAppointment;
import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

/**
 * The type Modify appointment controller.
 */
public class ModifyAppointmentController implements Initializable {
    /**
     * The Modify appointment user cb.
     */
    @FXML
    public ComboBox<Integer> modifyAppointmentUserCB;
    /**
     * The Modify appointment customer cb.
     */
    @FXML
    public ComboBox<Integer> modifyAppointmentCustomerCB;
    /**
     * The Modify appointment contact cb.
     */
    @FXML
    public ComboBox<String> modifyAppointmentContactCB;
    /**
     * The Modify appointment end time minutes.
     */
    @FXML
    public Spinner<Integer> modifyAppointmentEndTimeMinutes;
    /**
     * The Modify appointment end time hours.
     */
    @FXML
    public Spinner<Integer> modifyAppointmentEndTimeHours;
    /**
     * The Modify appointment start time minutes.
     */
    @FXML
    public Spinner<Integer> modifyAppointmentStartTimeMinutes;
    /**
     * The Modify appointment start time hours.
     */
    @FXML
    public Spinner<Integer> modifyAppointmentStartTimeHours;
    /**
     * The Modify appointment end date.
     */
    @FXML
    public DatePicker modifyAppointmentEndDate;
    /**
     * The Modify appointment start date.
     */
    @FXML
    public DatePicker modifyAppointmentStartDate;
    /**
     * The Modify appointment type.
     */
    @FXML
    public TextField modifyAppointmentType;
    /**
     * The Modify appointment location.
     */
    @FXML
    public TextField modifyAppointmentLocation;
    /**
     * The Modify appointment description.
     */
    @FXML
    public TextField modifyAppointmentDescription;
    /**
     * The Modify appointment title.
     */
    @FXML
    public TextField modifyAppointmentTitle;
    /**
     * The Modify appointment id.
     */
    @FXML
    public TextField modifyAppointmentID;

    /**
     * The Customer id list.
     */
    ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
    /**
     * The Contact name list.
     */
    ObservableList<String> contactNameList = FXCollections.observableArrayList();
    /**
     * The User id list.
     */
    ObservableList<Integer> userIDList = FXCollections.observableArrayList();
    /**
     * The Contact map.
     */
    public Map<String, Integer> contactMap = new HashMap<String, Integer>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modifyAppointmentID.setText(Integer.toString(selectedAppointment.getID()));
        modifyAppointmentTitle.setText(selectedAppointment.getTitle());
        modifyAppointmentDescription.setText(selectedAppointment.getDescription());
        modifyAppointmentLocation.setText(selectedAppointment.getLocation());
        modifyAppointmentDescription.setText(selectedAppointment.getDescription());
        modifyAppointmentType.setText(selectedAppointment.getType());
        modifyAppointmentUserCB.setValue(selectedAppointment.getUserID());
        modifyAppointmentCustomerCB.setValue(selectedAppointment.getCustomerID());
        modifyAppointmentContactCB.setValue(selectedAppointment.getContactName());

        String timeStamp = selectedAppointment.getStartDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.from(formatter.parse(timeStamp));
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(timeStamp));
        modifyAppointmentStartDate.setValue(localDate);
        modifyAppointmentStartTimeHours.getValueFactory().setValue(localDateTime.getHour());
        modifyAppointmentStartTimeMinutes.getValueFactory().setValue(localDateTime.getMinute());

        timeStamp = selectedAppointment.getEndDateTime();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        localDate = LocalDate.from(formatter.parse(timeStamp));
        localDateTime = LocalDateTime.from(formatter.parse(timeStamp));
        modifyAppointmentEndDate.setValue(localDate);
        modifyAppointmentEndTimeHours.getValueFactory().setValue(localDateTime.getHour());
        modifyAppointmentEndTimeMinutes.getValueFactory().setValue(localDateTime.getMinute());

        try {
            ResultSet resultSet = Utility.getCustomers();
            while (resultSet.next()) {
                customerIDList.add(Integer.parseInt(resultSet.getString("Customer_ID")));
            }
            modifyAppointmentCustomerCB.setItems(customerIDList);

            resultSet = Utility.getContacts();
            while (resultSet.next()) {
                contactNameList.add(resultSet.getString("Contact_Name"));
                contactMap.put(resultSet.getString("Contact_Name"), resultSet.getInt("Contact_ID"));
            }
            modifyAppointmentContactCB.setItems(contactNameList);

            resultSet = Utility.getUsers();
            while (resultSet.next()) {
                userIDList.add(Integer.parseInt(resultSet.getString("User_ID")));
            }
            modifyAppointmentUserCB.setItems(userIDList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Modify appointment save button clicked.
     *
     * @param actionEvent the action event
     */
    public void modifyAppointmentSaveButtonClicked(ActionEvent actionEvent) {
        try {
            int appointmentID = Integer.parseInt(modifyAppointmentID.getText());
            String appointmentTitle = modifyAppointmentTitle.getText();
            String appointmentDescription = modifyAppointmentDescription.getText();
            String appointmentLocation = modifyAppointmentLocation.getText();
            String appointmentContact = modifyAppointmentContactCB.getValue();
            String appointmentType = modifyAppointmentType.getText();
            LocalDate appointmentStartDate = modifyAppointmentStartDate.getValue();
            LocalDate appointmentEndDate = modifyAppointmentEndDate.getValue();
            int appointmentStartTimeHour = modifyAppointmentStartTimeHours.getValue();
            int appointmentEndTimeHour = modifyAppointmentEndTimeHours.getValue();
            int appointmentStartTimeMinute = modifyAppointmentStartTimeMinutes.getValue();
            int appointmentEndTimeMinute = modifyAppointmentEndTimeMinutes.getValue();
            int appointmentCustomerID = modifyAppointmentCustomerCB.getValue();
            int appointmentUserID = modifyAppointmentUserCB.getValue();

            Appointment appointment = new Appointment();

            appointment.setID(appointmentID);
            appointment.setTitle(appointmentTitle);
            appointment.setDescription(appointmentDescription);
            appointment.setLocation(appointmentLocation);
            appointment.setContactName(appointmentContact);
            appointment.setAppointmentType(appointmentType);
            appointment.setCustomerID(appointmentCustomerID);
            appointment.setUserID(appointmentUserID);

            Instant instant = Utility.instantBuilder(appointmentStartDate, appointmentStartTimeHour, appointmentStartTimeMinute);
            appointment.setStartDateTime(dateFormatter(instant, utcZone));

            if (instant.atZone(utcZone).getHour() >= 2 && instant.atZone(utcZone).getHour() < 12) {
                errorAlert("Invalid start time", "Start time is outside of business hours!");
                return;
            }

            instant = Utility.instantBuilder(appointmentEndDate, appointmentEndTimeHour, appointmentEndTimeMinute);
            appointment.setEndDateTime(dateFormatter(instant, utcZone));

            if (instant.atZone(utcZone).getHour() == 2 && instant.atZone(utcZone).getMinute() > 0 ) {
                errorAlert("Invalid start time", "End time is outside of business hours!");
                return;
            } else if (instant.atZone(utcZone).getHour() > 2 && instant.atZone(utcZone).getHour() < 12) {
                errorAlert("Invalid start time", "End time is outside of business hours!");
                return;
            }

            String query = String.format("DELETE FROM appointments WHERE Appointment_ID = %1$d",appointment.getID());

            Statement stmt = connection.createStatement();
            int deleteResponse = stmt.executeUpdate(query);

            if (deleteResponse != 1) {
                errorAlert("Error","Error modifying appointment");
                return;
            }

            query = String.format("""
                    INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By,  Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)
                    VALUES ("%1$d", "%2$s", "%3$s", "%4$s", "%5$s", "%6$s", "%7$s", NULL, NULL, NULL, NULL, "%8$d", "%9$d", "%10$d");
                    """,
                    appointment.getID(), appointment.getTitle(), appointment.getDescription(), appointment.getLocation(),
                    appointment.getAppointmentType(), appointment.getStartDateTime(), appointment.getEndDateTime(),
                    appointment.getCustomerID(), appointment.getUserID(), contactMap.get(appointment.getContactName()));

            int insertResponse = stmt.executeUpdate(query);

            if (insertResponse == 1) {
                informationAlert("Confirmation", "Successfully Modified","Appointment has been modified and saved to the database");
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

    /**
     * Modify appointment cancel button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void modifyAppointmentCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Appointment Schedule");
    }
}
