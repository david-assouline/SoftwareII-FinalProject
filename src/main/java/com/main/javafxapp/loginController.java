package com.main.javafxapp;

import com.main.javafxapp.Toolkit.JDBC;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public ToggleGroup LanguageToggleGroup;
    public Text userLocationText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Locale locale = Locale.getDefault();
        ZoneId zoneId = ZoneId.systemDefault();
        userLocationText.setText(zoneId.getId());
    }

    public void loginButton(ActionEvent actionEvent) throws SQLException, IOException {

        String usernameText = usernameTextField.getText();
        String passwordText = passwordTextField.getText();

        if (Utility.authenticateLogin(JDBC.connection, usernameText, passwordText)) {
            Utility.closeWindow(actionEvent);
            Utility.getStage(Main.class.getResource("scheduleView.fxml"), "AppointmentSchedule");

        } else {
            Utility.errorAlert("Authentication error", "Could not authenticate user with the given credentials");
        }
    }

    public void resetButton(ActionEvent actionEvent) {
    }
}
