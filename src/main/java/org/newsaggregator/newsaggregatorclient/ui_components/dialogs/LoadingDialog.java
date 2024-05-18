package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;

import java.util.Objects;

public class LoadingDialog extends GenericAlert {
    /**
     * Dialog hiển thị thông báo "Đang tải dữ liệu"
     * Được gọi khi ứng dụng đang tải dữ liệu từ server
     */
    public LoadingDialog() {
        super(AlertType.NONE, "Đang tải dữ liệu", "Vui lòng chờ trong giây lát", "Đang tải dữ liệu từ máy chủ...");
        this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
//        this.getDialogPane().getStyleClass().clear();
//        this.getDialogPane().getStyleClass().add("undecorated-dialog");
//        this.initModality(Modality.NONE);
//        this.setTitle("Đang tải dữ liệu");
//        this.setHeaderText("Vui lòng chờ trong giây lát");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setIconified(false);
        try{
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResource("refresh.png").toExternalForm())));
//            getDialogPane().getStylesheets().add(NewsAggregatorClientController.class.getResourceAsStream("/assets/css/dialogs.css").t);
//            getDialogPane().getStylesheets().add(this.getClass().getResource("dialogs.css").toExternalForm());
        }
        catch (NullPointerException e){
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResource("assets/images/refresh.png").toExternalForm())));
//            getDialogPane().getStylesheets().add(this.getClass().getResource("../../assets/css/dialogs.css").toExternalForm());

        }
    }
}
