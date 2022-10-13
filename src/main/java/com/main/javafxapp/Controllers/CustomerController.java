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

/**
 * The type Customer controller.
 */
public class CustomerController implements Initializable {
    /**
     * The Customer phone number column.
     */
    @FXML
    public TableColumn<Customer, String> customerPhoneNumberColumn;
    /**
     * The Customer postal code column.
     */
    @FXML
    public TableColumn<Customer, String> customerPostalCodeColumn;
    /**
     * The Customer address column.
     */
    @FXML
    public TableColumn<Customer, String> customerAddressColumn;
    /**
     * The Customer name column.
     */
    @FXML
    public TableColumn<Customer, String> customerNameColumn;
    /**
     * The Customers table.
     */
    @FXML
    public TableView<Customer> customersTable;
    /**
     * The Customer division column.
     */
    @FXML
    public TableColumn<Customer, String> customerDivisionColumn;
    /**
     * The constant selectedCustomer.
     */
    public static Customer selectedCustomer;

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

    /**
     * Add button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void addButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("AddCustomerView.fxml"), "Add Customer");

    }

    /**
     * Delete button clicked.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void deleteButtonClicked(ActionEvent actionEvent) throws SQLException {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            errorAlert("", "you must select a customer to delete");
        } else {
            if (customerHasAppointment(customer.getCustomerID())) {
                errorAlert("Could not complete operation","Customer has existing appointments. Please delete" +
                        " them first!");
                return;
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
    }

    /**
     * Modify button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void modifyButtonClicked(ActionEvent actionEvent) throws IOException {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            errorAlert("", "you must select a customer to modify");
        } else {
            closeWindow(actionEvent);
            selectedCustomer = customer;
            getStage((Main.class.getResource("ModifyCustomerView.fxml")), "Modify Customer");
        }
    }

    /**
     * Schedule view button clicked.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void scheduleViewButtonClicked(ActionEvent actionEvent) throws IOException {
        Utility.closeWindow(actionEvent);
        Utility.getStage(Main.class.getResource("ScheduleView.fxml"), "Schedule");
    }

    /**
     * Load customers.
     *
     * @throws SQLException the sql exception
     */
    public void loadCustomers() throws SQLException {
        Customer.allCustomers.clear();
        ResultSet resultSet = getCustomers();

        while (resultSet.next()) {
            Customer customer = new Customer();
            customer.setCustomerID(resultSet.getInt("Customer_ID"));
            customer.setName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setCountry(getCountryNameFromDivisionID(resultSet.getInt("Division_ID")));
            customer.setDivision(getDivisionNameByDivisionID(resultSet.getInt("Division_ID")));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setPhone(resultSet.getString("Phone"));

            Customer.addCustomer(customer);
        }
    }
}
