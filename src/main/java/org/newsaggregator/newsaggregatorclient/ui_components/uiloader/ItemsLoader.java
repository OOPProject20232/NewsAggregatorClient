package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

import java.util.List;

public interface ItemsLoader<T extends GenericData> {
    public void loadItems(List<T> data);
}
