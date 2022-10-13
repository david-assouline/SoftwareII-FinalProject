package com.main.javafxapp.Toolkit;

import com.main.javafxapp.Controllers.LoginController;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

import static com.main.javafxapp.Controllers.LoginController.authenticatedUser;
import static com.main.javafxapp.Controllers.LoginController.utcZone;
import static com.main.javafxapp.Toolkit.JDBC.connection;

/**
 * The type Utility.
 */
public class Utility {

    /**
     * Gets stage.
     *
     * @param targetUrl  the target url
     * @param stageTitle the stage title
     * @throws IOException the io exception
     */
    public static void getStage(URL targetUrl, String stageTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(targetUrl);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Close window.
     *
     * @param event the event
     */
    public static void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Error alert.
     *
     * @param title     the title
     * @param alertText the alert text
     */
    public static void errorAlert(String title, String alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, alertText);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    /**
     * Confirmation alert boolean.
     *
     * @param title     the title
     * @param alertText the alert text
     * @return the boolean
     */
    public static boolean confirmationAlert(String title, String alertText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertText, ButtonType.CANCEL, ButtonType.YES);
        alert.setTitle(title);
        alert.setHeaderText(title);
        Optional<ButtonType> confirm = alert.showAndWait();
        return (confirm.isPresent() && confirm.get() == ButtonType.YES);
    }

    /**
     * Information alert.
     *
     * @param title       the title
     * @param headerText  the header text
     * @param contextText the context text
     */
    public static void informationAlert(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    /**
     * Authenticate login boolean.
     *
     * @param connection the connection
     * @param username   the username
     * @param password   the password
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public static boolean authenticateLogin(Connection connection, String username, String password) throws SQLException {

        String query = String.format("SELECT * FROM users WHERE User_Name='%1$s' AND Password='%2$s'", username, password);

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                authenticatedUser = new User();
                authenticatedUser.setUserID(resultSet.getString("User_ID"));
                authenticatedUser.setUserName(resultSet.getString("User_Name"));
                System.out.printf("Successfully authenticated user: %1$s with user_ID: %2$s%n", authenticatedUser.getUserName(), authenticatedUser.getUserID());
                return true;
            } else {
                System.out.printf("Error authenticating user: %1$s with password: %2$s%n", username, password);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets customers.
     *
     * @return the customers
     * @throws SQLException the sql exception
     */
    public static ResultSet getCustomers() throws SQLException {
        String query = "SELECT * FROM customers";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Gets contacts.
     *
     * @return the contacts
     * @throws SQLException the sql exception
     */
    public static ResultSet getContacts() throws SQLException {
        String query = "SELECT * FROM contacts";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Gets users.
     *
     * @return the users
     * @throws SQLException the sql exception
     */
    public static ResultSet getUsers() throws SQLException {
        String query = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Gets countries.
     *
     * @return the countries
     * @throws SQLException the sql exception
     */
    public static ResultSet getCountries() throws SQLException {
        String query = "SELECT * FROM countries";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Gets divisions by country name.
     *
     * @param countryName the country name
     * @return the divisions by country name
     * @throws SQLException the sql exception
     */
    public static ResultSet getDivisionsByCountryName(String countryName) throws SQLException {
        String query = String.format("""
                SELECT * FROM countries WHERE Country = "%1$s"
                """, countryName);
        Statement stmt = connection.createStatement();
        ResultSet resultSet =  stmt.executeQuery(query);

        if (resultSet.next()) {
            query = String.format("""
        SELECT Division FROM first_level_divisions WHERE Country_ID = "%1$s"
        """, resultSet.getString("Country_ID"));
        }
        return stmt.executeQuery(query);
    }

    /**
     * Gets division id by division name.
     *
     * @param divisionName the division name
     * @return the division id by division name
     * @throws SQLException the sql exception
     */
    public static int getDivisionIDByDivisionName(String divisionName) throws SQLException {
        String query = String.format("""
                SELECT Division_ID FROM first_level_divisions WHERE Division = "%1$s"
                """, divisionName);
        Statement stmt = connection.createStatement();
        ResultSet resultSet =  stmt.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("Division_ID");
        }
        return -1;
    }

    /**
     * Gets division name by division id.
     *
     * @param divisionID the division id
     * @return the division name by division id
     * @throws SQLException the sql exception
     */
    public static String getDivisionNameByDivisionID(int divisionID) throws SQLException {
        String query = String.format("""
                SELECT Division FROM first_level_divisions WHERE Division_ID = "%1$d"
                """, divisionID);
        Statement stmt = connection.createStatement();
        ResultSet resultSet =  stmt.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getString("Division");
        }
        return null;
    }

    /**
     * Instant builder instant.
     *
     * @param localDate the local date
     * @param hour      the hour
     * @param minute    the minute
     * @return the instant
     */
    public static Instant instantBuilder(LocalDate localDate, int hour, int minute) {

        LocalDateTime dateTime = LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),hour,minute);
        return dateTime.atZone(LoginController.zoneID).toInstant();
    }

    /**
     * Date formatter string.
     *
     * @param instant        the instant
     * @param targetTimeZone the target time zone
     * @return the string
     */
    public static String dateFormatter(Instant instant, ZoneId targetTimeZone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(targetTimeZone);
        return dateTimeFormatter.format(instant);
    }

    /**
     * Gets contact name from contact id.
     *
     * @param contactID the contact id
     * @return the contact name from contact id
     * @throws SQLException the sql exception
     */
    public static String getContactNameFromContactID (int contactID) throws SQLException {
        String query = String.format("SELECT Contact_Name FROM contacts WHERE Contact_ID = %1$d",contactID);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getString("Contact_Name");
        } else {
            return null;
        }
    }

