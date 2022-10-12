package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.User;
import com.main.javafxapp.Toolkit.JDBC;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.dateFormatter;
import static com.main.javafxapp.Toolkit.Utility.informationAlert;

public class LoginController implements Initializable {
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public ToggleGroup LanguageToggleGroup;
    public Text userLocationText;

    public static User authenticatedUser;
    public static ZoneId zoneID;
    public static ZoneId utcZone;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Locale locale = Locale.getDefault();
        zoneID = ZoneId.systemDefault();
        utcZone = ZoneId.of("UTC");
        userLocationText.setText(zoneID.getId());

    }

    public void loginButton(ActionEvent actionEvent) throws SQLException, IOException {

        String usernameText = usernameTextField.getText();
        String passwordText = passwordTextField.getText();

        if (Utility.authenticateLogin(JDBC.connection, usernameText, passwordText)) {
            authenticatedUser.setZoneID(zoneID);

            try {
                if (checkWithinFifteen() == -1) {
                    informationAlert("Information Alert","No upcoming appointments","No appointments starting within the next 15 minutes");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Utility.closeWindow(actionEvent);
            Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Appointment Schedule");

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);
            outputFile.print(String.format("Successful login for user: %1$s at: %2$s \n", usernameText, ZonedDateTime.now(utcZone)));
            outputFile.close();

        } else {
            Utility.errorAlert("Authentication error", "Could not authenticate user with the given credentials");

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);
            outputFile.print(String.format("Unsuccessful login for user: %1$s at: %2$s \n", usernameText, ZonedDateTime.now(utcZone)));
            outputFile.close();
        }
    }

    public void resetButton(ActionEvent actionEvent) {
    }

    public int checkWithinFifteen() throws SQLException {
        int response = -1;

        Appointment.allAppointments.clear();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM client_schedule.appointments");

        while (resultSet.next()) {
            String sqlTimeStamp = resultSet.getString("Start");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(LoginController.utcZone);
            String result = dateFormatter(Instant.from(formatter.parse(sqlTimeStamp)), zoneID);
            Instant startTime = Instant.from(formatter.parse(sqlTimeStamp));

            if (startTime.isAfter(Instant.now()) && startTime.isBefore(Instant.now().plus(15, ChronoUnit.MINUTES))) {
                informationAlert("Upcoming appointment","You have an appointment within the next 15 minutes",
                        String.format("Appointment ID %1$d starts at %2$s", resultSet.getInt("Appointment_ID"), result));
                response = 0;
            }
        }
        return response;
    }
}
