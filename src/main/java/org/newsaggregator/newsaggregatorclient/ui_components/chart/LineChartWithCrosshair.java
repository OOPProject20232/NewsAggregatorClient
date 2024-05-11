package org.newsaggregator.newsaggregatorclient.ui_components.chart;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.security.SecureRandom;

public class LineChartWithCrosshair<X,Y> extends LineChart<X,Y> {
    /**
     * LineChart với crosshair
     * @param xAxis
     * @param yAxis
     */
    BooleanProperty crosshairVisible = new SimpleBooleanProperty();
//    BooleanProperty crosshairSnapToData = new SimpleBooleanProperty();
    Line xLine = new Line();
    Line yLine = new Line();

    public LineChartWithCrosshair(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);
        xLine.getStyleClass().add("crosshair");
        yLine.getStyleClass().add("crosshair");
        xLine.endXProperty().bind(xLine.startXProperty());
        yLine.endYProperty().bind(yLine.startYProperty());
        yLine.visibleProperty().bind(crosshairVisible);
        xLine.visibleProperty().bind(crosshairVisible);
        setOnMouseExited(event -> {
            crosshairVisible.set(false);
        });
        setOnMouseMoved(event -> {
           if (crosshairVisible.get()){
               Bounds plotAreaBounds = getBoundsInParent();
               double mouseX = event.getX();
               double mouseY = event.getY();
               if (plotAreaBounds.contains(mouseX, mouseY)){
                   double x = (double) getXAxis().getValueForDisplay(mouseX);
                   double y = (double) getYAxis().getValueForDisplay(mouseY);
                   xLine.setStartX(event.getX() - plotAreaBounds.getMinX());
                   yLine.setStartY(event.getY() - plotAreaBounds.getMinY());
               }
           }
        });
    }
}