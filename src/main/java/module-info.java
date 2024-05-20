module org.newsaggregator.newsaggregatorclient {
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
    requires java.desktop;
    requires javafx.swing;
    requires commons.io;
    requires com.fasterxml.jackson.annotation;
    requires java.sql;
    requires org.jsoup;

    opens org.newsaggregator.newsaggregatorclient to javafx.fxml;
    exports org.newsaggregator.newsaggregatorclient;
    exports org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;
    opens org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe to javafx.fxml;
    opens org.newsaggregator.newsaggregatorclient.ui_components.dialogs to javafx.fxml;
    exports org.newsaggregator.newsaggregatorclient.ui_components.dialogs;
}