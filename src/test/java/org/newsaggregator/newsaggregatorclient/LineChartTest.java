package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.ui_components.chart.LineChartWithCrosshair;

import javax.sound.sampled.Line;

public class LineChartTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        LineChartWithCrosshair<String, Number> lineChart = new LineChartWithCrosshair<>(null, null);
        anchorPane.getChildren().add(lineChart);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);
        Scene scene = new Scene(anchorPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
