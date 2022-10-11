package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Customer;
import com.main.javafxapp.Toolkit.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static com.main.javafxapp.Controllers.CustomerController.selectedCustomer;
import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;

public class ModifyCustomerController implements Initializable {

    @FXML
    public TextField customerID;
    @FXML
    public TextField nameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField postalCodeField;
    @FXML
    public TextField phoneNumberField;
    @FXML
    public ComboBox<String> countryComboBox;
    @FXML
    public ComboBox<String> divisionComboBox;
    public ObservableList<String> countryList = FXCollections.observableArrayList();
    public ObservableList<String> divisionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        phoneNumberField.setText(selectedCustomer.getPhone());

        try {
            ResultSet resultSet = Utility.getCountries();
            while (resultSet.next()) {
                countryList.add(resultSet.getString("Country"));
            }
            countryComboBox.setItems(countryList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        countryComboBox.setValue(selectedCustomer.getCountry());
        divisionComboBox.setValue(selectedCustomer.getDivision());

        try {
            divisionList.clear();
            ResultSet resultSet = Utility.getDivisionsByCountryName(countryComboBox.getValue());
            while (resultSet.next()) {
                divisionList.add(resultSet.getString("Division"));
            }
            divisionComboBox.setItems(divisionList);
        } catch (SQLException e) {
            return;
        }
    }

    public void saveButtonClicked(ActionEvent actionEvent) {
        try {
            Customer customer = new Customer();

            customer.setCustomerID(Integer.parseInt(customerID.getText()));
            customer.setName(nameField.getText());
            customer.setAddress(addressField.getText());
            customer.setPostalCode(postalCodeField.getText());
            customer.setPhone(phoneNumberField.getText());
            customer.setCountry(countryComboBox.getValue());
            customer.setDivision(divisionComboBox.getValue());

            String query = String.format("DELETE FROM customers WHERE Customer_ID = %1$d",customer.getCustomerID());

            Statement stmt = connection.createStatement();
            int deleteResponse = stmt.executeUpdate(query);

            if (deleteResponse != 1) {
                errorAlert("Error","Error modifying customer");
                return;
            }

            query = String.format("""
                    INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)
                    VALUES ("%1$d", "%2$s", "%3$s", "%4$s", "%5$s", NULL, NULL, NULL, NULL, "%6$d");
                    """,
                    customer.getCustomerID(), customer.getName(), customer.getAddress(), customer.getPostalCode(),
                    customer.getPhone(), getDivisionIDByDivisionName(customer.getDivision()));

            int response = stmt.executeUpdate(query);

            if (response == 1) {
                informationAlert("Confirmation", "Successfully modified","Customer has been saved to the database");
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

    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("CustomerView.fxml"), "Customers");
    }

    public void countrySelected(ActionEvent actionEvent) throws SQLException {
        divisionList.clear();
        divisionComboBox.setValue("");
        ResultSet resultSet = Utility.getDivisionsByCountryName(countryComboBox.getValue());
        while (resultSet.next()) {
            divisionList.add(resultSet.getString("Division"));
        }
        divisionComboBox.setItems(divisionList);
    }
}
