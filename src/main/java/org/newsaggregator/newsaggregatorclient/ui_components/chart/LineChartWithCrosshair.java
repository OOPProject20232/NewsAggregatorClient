package org.newsaggregator.newsaggregatorclient.ui_components.chart;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.shape.Line;

public class LineChartWithCrosshair<X, Y> extends LineChart {
    /**
     * Custom LineChart for showing crosshair lines on mouse hover.
     * Based this StackOverflow answer: https://stackoverflow.com/a/56230124
     */

    private Line vLine;
//    private Line hLine;
    private Group plotArea;
    private BooleanProperty showFlag = new SimpleBooleanProperty();
    private BooleanProperty showCrossHair = new SimpleBooleanProperty();
    private double tickSize = 5;

    public LineChartWithCrosshair(Axis<X> xAxis, Axis<Y> yAxis, CustomCursor customCursor) {
        super(xAxis, yAxis);
        vLine = customCursor.getvLine();
//        hLine = customCursor.gethLine();
        showCrossHair.set(customCursor.isEstUtilisé());
        customCursor.isUsedProperty().addListener((obs, old, show) -> showCrossHair.set(show));

//        hLine.endYProperty().bind(hLine.startYProperty());
        vLine.endXProperty().bind(vLine.startXProperty());
        vLine.visibleProperty().bind(showFlag);
//        hLine.visibleProperty().bind(showFlag);
        setOnMouseExited(e -> showFlag.set(false));
        setOnMouseMoved(e -> {
            if (isShowCrossHair() && plotArea != null) {
                Bounds b = plotArea.getBoundsInLocal();
                // If the mouse cursor is within the plot area bounds
                if (b.getMinX() < e.getX() && e.getX() < b.getMaxX() && b.getMinY() < e.getY() && e.getY() < b.getMaxY()) {
                    showFlag.set(true);
                    moveCrossHair(e.getX() - b.getMinX() - tickSize, e.getY() - b.getMinY() - tickSize);
                } else {
                    showFlag.set(false);
                }
            }
        });
    }

    private void moveCrossHair(double x, double y) {
        vLine.setStartX(x);
//        hLine.setStartY(y);
    }

    public boolean isShowCrossHair() {
        return showCrossHair.get();
    }

    public BooleanProperty showCrossHairProperty() {
        return showCrossHair;
    }

    public void setShowCrossHair(boolean showCrossHair) {
        this.showCrossHair.set(showCrossHair);
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        if (plotArea == null && !getPlotChildren().isEmpty()) {
            Group plotContent = (Group) ((Node) getPlotChildren().get(0)).getParent();
            plotArea = (Group) plotContent.getParent();
        }
        if (!getPlotChildren().contains(vLine)) {
            getPlotChildren().add(vLine);
//            getPlotChildren().addAll(vLine, hLine);
        }
//        hLine.setStartX(0);
//        hLine.setEndX(getBoundsInLocal().getWidth());

        vLine.setStartY(0);
        vLine.setEndY(getBoundsInLocal().getHeight());
    }

}