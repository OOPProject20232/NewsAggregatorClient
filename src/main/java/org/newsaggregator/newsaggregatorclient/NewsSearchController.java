package org.newsaggregator.newsaggregatorclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewsSearchController {
    /**
     * Controller của tab tìm kiếm tin tức
     */
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;


    public void initialize() {
        // Khởi tạo các giá trị mặc định
        searchButton.setDisable(true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchButton.setDisable(newValue.trim().isEmpty());
        });
        searchTextField.setOnAction(event -> search());
        searchButton.setOnAction(event -> search());
        searchTextField.requestFocus();
    }

    private void search() {
        // Hàm xử lý sự kiện tìm kiếm tin tức
        System.out.println("Searching for: " + searchTextField.getText());
    }
}
