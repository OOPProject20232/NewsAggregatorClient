package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.SearchJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.HorizontalDataCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;

import java.util.List;

public class SearchItemsLoader<T, D extends GenericData>{
    /*
     * Load JSON file for search
     */
    SearchJSONLoader searchJSONLoader;
    T searchTitledPane;
    HostServices hostServices;
    int limit = 10;
    int page = 1;

    public SearchItemsLoader(String searchQuery, String searchType, String searchField, String isDesc, String isExactOrRegex, T container) {
        searchJSONLoader = new SearchJSONLoader(searchQuery, searchType, searchField, isDesc, isExactOrRegex);
    }

    public void loadJSON(){
        searchJSONLoader.setPage(page);
        searchJSONLoader.setLimit(limit);
        searchJSONLoader.loadJSON();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void loadItems(List<D> data) {
        D genericData = data.get(0);
        if (genericData instanceof NewsItemData) {
            ArticleItemsLoader<T> articleItemsLoader = new ArticleItemsLoader<>(limit, 0, null, searchTitledPane, null);
            articleItemsLoader.loadItems((List<NewsItemData>) data);
        }
        if (genericData instanceof RedditPostData){
            RedditItemsLoader<T> redditItemsLoader = new RedditItemsLoader<>(limit, 0, null, searchTitledPane);
            redditItemsLoader.loadItems((List<RedditPostData>) data);
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
