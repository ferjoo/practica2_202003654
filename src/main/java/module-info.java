module com.ipc1.practica2_202003654 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.ipc1.practica2_202003654 to javafx.fxml;
    exports com.ipc1.practica2_202003654;
    
    opens models to javafx.base;
}
