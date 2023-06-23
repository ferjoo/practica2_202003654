package com.ipc1.practica2_202003654;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Order;
import models.Product;

/**
 * JavaFX App
 */
public class App extends Application {


    private static Scene scene;
    private static List<Product> productList;
    private static List<Order> ordersHistory;
    private static List<Order> currentDeliveryOrders;
    private static final String PRODUCT_FILE = "products.bin";
    private static final String ORDERS_FILE = "orders.bin";

    @Override
    public void start(Stage stage) throws IOException {
        productList = loadProductList();
        ordersHistory = loadOrdersHistory();
        currentDeliveryOrders = new ArrayList<>(); // Initialize the currentDeliveryOrders list

        FXMLLoader loader = new FXMLLoader(App.class.getResource("deliveriesView.fxml"));
        Parent deliveriesView = loader.load();
        DeliveriesViewController deliveriesController = loader.getController();
        scene = new Scene(deliveriesView, 1000, 700);
        scene.setUserData(deliveriesController); // Set the userData property

        scene = new Scene(loadFXML("mainMenu"), 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void initializeDefaultProducts() {
        productList = new ArrayList<>(); // Initialize the productList
        productList.add(new Product(1, "Hamburguesa con carne", 50));
        productList.add(new Product(2, "Gaseosa", 5.50));
        productList.add(new Product(3, "Porcion de pastel", 8.99));
        productList.add(new Product(4, "Aros de cebolla", 13.99));
        saveProductList(); // Save the productList to a binary file
    }

    public static List<Product> getProductList() {
        if (productList == null) {
            productList = loadProductList();
        }
        return productList;
    }

    public static void addProduct(String name, int price) {
        int id = productList.size() + 1;
        Product product = new Product(id, name, price);
        productList.add(product);
        saveProductList(); // Save the updated productList to a binary file
    }

    private static void saveProductList() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            outputStream.writeObject(productList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Product> loadProductList() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRODUCT_FILE))) {
            return (List<Product>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or an error occurs during deserialization, return an empty list
            return new ArrayList<>();
        }
    }

    public static List<Order> getOrdersHistory() {
        if (ordersHistory == null) {
            ordersHistory = loadOrdersHistory();
        }
        return ordersHistory;
    }

    public static void addOrderToHistory(Order order) {
        ordersHistory.add(order);
        saveOrdersHistory(); // Save the updated ordersHistory to a binary file
    }

    public static void updateOrderById(int id, Order updatedOrder) {
        for (int i = 0; i < ordersHistory.size(); i++) {
            Order order = ordersHistory.get(i);
            if (order.getId() == id) {
                ordersHistory.set(i, updatedOrder);
                saveOrdersHistory(); // Save the updated ordersHistory to a binary file
                break;
            }
        }
    }

    private static void saveOrdersHistory() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            outputStream.writeObject(ordersHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Order> loadOrdersHistory() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
            return (List<Order>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or an error occurs during deserialization, return an empty list
            return new ArrayList<>();
        }
    }
    
    public static List<Order> getCurrentDeliveryOrders() {
        return currentDeliveryOrders;
    }
    
    public static void addOrderToCurrentDelivery(Order order) {
        currentDeliveryOrders.add(order);
    }
    
    public static void updateCurrentOrderById(int id, Order updatedOrder) {
        for (int i = 0; i < currentDeliveryOrders.size(); i++) {
            Order order = currentDeliveryOrders.get(i);
            if (order.getId() == id) {
                currentDeliveryOrders.set(i, updatedOrder);
                break;
            }
        }
    }
    
    public static void sendOrder(int orderId) {
        Order orderToDeliver = null;

        // Check if the order ID is already being processed in a thread
        if (isOrderBeingProcessed(orderId)) {
            System.out.println("Order with ID " + orderId + " is already being processed.");
            return;
        }

        // Find the order in the current delivery orders list
        for (Order order : currentDeliveryOrders) {
            if (order.getId() == orderId) {
                orderToDeliver = order;
                break;
            }
        }

        // Check if the order exists
        if (orderToDeliver == null) {
            System.out.println("Order with ID " + orderId + " does not exist.");
            return;
        }

        final Order finalOrderToDeliver = orderToDeliver; // Create a final copy of orderToDeliver

        double distance = finalOrderToDeliver.getDistance();
        double deliveryTimeInSeconds = distance * 10;

        Pane dealerroad;
        ImageView dealerbike;

        // Determine the appropriate dealer road and bike based on the dealer's name
        if (finalOrderToDeliver.getDealer().equals("Repartidor 1")) {
            dealerroad = (Pane) scene.lookup("#dealer1road");
            dealerbike = (ImageView) scene.lookup("#dealer1bike");
        } else if (finalOrderToDeliver.getDealer().equals("Repartidor 2")) {
            dealerroad = (Pane) scene.lookup("#dealer2road");
            dealerbike = (ImageView) scene.lookup("#dealer2bike");
        } else if (finalOrderToDeliver.getDealer().equals("Repartidor 3")) {
            dealerroad = (Pane) scene.lookup("#dealer3road");
            dealerbike = (ImageView) scene.lookup("#dealer3bike");
        } else {
            System.out.println("Invalid dealer name: " + finalOrderToDeliver.getDealer());
            return;
        }

        double paneWidth = dealerroad.getWidth();
        double movementDistancePerSecond = paneWidth / deliveryTimeInSeconds;

        double initialPositionX = dealerbike.getTranslateX();
        double targetPositionX = paneWidth - dealerbike.getFitWidth();

        // Create a translate transition to move the bike image
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(deliveryTimeInSeconds), dealerbike);
        translateTransition.setFromX(initialPositionX);
        translateTransition.setToX(targetPositionX);

        // Play the translate transition
        translateTransition.play();

        // Simulate delivery time using a thread
        Thread deliveryThread;
            deliveryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) (deliveryTimeInSeconds * 1000L));
                    System.out.println("Order with ID " + orderId + " delivered successfully.");

                    // Create a translate transition for the return animation
                    TranslateTransition returnTransition = new TranslateTransition(Duration.seconds(deliveryTimeInSeconds), dealerbike);
                    returnTransition.setToX(initialPositionX);

                    // Play the return animation
                    returnTransition.play();
                    System.out.println("Dealer has returned.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Remove the order from current delivery orders
                    currentDeliveryOrders.remove(finalOrderToDeliver);

                    // Update the deliveryDateTime of the order in ordersHistory
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd:MM:yyyy");
                    String deliveryDateTime = LocalDateTime.now().format(formatter);
                    for (Order order : ordersHistory) {
                        if (order.getId() == orderId) {
                            order.setDeliveryDateTime(deliveryDateTime);
                            saveOrdersHistory();
                            break;
                        }
                    }
                }
            }
        });
        deliveryThread.start();
    }

    public static boolean isOrderBeingProcessed(int orderId) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().equals("OrderThread-" + orderId)) {
                return true;
            }
        }
        return false;
    }

}
