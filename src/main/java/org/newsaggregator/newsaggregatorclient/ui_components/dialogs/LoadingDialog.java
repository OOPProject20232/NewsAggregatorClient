package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

public class LoadingDialog extends GenericDialog{
    /**
     * Dialog hiển thị thông báo "Đang tải dữ liệu"
     * Được gọi khi ứng dụng đang tải dữ liệu từ server
     */
    public LoadingDialog() {
        this.setTitle("Đang tải dữ liệu");
        this.setHeaderText("Vui lòng chờ trong giây lát");
        this.getDialogPane().getButtonTypes().clear();
    }
}
