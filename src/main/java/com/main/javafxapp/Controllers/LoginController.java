package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.User;
import com.main.javafxapp.Toolkit.JDBC;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
import java.util.Objects;
import java.util.ResourceBundle;

import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.dateFormatter;
import static com.main.javafxapp.Toolkit.Utility.informationAlert;

/**
 * The type Login controller.
 */
public class LoginController implements Initializable {
    /**
     * The Username text field.
     */
    @FXML
    public TextField usernameTextField;
    /**
     * The Password text field.
     */
    @FXML
    public TextField passwordTextField;
    /**
     * The User location text.
     */
    @FXML
    public Text userLocationText;

    /**
     * The constant authenticatedUser.
     */
    public static User authenticatedUser;
    /**
     * The constant zoneID.
     */
    public static ZoneId zoneID;
    /**
     * The constant utcZone.
     */
    public static ZoneId utcZone;
    /**
     * The Username label.
     */
    @FXML
    public Text usernameLabel;
    /**
     * The Password label.
     */
    @FXML
    public Text passwordLabel;
    /**
     * The Login button.
     */
    @FXML
    public Button loginButton;
    /**
     * The Exit label.
     */
    @FXML
    public Button exitLabel;
    /**
     * The User location label.
     */
    @FXML
    public Label userLocationLabel;
    /**
     * The Locale.
     */
    public Locale locale;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        locale = Locale.getDefault();
        zoneID = ZoneId.systemDefault();
        utcZone = ZoneId.of("UTC");
        userLocationText.setText(zoneID.getId());

        if (Objects.equals(locale.getLanguage(), "fr")) {
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginButton.setText("Connexion");
            exitLabel.setText("Quitter");
            userLocationLabel.setText("Emplacement:");
        }
    }

    /**
     * Login button.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
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
            if (Objects.equals(locale.getLanguage(), "fr")) {
                Utility.errorAlert("Erreur d'authentification", "Vos informations n'ont pas été trouvés dans la base de données");
            } else {
                Utility.errorAlert("Authentication error", "Could not authenticate user with the given credentials");
            }

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);
            outputFile.print(String.format("Unsuccessful login for user: %1$s at: %2$s \n", usernameText, ZonedDateTime.now(utcZone)));
            outputFile.close();
        }
    }

    /**
     * Check within fifteen int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
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

    /**
     * Exit button clicked.
     *
     * @param actionEvent the action event
     */
    public void exitButtonClicked(ActionEvent actionEvent) {
        Utility.closeWindow(actionEvent);
    }
}
