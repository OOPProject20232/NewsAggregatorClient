package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.SearchJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.HorizontalDataCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.SearchTitledPane;

import java.util.List;

public class SearchItemsLoader<T extends HorizontalDataCard<D>, D extends GenericData>{
    /*
     * Load JSON file for search
     */
    SearchJSONLoader searchJSONLoader;
    SearchTitledPane searchTitledPane;
    HostServices hostServices;
    int limit = 10;

    public SearchItemsLoader(String searchQuery, String searchType, String isDesc, String isExactOrRegex, SearchTitledPane searchTitledPane) {
        SearchJSONLoader searchJSONLoader = new SearchJSONLoader(searchQuery, searchType, isDesc, isExactOrRegex);
        searchJSONLoader.loadJSON();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void loadItems(List<D> data) {
        D genericData = data.get(0);
        if (genericData instanceof NewsItemData) {
            ArticleItemsLoader<SearchTitledPane> articleItemsLoader = new ArticleItemsLoader<>(limit, 0, null, searchTitledPane, null);
            articleItemsLoader.loadItems((List<NewsItemData>) data);
        }
        if (genericData instanceof RedditPostData){
            RedditItemsLoader<SearchTitledPane> redditItemsLoader = new RedditItemsLoader<>(limit, 0, null, searchTitledPane);
            redditItemsLoader.loadItems((List<RedditPostData>) data);
        }
    }
}
