module com.example.newprojectbss {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.newprojectbss to javafx.fxml;
    exports com.example.newprojectbss;
}