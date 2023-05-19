module databaseProjectExample {
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.controls;
    requires javafx.fxml;

    opens service to javafx.fxml;
    opens controller to javafx.fxml;
    opens models to javafx.base;
    
    exports service;
    exports controller;
    exports models;
}
