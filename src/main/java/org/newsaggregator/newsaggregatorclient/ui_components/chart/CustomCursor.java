package org.newsaggregator.newsaggregatorclient.ui_components.chart;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Line;

public class CustomCursor {
    /*
        * CustomCursor class for showing crosshair lines on mouse hover.
        * Based this StackOverflow answer: https://stackoverflow.com/a/56230124
     */
    private Line vLine;
    private Line hLine;
    private BooleanProperty isUsed = new SimpleBooleanProperty();

    public CustomCursor(Line vLine, Line hLine, boolean isUsed) {
        this.vLine = vLine;
        this.hLine = hLine;
//        this.isUsed.set(isUsed);
    }

    public Line getvLine() {
        return vLine;
    }

    public Line gethLine() {
        return hLine;
    }

//    public boolean isUsed() {
//        return isUsed.get();
//    }

//    public void setIsUsed(boolean estUtilisé) {
//        this.isUsed.set(estUtilisé);
//    }

    public BooleanProperty isUsedProperty() {
        return isUsed;
    }
}
