module com.example.newprojectbss {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.newprojectbss to javafx.fxml;
    opens com.example.newprojectbss.model to javafx.base;  // <<< aqui

    exports com.example.newprojectbss;
    exports com.example.newprojectbss.model;
}
