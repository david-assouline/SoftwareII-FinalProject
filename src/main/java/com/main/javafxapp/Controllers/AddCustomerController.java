package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.Customer;
import com.main.javafxapp.Toolkit.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

import static com.main.javafxapp.Controllers.LoginController.utcZone;
import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

/**
 * The type Add customer controller.
 */
public class AddCustomerController implements Initializable {

    /**
     * The Name field.
     */
    public TextField nameField;
    /**
     * The Address field.
     */
    public TextField addressField;
    /**
     * The Postal code field.
     */
    public TextField postalCodeField;
    /**
     * The Phone number field.
     */
    public TextField phoneNumberField;
    /**
     * The Country combo box.
     */
    public ComboBox<String> countryComboBox;
    /**
     * The Division combo box.
     */
    public ComboBox<String> divisionComboBox;
    /**
     * The Country list.
     */
    public ObservableList<String> countryList = FXCollections.observableArrayList();
    /**
     * The Division list.
     */
    public ObservableList<String> divisionList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ResultSet resultSet = Utility.getCountries();
            while (resultSet.next()) {
                countryList.add(resultSet.getString("Country"));
            }
            countryComboBox.setItems(countryList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save button clicked.
     *
     * @param actionEvent the action event
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        Random rand = new Random();
        try {
            int id = rand.nextInt(1000);
            String customerName = nameField.getText();
            String customerAddress = addressField.getText();
            String customerPostalCode = postalCodeField.getText();
            String customerPhoneNumber = phoneNumberField.getText();
            String customerCountry = countryComboBox.getValue();
            String customerDivision = divisionComboBox.getValue();

            Customer customer = new Customer();

            customer.setCustomerID(id);
            customer.setName(customerName);
            customer.setAddress(customerAddress);
            customer.setPostalCode(customerPostalCode);
            customer.setPhone(customerPhoneNumber);
            customer.setCountry(customerCountry);
            customer.setDivision(customerDivision);

            String query = String.format("""
                    INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)
                    VALUES ("%1$d", "%2$s", "%3$s", "%4$s", "%5$s", NULL, NULL, NULL, NULL, "%6$d");
                    """,
                    customer.getCustomerID(), customer.getName(), customer.getAddress(), customer.getPostalCode(),
                    customer.getPhone(), getDivisionIDByDivisionName(customerDivision));

            Statement stmt = connection.createStatement();
            int response = stmt.executeUpdate(query);

            if (response == 1) {
                informationAlert("Confirmation", "Successfully added","Customer has been saved to the database");
                Utility.closeWindow(actionEvent);
                Utility.getStage(Main.class.getResource("CustomerView.fxml"), "Customers");
            } else {
                errorAlert("Error","Could not save customer to the database");
                return;
            }

        } catch (Exception e) {
            Utility.errorAlert("Error","Invalid Data");
            return;
        }
    }

    /**
     * Cancel button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("CustomerView.fxml"), "Customers");
    }

    /**
     * Country selected.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void countrySelected(ActionEvent actionEvent) throws SQLException {
        divisionList.clear();
        ResultSet resultSet = Utility.getDivisionsByCountryName(countryComboBox.getValue());
        while (resultSet.next()) {
            divisionList.add(resultSet.getString("Division"));
        }
        divisionComboBox.setItems(divisionList);

    }
}
