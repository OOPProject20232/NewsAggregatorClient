package org.newsaggregator.newsaggregatorclient.ui_component.uiloader;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;

import java.util.List;

public interface ItemsLoader<T extends GenericData> {
    public NewsCategoryGroupTitledPane loadItems(List<T> data);
}
