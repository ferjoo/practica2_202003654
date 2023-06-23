/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ipc1.practica2_202003654;

import models.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ferjo
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }    

    
    @FXML
    public void onOpenOrders(ActionEvent event) throws IOException {
        App.setRoot("ordersView");
    }
    
    @FXML
    public void onOpenDeliveries(ActionEvent event) throws IOException {
        App.setRoot("deliveriesView");
    }
    
    @FXML
    public void onOpenAddProduct(ActionEvent event) throws IOException {
        App.setRoot("addProduct");
    }
    
    @FXML
    public void onOpenHistory(ActionEvent event) throws IOException {
        App.setRoot("ordersHistory");
    }
}
