package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

import java.util.List;

/**
 * <p>Một interface cho các lớp tải dữ liệu lên UI</p>
 *
 * @param <T>
 */
public interface ItemsLoader<T extends GenericData> {
    public void loadItems(List<T> data);
}
