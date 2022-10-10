package com.main.javafxapp.Toolkit;

import com.main.javafxapp.Controllers.LoginController;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.User;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static com.main.javafxapp.Controllers.LoginController.authenticatedUser;
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

    public static void informationAlert(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

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

    public static Appointment appointmentWithinFifteen(){

        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        LocalDateTime startTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime endTime = startTime.plusMinutes(15);


        int userID = Integer.parseInt(authenticatedUser.getUserID());

        try{

            String query = String.format("""
                    SELECT * FROM appointment\s
                    JOIN customer ON appointment.customerId=customer.customerId JOIN user ON appointment.userId=user.userId
                    WHERE start BETWEEN '%1$s' AND '%2$s' AND appointment.userId='%3$s'""",startTime, endTime, userID);

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            
            if(resultSet.next()){
                Appointment nextAppointment = new Appointment();
                LocalDateTime startDateTime = resultSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endDateTime = resultSet.getTimestamp("end").toLocalDateTime();

                ZonedDateTime startZonedDateTime = startDateTime.atZone(ZoneId.of("UTC"));
                ZonedDateTime endZonedDateTIme = endDateTime.atZone(ZoneId.of("UTC"));
                ZonedDateTime localStartZonedDateTime = startZonedDateTime.withZoneSameInstant(zoneId);
                ZonedDateTime localEndZonedDateTime = endZonedDateTIme.withZoneSameInstant(zoneId);

//                nextAppointment.setAppointmentID(resultSet.getInt("appointmentId"));
//                nextAppointment.setAppointmentCustomerID(resultSet.getString("customerId"));
//                nextAppointment.setAppointmentTitle(resultSet.getString("title"));
//                nextAppointment.setAppointmentType(resultSet.getString("type"));
//                nextAppointment.setAppointmentCustomerName(resultSet.getString("customerName"));
//                nextAppointment.setAppointmentStartDate(localStartZonedDateTime);
//                nextAppointment.setAppointmentStartTime(localStartZonedDateTime);
//                nextAppointment.setAppointmentEndDate(localEndZonedDateTime);
//                nextAppointment.setAppointmentEndTime(localEndZonedDateTime);
                return nextAppointment;
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ResultSet getCustomers() throws SQLException {
        String query = "SELECT * FROM customers";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);

    }

    public static ResultSet getContacts() throws SQLException {
        String query = "SELECT * FROM contacts";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);

    }

    public static ResultSet getUsers() throws SQLException {
        String query = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);

    }

    public static Instant instantBuilder(LocalDate localDate, int hour, int minute) {

        LocalDateTime dateTime = LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),hour,minute);
        return dateTime.atZone(LoginController.zoneID).toInstant();
    }

    public static Instant convertToTimezone(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toInstant();
    }

    public static String dateFormatter(Instant instant, ZoneId targetTimeZone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(targetTimeZone);
        return dateTimeFormatter.format(instant);
    }

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

}
