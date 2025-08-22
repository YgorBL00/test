module com.example.newprojectbss {
    requires javafx.controls;
    requires javafx.fxml;
    requires kernel;
    requires layout;
    requires java.desktop;

    opens com.example.newprojectbss.model to javafx.base;  // <<< aqui

    exports com.example.newprojectbss;
    exports com.example.newprojectbss.model;
    opens com.example.newprojectbss to javafx.base, javafx.fxml;
}
