package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private String dealer;
    private List<Product> products;
    private double orderTotal;
    private double distance;
    private String creationDateTime;
    private String deliveryDateTime;

    public Order(int id, String dealer, List<Product> products, double orderTotal, double distance, String creationDateTime, String deliveryDateTime) {
        this.id = id;
        this.dealer = dealer;
        this.products = new ArrayList<>(products);
        this.orderTotal = orderTotal;
        this.distance = distance;
        this.creationDateTime = creationDateTime;
        this.deliveryDateTime = deliveryDateTime;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }
}
