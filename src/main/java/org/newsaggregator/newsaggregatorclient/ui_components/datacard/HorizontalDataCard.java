package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

/**
 * Class HorizontalDataCard chứa các thành phần của một card hiển thị thông tin của một bài viết
 * kế thừa từ HBox
 * @see HBox
 * Sử dụng 1 lớp mở rộng của GenericData để lấy thông tin của bài viết
 * @see GenericData
 */
public abstract class HorizontalDataCard<T extends GenericData> extends HBox {
    public static String STYLESHEET = "org/newsaggregator/newsaggregatorclient/assets/css/datacard.css";
    public static int SPACING = 12;
    public void setCardStyle(){
        this.getStyleClass().add("datacard");
        VBox.setVgrow(this, Priority.ALWAYS);
    }
    public abstract void setText();

    public abstract void setImage();
}
