module com.main.javafxapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.main.javafxapp to javafx.fxml;
    exports com.main.javafxapp;
    exports com.main.javafxapp.Controllers;
    exports com.main.javafxapp.Models;
    opens com.main.javafxapp.Models to javafx.fxml, javafx.base;
    opens com.main.javafxapp.Controllers to javafx.fxml;
}