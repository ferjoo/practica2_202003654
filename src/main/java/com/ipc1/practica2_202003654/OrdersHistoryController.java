/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ipc1.practica2_202003654;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Order;

/**
 * FXML Controller class
 *
 * @author ferjo
 */
public class OrdersHistoryController implements Initializable {
    
    @FXML
    private TableView<Order> ordersHistoryTable;
    @FXML
    private TableColumn<Order, String> idColumn;
    @FXML
    private TableColumn<Order, String> dealerColumn;
    @FXML
    private TableColumn<Order, String> orderTotalColumn;
    @FXML
    private TableColumn<Order, String> creationDateTimeColumn;
    @FXML
    private TableColumn<Order, String> deliveryDateTimeColumn;

    /**
     * Initializes the controller class.
        */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
       idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
       dealerColumn.setCellValueFactory(new PropertyValueFactory<>("dealer"));
       orderTotalColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
       creationDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("creationDateTime"));
       deliveryDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDateTime"));

       List<Order> ordersHistory = App.getOrdersHistory();
       ordersHistoryTable.getItems().addAll(ordersHistory);
   } 
    
            
    @FXML
    public void onBack(ActionEvent event) throws IOException {
        App.setRoot("mainMenu");
    }
    
}
