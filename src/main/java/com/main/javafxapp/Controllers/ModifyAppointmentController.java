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

public class ModifyAppointmentController implements Initializable {
    @FXML
    public ComboBox<Integer> modifyAppointmentUserCB;
    @FXML
    public ComboBox<Integer> modifyAppointmentCustomerCB;
    @FXML
    public ComboBox<String> modifyAppointmentContactCB;
    @FXML
    public Spinner<Integer> modifyAppointmentEndTimeMinutes;
    @FXML
    public Spinner<Integer> modifyAppointmentEndTimeHours;
    @FXML
    public Spinner<Integer> modifyAppointmentStartTimeMinutes;
    @FXML
    public Spinner<Integer> modifyAppointmentStartTimeHours;
    @FXML
    public DatePicker modifyAppointmentEndDate;
    @FXML
    public DatePicker modifyAppointmentStartDate;
    @FXML
    public TextField modifyAppointmentType;
    @FXML
    public TextField modifyAppointmentLocation;
    @FXML
    public TextField modifyAppointmentDescription;
    @FXML
    public TextField modifyAppointmentTitle;
    @FXML
    public TextField modifyAppointmentID;

    ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
    ObservableList<String> contactNameList = FXCollections.observableArrayList();
    ObservableList<Integer> userIDList = FXCollections.observableArrayList();
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

            instant = Utility.instantBuilder(appointmentEndDate, appointmentEndTimeHour, appointmentEndTimeMinute);
            appointment.setEndDateTime(dateFormatter(instant, utcZone));

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

    public void modifyAppointmentCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Appointment Schedule");
    }
}
