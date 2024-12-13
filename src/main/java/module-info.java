module org.example.vmst_finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.vmst_finalproject to javafx.fxml;
    exports org.example.vmst_finalproject;
    exports org.example.vmst_finalproject.models;
    opens org.example.vmst_finalproject.models to javafx.fxml;
}