    /**
     * Gets country name from division id.
     *
     * @param divisionID the division id
     * @return the country name from division id
     * @throws SQLException the sql exception
     */
    public static String getCountryNameFromDivisionID (int divisionID) throws SQLException {
        int countryID;

        String query = String.format("SELECT Country_ID FROM first_level_divisions WHERE Division_ID = %1$d", divisionID);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        if (resultSet.next()) {
            countryID = resultSet.getInt("Country_ID");
        } else {
            return null;
        }

        query = String.format("SELECT Country FROM countries WHERE Country_ID = %1$d", countryID);

        resultSet = stmt.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getString("Country");
        } else {
            return null;
        }
    }

    /**
     * Customer has appointment boolean.
     *
     * @param customerID the customer id
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public static boolean customerHasAppointment(int customerID) throws SQLException {
        String query = String.format("SELECT * FROM appointments WHERE Customer_ID = %1$d", customerID);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Has overlapping appointment boolean.
     *
     * @param proposedStartTime the proposed start time
     * @param proposedEndTime   the proposed end time
     * @param customerID        the customer id
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public static boolean hasOverlappingAppointment(Instant proposedStartTime, Instant proposedEndTime, int customerID) throws SQLException {
        String query = String.format("""
        SELECT * FROM appointments WHERE Customer_ID = %1$d
        """, customerID);
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(LoginController.utcZone);

            String sqlStartTimeStamp = resultSet.getString("Start");
            Instant existingStartTime = Instant.from(formatter.parse(sqlStartTimeStamp));

            String sqlEndTimeStamp = resultSet.getString("End");
            Instant existingEndTime = Instant.from(formatter.parse(sqlEndTimeStamp));

            if (proposedStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(proposedEndTime)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Generate customer report observable list.
     *
     * @return the observable list
     * @throws SQLException the sql exception
     */
    public static ObservableList<String> generateCustomerReport () throws SQLException {
        ObservableList<String> response = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();

        response.add("Appointments by type and month:\n\n");

        String typeQuery = ("SELECT Type, COUNT(Type) as \"Total\" FROM appointments GROUP BY Type");
        String monthQuery = ("SELECT MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month");

        ResultSet typeResultSet = stmt.executeQuery(typeQuery);
        while (typeResultSet.next()) {
            String toAdd = String.format("%1$s appointment(s) of type \"%2$s\" \n", typeResultSet.getString("Total"), typeResultSet.getString("Type"));
            response.add(toAdd);
        }
        response.add("\n");
        ResultSet monthResultSet = stmt.executeQuery(monthQuery);
        while (monthResultSet.next()) {
            String toAdd = String.format("%1$s has %2$s appointment(s) \n", monthResultSet.getString("Month"), monthResultSet.getString("Total"));
            response.add(toAdd);
        }

        return response;
    }

    /**
     * Generate contact appointments report observable list.
     *
     * @return the observable list
     * @throws SQLException the sql exception
     */
    public static ObservableList<String> generateContactAppointmentsReport () throws SQLException {
        ObservableList<String> response = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();

        String query = ("SELECT DISTINCT Contact_ID FROM contacts");
        ResultSet resultSet = stmt.executeQuery(query);

        List<String> listOfDistinctContacts = new ArrayList<>();

        while (resultSet.next()) {
            listOfDistinctContacts.add(resultSet.getString("Contact_ID"));
        }

        for (String contactID: listOfDistinctContacts) {
            query = ("SELECT * FROM appointments WHERE Contact_ID = " + contactID);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                response.add(String.format("""
                        Contact %1$s has appointment: %2$s of type: %3$s (%4$s) starting at %5$s and ending at %6$s with customer %7$s
                        
                        """, resultSet.getString("Appointment_ID"), resultSet.getString("Title"),
                        resultSet.getString("Type"), resultSet.getString("Description"),
                        resultSet.getString("Start"), resultSet.getString("End"),
                        resultSet.getString("Customer_ID")));
            }
        }

        return response;
    }

    /**
     * Generate contacts report observable list.
     *
     * @return the observable list
     * @throws SQLException the sql exception
     */
    public static ObservableList<String> generateContactsReport() throws SQLException {
        ObservableList<String> response = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();

        String query = ("SELECT * FROM contacts");
        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) {
            response.add(String.format("""
                    Contact ID: %1$s
                    Contact Name: %2$s
                    Contact E-mail: %3$s
                    
                    """, resultSet.getString("Contact_ID"), resultSet.getString("Contact_Name"),
                    resultSet.getString("Email")));
        }
        return response;
    }

}
