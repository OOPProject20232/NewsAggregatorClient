package org.newsaggregator.newsaggregatorclient;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.newsaggregator.newsaggregatorclient.jsonparsing.SearchJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;

public class NewsSearchController{
    /**
     * Controller của tab tìm kiếm tin tức
     */
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ToggleButton articleToggleButton;

    @FXML
    private ToggleButton redditToggleButton;

    @FXML
    private ComboBox<String> searchFieldComboBox;

    @FXML
    private ComboBox<String> searchOrderComboBox;

    private String searchType;
    private String searchOrder;
    private String searchField;


    public void initialize() {
        /**
         * Khởi tạo các giá trị mặc định
         * - Disable nút tìm kiếm khi ô tìm kiếm trống hoặc chỉ chứa khoảng trắng
         * - Xử lý sự kiện tìm kiếm khi nhấn Enter hoặc click vào nút tìm kiếm
         * - Focus vào ô tìm kiếm
         */
        SearchService searchService = new SearchService();
        searchService.restart();
        searchButton.setDisable(true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchButton.setDisable(newValue.trim().isEmpty());
        });
        searchTextField.setOnKeyPressed(event -> {
            autocomplete();
        });
        searchTextField.setOnAction(event -> {
            search();
        });
        searchButton.setOnAction(event -> search());
        searchTextField.requestFocus();
        searchFieldComboBox.getItems().clear();
        searchOrderComboBox.getItems().clear();
        searchFieldComboBox.getItems().addAll("all", "categories");
        searchOrderComboBox.getItems().addAll("Newest", "Oldest");
    }

    private void search() {
        // Hàm xử lý sự kiện tìm kiếm tin tức
        System.out.println("Searching for: " + searchTextField.getText());
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        Platform.runLater(() -> {
            SearchJSONLoader searchJSONLoader = new SearchJSONLoader(searchTextField.getText(), "articles", "desc", "r");
            searchJSONLoader.loadJSON();
            System.out.println(searchJSONLoader);
            loadingDialog.close();
        });
    }

    private void autocomplete() {
        // Hàm xử lý sự kiện autocomplete
        System.out.println("Autocompleting for: " + searchTextField.getText());
    }

    private static class SearchService extends Service<Void> {
        /**
         * Service xử lý tìm kiếm tin tức
         */
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Xử lý tìm kiếm tin tức
                    return null;
                }
            };
        }
    }

    public void insertSearchText(String searchKeyword){
        searchTextField.setText(searchKeyword);
        search();
    }
}
