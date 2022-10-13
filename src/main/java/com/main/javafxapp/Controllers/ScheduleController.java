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
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;

import static com.main.javafxapp.Controllers.LoginController.zoneID;
import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

/**
 * The type Schedule controller.
 */
public class ScheduleController implements Initializable{

    /**
     * The Appt id column.
     */
    @FXML
    public TableColumn<Appointment, Integer> apptIDColumn;
    /**
     * The Appt title column.
     */
    @FXML
    public TableColumn<Appointment, String> apptTitleColumn;
    /**
     * The Appt description column.
     */
    @FXML
    public TableColumn<Appointment, String> apptDescriptionColumn;
    /**
     * The Appt location column.
     */
    @FXML
    public TableColumn<Appointment, String> apptLocationColumn;
    /**
     * The Appt contact column.
     */
    @FXML
    public TableColumn<Appointment, String> apptContactColumn;
    /**
     * The Appt type column.
     */
    @FXML
    public TableColumn<Appointment, String> apptTypeColumn;
    /**
     * The Appt start date time.
     */
    @FXML
    public TableColumn<Appointment, String> apptStartDateTime;
    /**
     * The Appt end date time.
     */
    @FXML
    public TableColumn<Appointment, String> apptEndDateTime;
    /**
     * The Appt customer id.
     */
    @FXML
    public TableColumn<Appointment, Integer> apptCustomerID;
    /**
     * The Appt user id column.
     */
    @FXML
    public TableColumn<Appointment, Integer> apptUserIDColumn;
    /**
     * The View by week radio.
     */
    @FXML
    public RadioButton viewByWeekRadio;
    /**
     * The View selector.
     */
    @FXML
    public ToggleGroup viewSelector;
    /**
     * The View by month radio.
     */
    @FXML
    public RadioButton viewByMonthRadio;
    /**
     * The Appointments table.
     */
    @FXML
    public TableView<Appointment> appointmentsTable;
    /**
     * The constant selectedAppointment.
     */
    public static Appointment selectedAppointment;
    /**
     * The View all radio.
     */
    public RadioButton viewAllRadio;

    /**
     * The This month query.
     */
    String THIS_MONTH_QUERY = "SELECT * FROM client_schedule.appointments WHERE MONTH(Start) = MONTH(now()) AND YEAR(Start) = YEAR(now());";
    /**
     * The This week query.
     */
    String THIS_WEEK_QUERY = "SELECT * FROM client_schedule.appointments WHERE WEEK(Start) = WEEK(now()) AND YEAR(Start) = YEAR(now());";
    /**
     * The All time query.
     */
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

    }

    /**
     * Add button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void addButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("AddAppointmentView.fxml"), "Add New Appointment");
    }

    /**
     * Modify button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
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

    /**
     * Delete button clicked.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
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

    /**
     * Load appointments.
     *
     * @param query the query
     * @throws SQLException the sql exception
     */
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
            String result = dateFormatter(Instant.from(formatter.parse(sqlTimeStamp)), zoneID);
            appointment.setStartDateTime(result);

            sqlTimeStamp = resultSet.getString("End");
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(LoginController.utcZone);
            result = dateFormatter(Instant.from(formatter.parse(sqlTimeStamp)), LoginController.zoneID);
            appointment.setEndDateTime(result);

            Appointment.addAppointment(appointment);
        }
    }

    /**
     * Customers button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void customersButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("CustomerView.fxml"), "Customers");
    }

    /**
     * View by week radio clicked.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void viewByWeekRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(THIS_WEEK_QUERY);
    }

    /**
     * View by month radio clicked.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void viewByMonthRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(THIS_MONTH_QUERY);
    }

    /**
     * View all radio clicked.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void viewAllRadioClicked(ActionEvent actionEvent) throws SQLException {
        loadAppointments(ALL_TIME_QUERY);
    }

    /**
     * Reports button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void reportsButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ReportsView.fxml"), "Reports");
    }
}

