/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ipc1.practica2_202003654;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import models.Order;
import models.Product;

/**
 * FXML Controller class
 *
 * @author ferjo
 */
public class OrdersViewController implements Initializable {    
    @FXML
    private TextField productNameInput;
    @FXML
    private TextField productPriceInput;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> productPriceColumn;
    @FXML
    private TableView<Product> orderProductsTable;
    @FXML
    private TableColumn<Product, String> orderProductIdColumn;
    @FXML
    private TableColumn<Product, String> orderProductNameColumn;
    @FXML
    private TableColumn<Product, String> orderProductPriceColumn;
    @FXML
    private ComboBox<String> dealerComboBox;
    @FXML
    private Slider sliderDistance;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label orderTotalLabel;

    private static double orderDistance = 0;
    private static double orderTotal = 0;
    private static Product selectedProduct = null;
    private static List<Product> orderProducts;
    private static String selectedDealer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sliderDistance.setMax(10);
        
        sliderDistance.valueProperty().addListener((observable, oldValue, newValue) -> {
            orderDistance = newValue.doubleValue();
            distanceLabel.setText(String.format("%.2f", orderDistance));
        });

        
        
        ObservableList<String> dealers = FXCollections.observableArrayList(
            "Repartidor 1",
            "Repartidor 2",
            "Repartidor 3"
        );
        dealerComboBox.setItems(dealers);
        
            // Configure dealerComboBox
        dealerComboBox.setItems(dealers);
        dealerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedDealer = newValue;
            System.out.println("Selected dealer: " + selectedDealer);
        });
        // Configure table columns
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Q" + cellData.getValue().getPrice()));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Format the price column
        productPriceColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(price);
                }
            }
        });

        // Enable editing for the price column
        productPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productPriceColumn.setOnEditCommit(event -> {
            // Get the edited price value
            String newPrice = event.getNewValue();
            Product product = event.getTableView().getItems().get(event.getTablePosition().getRow());
            // Update the product with the new price
            product.setPrice(Integer.parseInt(newPrice));
        });

        // Set products list to the table
        refreshTable();
        
        // Configure order products table columns
        orderProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderProductPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Q" + cellData.getValue().getPrice()));
        orderProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Set order products list to the table
        refreshOrderProductsTable();
    }
        
    @FXML
    public void onBack(ActionEvent event) throws IOException {
        App.setRoot("mainMenu");
    }
    
    @FXML
    public void onCreateOrder(ActionEvent event) throws IOException {
        if (selectedDealer == null) {
            showAlert(AlertType.ERROR, "Error", "No dealer selected.");
            return;
        }

        if (orderDistance <= 0) {
            showAlert(AlertType.ERROR, "Error", "Please select a distance greater than 0.");
            return;
        }

        if (orderProducts == null || orderProducts.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "No products added to the order.");
            return;
        }

        // Create the order using the selected dealer
        int orderId = App.getOrdersHistory().size() + 1; // Generate a new order ID
        String creationDateTime = getCurrentDateTime();
        String deliveryDateTime = "PENDIENTE";
        Order newOrder = new Order(orderId, selectedDealer, orderProducts, orderTotal, (int) orderDistance, creationDateTime, deliveryDateTime);

        // Add the order to history and current delivery
        App.addOrderToHistory(newOrder);
        App.addOrderToCurrentDelivery(newOrder);

        // Remove the selected dealer from the ComboBox options
        dealerComboBox.getItems().remove(selectedDealer);

        // Reset variables and UI
        selectedDealer = null;
        orderDistance = 0;
        orderTotal = 0;
        selectedProduct = null;
        orderProducts = null;
        refreshOrderProductsTable();
        orderTotalLabel.setText("Total: Q0.00");
        sliderDistance.setValue(0);
        distanceLabel.setText("0.00");

        // Clear the selection in the ComboBox
        dealerComboBox.getSelectionModel().clearSelection();

        // Show success message
        showAlert(AlertType.INFORMATION, "Success", "Order created successfully.");
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        return now.format(formatter);
    }

    @FXML
    public void onAddSelectedProduct(ActionEvent event) throws IOException {
        if (selectedProduct == null) {
            showAlert(AlertType.ERROR, "Error", "No product selected.");
            return;
        }

        if (orderProducts == null) {
            orderProducts = new ArrayList<>();
        }

        orderProducts.add(selectedProduct);
        System.out.println("Selected product added to order: " + selectedProduct.getName());

        // Update the order total
        orderTotal += selectedProduct.getPrice();
        orderTotalLabel.setText("Total: Q" + orderTotal);

        refreshOrderProductsTable();
    }

    @FXML
    public void onAddProduct(ActionEvent event) {
        String name = productNameInput.getText();
        String priceText = productPriceInput.getText();

        // Validate input fields
        if (name.isEmpty() || priceText.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        // Parse the price input
        int price;
        try {
            price = Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid price value.");
            return;
        }

        // Add the product
        App.addProduct(name, price);
        System.out.println("Product added: " + name + ", " + price);

        // Clear input fields
        productNameInput.clear();
        productPriceInput.clear();

        // Show success alert
        showAlert(AlertType.INFORMATION, "Success", "Product added successfully.");
        refreshTable();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTable() {
        productsTable.getItems().clear();
        productsTable.getItems().addAll(App.getProductList());
        
        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProduct = newSelection;
            }
        });
    }
    
    private void refreshOrderProductsTable() {
        if (orderProducts == null) {
            orderProductsTable.getItems().clear();
        } else {
            ObservableList<Product> orderProductsList = FXCollections.observableArrayList(orderProducts);
            orderProductsTable.setItems(orderProductsList);
        }
    }
}
