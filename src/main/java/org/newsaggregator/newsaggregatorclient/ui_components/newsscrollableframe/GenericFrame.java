package org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * <p>Định nghĩa khung xem dữ liệu cho từng loại hình tin tức</p>
 * <p>Bao gồm GridPane để thêm các CategoryTitledPane,
 * cùng với số hiệu trang hiện tại (do CSDL có thực hiện phân trang theo giới hạn tải)
 * và giới hạn tải tin mặc định cho mỗi category</p>
 */
public class GenericFrame extends ScrollPane {
    protected final GridPane itemsContainer = new GridPane();
    protected int currentPage = 1;
    protected int limit = 30;
    protected HostServices hostServices;
    public GenericFrame(){
        AnchorPane container = new AnchorPane();
        this.setContent(container);
        container.getChildren().add(itemsContainer);
        AnchorPane.setBottomAnchor(itemsContainer, 0.0);
        AnchorPane.setTopAnchor(itemsContainer, 0.0);
        AnchorPane.setLeftAnchor(itemsContainer, 0.0);
        AnchorPane.setRightAnchor(itemsContainer, 0.0);
        setFitToWidth(true);
        setFitToHeight(true);
        itemsContainer.getChildren().clear();
        itemsContainer.getStyleClass().addAll("generic-transparent-container");
        itemsContainer.setHgap(36);
        itemsContainer.setVgap(36);
        itemsContainer.setPadding(new Insets(12, 48, 48, 0));
//        GridPane.setMargin(itemsContainer, new Insets(0,0,0,48));
        itemsContainer.setAlignment(Pos.BASELINE_LEFT);
        itemsContainer.setMaxSize(BASELINE_OFFSET_SAME_AS_HEIGHT, Region.BASELINE_OFFSET_SAME_AS_HEIGHT);
//        itemsContainer.setGridLinesVisible(true);
        container.getStyleClass().addAll( "generic-container");
        this.getStyleClass().addAll("generic-container");
    }

    /**
     * Chuyển sang trang tiếp theo
     * Sau khi chuyển tr
     */
    public void resetPage() {
        currentPage = 1;
    }

    public void nextPage() {
        currentPage+=1;
    }

    public void previousPage() {
        currentPage-=1;
    }
}
