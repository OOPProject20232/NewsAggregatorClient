module org.example.newsaggregatorclient {
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
    requires annotations;
    requires org.json;
    requires com.fasterxml.jackson.databind;

    opens org.example.newsaggregatorclient to javafx.fxml;
    exports org.example.newsaggregatorclient;
}