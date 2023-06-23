/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ipc1.practica2_202003654;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import models.Order;

/**
 * FXML Controller class
 *
 * @author ferjo
 */
public class DeliveriesViewController implements Initializable {
    @FXML
    private ImageView dealer1bike;

    @FXML
    private ImageView dealer2bike;

    @FXML
    private ImageView dealer3bike;
    
    @FXML
    private Label dealer1label;
    
    @FXML
    private Label dealer2label;
    
    @FXML
    private Label dealer3label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Order> currentDeliveryOrders = App.getCurrentDeliveryOrders();

        for (Order order : currentDeliveryOrders) {
            if (order.getDealer().equals("Repartidor 1")) {
                dealer1label.setText("Distancia: " + order.getDistance() + " km");
            } else if (order.getDealer().equals("Repartidor 2")) {
                dealer2label.setText("Distancia: " + order.getDistance() + " km");
            } else if (order.getDealer().equals("Repartidor 3")) {
                dealer3label.setText("Distancia: " + order.getDistance() + " km");
            }
        }
    } 
    
    @FXML
    public void onBack(ActionEvent event) throws IOException {
        App.setRoot("mainMenu");
    }
    
    @FXML
    public void onSendDriver1(ActionEvent event) throws IOException {
        int orderId = getCurrentOrderIdByDealer("Repartidor 1");
        if (orderId != -1) {
            App.sendOrder(orderId);
        }
    }

    @FXML
    public void onSendDriver2(ActionEvent event) throws IOException {
        int orderId = getCurrentOrderIdByDealer("Repartidor 2");
        if (orderId != -1) {
            App.sendOrder(orderId);
        } 
    }

    @FXML
    public void onSendDriver3(ActionEvent event) throws IOException {
        int orderId = getCurrentOrderIdByDealer("Repartidor 3");
        if (orderId != -1) {
            App.sendOrder(orderId);
        } 
    }
    
    
    @FXML
    public void onSendAll(ActionEvent event) throws IOException {
        int orderId2 = getCurrentOrderIdByDealer("Repartidor 2");
        if (orderId2 != -1) {
            App.sendOrder(orderId2);
        } 

        int orderId3 = getCurrentOrderIdByDealer("Repartidor 3");
        if (orderId3 != -1) {
            App.sendOrder(orderId3);
        } 
        
        int orderId = getCurrentOrderIdByDealer("Repartidor 1");
        if (orderId != -1) {
            App.sendOrder(orderId);
        }
    }
    

    private int getCurrentOrderIdByDealer(String dealerName) {
        List<Order> currentDeliveryOrders = App.getCurrentDeliveryOrders();
        for (Order order : currentDeliveryOrders) {
            if (order.getDealer().equals(dealerName)) {
                return order.getId();
            }
        }
        return -1; // Return -1 if no order found for the given dealer name
    }
    
    

    @FXML
    public void moveDealerImage(int orderId, double distance) {
        ImageView dealerImage = null;
        System.out.println("orderId: "+orderId);

        // Find the dealer image based on the order ID
        if (orderId == 1) {
            dealerImage = dealer1bike;
        } else if (orderId == 2) {
            dealerImage = dealer2bike;
        } else if (orderId == 3) {
            dealerImage = dealer3bike;
        }

        if (dealerImage != null) {
            double paneWidth = dealerImage.getParent().getBoundsInLocal().getWidth();
            double travelDistance = paneWidth * (distance / 100); // Adjust the scaling factor as needed

            // Create a translate transition to move the dealer image
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), dealerImage);
            translateTransition.setToX(travelDistance);
            translateTransition.play();
        }
    }
}
