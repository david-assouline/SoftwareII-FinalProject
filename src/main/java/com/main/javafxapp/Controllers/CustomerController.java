package com.main.javafxapp.Controllers;

import com.main.javafxapp.Main;
import com.main.javafxapp.Models.Appointment;
import com.main.javafxapp.Models.Customer;
import com.main.javafxapp.Toolkit.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.main.javafxapp.Toolkit.JDBC.connection;
import static com.main.javafxapp.Toolkit.Utility.*;
import static com.main.javafxapp.Toolkit.Utility.dateFormatter;

public class CustomerController implements Initializable {
    @FXML
    public TableColumn<Customer, String> customerPhoneNumberColumn;
    @FXML
    public TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML
    public TableColumn<Customer, String> customerAddressColumn;
    @FXML
    public TableColumn<Customer, String> customerNameColumn;
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, String> customerDivisionColumn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customersTable.setItems(Customer.getAllCustomers());
        customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
    public void addButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("AddCustomerView.fxml"), "Add Customer");

    }

    public void deleteButtonClicked(ActionEvent actionEvent) throws SQLException {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            errorAlert("", "you must select a customer to delete");
        } else {
            if (confirmationAlert("Confirm Operation","Are you sure you would like to delete this customer?")) {
                String query = String.format("DELETE FROM customers WHERE Customer_ID = %1$d", customer.getCustomerID());

                Statement stmt = connection.createStatement();
                int deleteResponse = stmt.executeUpdate(query);

                if (deleteResponse != 1) {
                    errorAlert("Error","Error deleting customer");
                    return;
                } else {
                    loadCustomers();
                }
            }
        }
    }

    public void modifyButtonClicked(ActionEvent actionEvent) {
    }

    public void scheduleViewButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Schedule");
    }

    public void loadCustomers() throws SQLException {
        Customer.allCustomers.clear();
        ResultSet resultSet = getCustomers();

        while (resultSet.next()) {
            Customer customer = new Customer();
            customer.setCustomerID(resultSet.getInt("Customer_ID"));
            customer.setName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setDivision(getDivisionNameByDivisionID(resultSet.getInt("Division_ID")));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setPhone(resultSet.getString("Phone"));

            Customer.addCustomer(customer);
        }
    }
}
