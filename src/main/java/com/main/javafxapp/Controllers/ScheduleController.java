package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

public class ScheduleController implements Initializable{

    @FXML
    public TableColumn<Appointment, Integer> apptIDColumn;
    @FXML
    public TableColumn<Appointment, String> apptTitleColumn;
    @FXML
    public TableColumn<Appointment, String> apptDescriptionColumn;
    @FXML
    public TableColumn<Appointment, String> apptLocationColumn;
    @FXML
    public TableColumn<Appointment, String> apptContactColumn;
    @FXML
    public TableColumn<Appointment, String> apptTypeColumn;
    @FXML
    public TableColumn<Appointment, String> apptStartDateTime;
    @FXML
    public TableColumn<Appointment, String> apptEndDateTime;
    @FXML
    public TableColumn<Appointment, Integer> apptCustomerID;
    @FXML
    public TableColumn<Appointment, Integer> apptUserIDColumn;
    @FXML
    public RadioButton viewByWeekRadio;
    @FXML
    public ToggleGroup viewSelector;
    @FXML
    public RadioButton viewByMonthRadio;
    @FXML
    public TableView<Appointment> appointmentsTable;
    public static Appointment selectedAppointment;
    public RadioButton viewAllRadio;

    String THIS_MONTH_QUERY = "SELECT * FROM client_schedule.appointments WHERE MONTH(Start) = MONTH(now()) AND YEAR(Start) = YEAR(now());";
    String THIS_WEEK_QUERY = "SELECT * FROM client_schedule.appointments WHERE WEEK(Start) = WEEK(now()) AND YEAR(Start) = YEAR(now());";
    String ALL_TIME_QUERY = "SELECT * FROM client_schedule.appointments";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            loadAppointments(THIS_WEEK_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentsTable.setItems(Appointment.getAllAppointments());
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        apptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        apptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartDateTime.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        apptEndDateTime.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        apptCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

//        Appointment nextAppointment = Utility.appointmentWithinFifteen();
//        if (nextAppointment != null) {
//            String message = String.format("Your appointment (ID: %1$s) is scheduled for %2$s",
//                    nextAppointment.getAppointmentID(),
//                    nextAppointment.getAppointmentStartDateTime());
//
//            Utility.informationAlert(
//                    "Upcoming Appointment",
//                    "You have an appointment in the next 15 minutes",
//                    message);
//        } else {
//            Utility.informationAlert(
//                    "Upcoming Appointment",
//                    "You have no upcoming appointments in the next 15 minutes",
//                    "");
//        }
    }

    public void addButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("AddAppointmentView.fxml"), "Add New Appointment");
    }

    public void modifyButtonClicked(ActionEvent actionEvent) throws IOException {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            errorAlert("", "you must select an appointment to modify");
        } else {
            closeWindow(actionEvent);
            selectedAppointment = appointment;
            getStage((Main.class.getResource("ModifyAppointmentView.fxml")), "Modify Appointment");
        }
    }

    public void deleteButtonClicked(ActionEvent actionEvent) throws SQLException {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            errorAlert("", "you must select an appointment to delete");
        } else {
            if (confirmationAlert("Confirm Operation",String.format("Are you sure you would like to delete this appointment?" +
                    "\nAppointment ID: %1$s\tAppointment Type: %2$s", appointment.getID(), appointment.getType()))) {
                String query = String.format("DELETE FROM appointments WHERE Appointment_ID = %1$d", appointment.getID());

                Statement stmt = connection.createStatement();
                int deleteResponse = stmt.executeUpdate(query);

                if (deleteResponse != 1) {
                    errorAlert("Error","Error deleting appointment");
                    return;
                } else {
                    if ((RadioButton) viewSelector.getSelectedToggle() == viewByWeekRadio) {
                        loadAppointments(THIS_WEEK_QUERY);
                    } else if ((RadioButton) viewSelector.getSelectedToggle() == (RadioButton) viewByMonthRadio) {
                        loadAppointments(THIS_MONTH_QUERY);
                    } else {
                        loadAppointments(ALL_TIME_QUERY);
                    }
                }
            }
        }
    }

    public void loadAppointments(String query) throws SQLException {
        Appointment.allAppointments.clear();

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) {
            Appointment appointment = new Appointment();
            appointment.setID(resultSet.getInt("Appointment_ID"));
            appointment.setCustomerID(resultSet.getInt("Customer_ID"));
            appointment.setUserID(resultSet.getInt("User_ID"));
            appointment.setContactName(getContactNameFromContactID(resultSet.getInt("Contact_ID")));
            appointment.setTitle(resultSet.getString("Title"));
            appointment.setLocation(resultSet.getString("Location"));
            appointment.setDescription(resultSet.getString("Description"));
            appointment.setAppointmentType(resultSet.getString("Type"));

            String sqlTimeStamp = resultSet.getString("Start");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(LoginController.utcZone);
            String result = dateFormatter(Instant.from(formatter.parse(sqlTimeStamp)), LoginController.zoneID);
            appointment.setStartDateTime(result);

            sqlTimeStamp = resultSet.getString("End");
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(LoginController.utcZone);
            result = dateFormatter(Instant.from(formatter.parse(sqlTimeStamp)), LoginController.zoneID);
            appointment.setEndDateTime(result);

            Appointment.addAppointment(appointment);
        }
    }

    public void customersButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("CustomerView.fxml"), "Customers");
    }

    public void viewByWeekRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(THIS_WEEK_QUERY);
    }

    public void viewByMonthRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(THIS_MONTH_QUERY);
    }

    public void viewAllRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(ALL_TIME_QUERY);
    }
}

