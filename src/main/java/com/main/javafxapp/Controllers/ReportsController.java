package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Toolkit.Utility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The type Reports controller.
 */
public class ReportsController {
    /**
     * The Text area.
     */
    @FXML
    public TextArea textArea;

    /**
     * Customer report clicked. Contains Lambda expression to make the code easier to read and more concise.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void customerReportClicked(ActionEvent actionEvent) throws SQLException {
        textArea.clear();
        ObservableList<String> tempString = Utility.generateCustomerReport();

        tempString.forEach( (customerReport) -> {
            textArea.appendText(customerReport);
        });
    }

    /**
     * Schedule report clicked. Contains Lambda expression to make the code easier to read and more concise.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void scheduleReportClicked(ActionEvent actionEvent) throws SQLException {
        textArea.clear();
        ObservableList<String> tempString = Utility.generateContactAppointmentsReport();

        tempString.forEach( (appointmentReport) -> {
            textArea.appendText(appointmentReport);
        });
    }

    /**
     * Contacts report clicked. Contains Lambda expression to make the code easier to read and more concise.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void contactsReportClicked(ActionEvent actionEvent) throws SQLException {
        textArea.clear();
        ObservableList<String> tempString = Utility.generateContactsReport();

        tempString.forEach( (contactReport) -> {
            textArea.appendText(contactReport);
        });
    }

    /**
     * Back to schedule clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void backToScheduleClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Schedule");
    }
}
