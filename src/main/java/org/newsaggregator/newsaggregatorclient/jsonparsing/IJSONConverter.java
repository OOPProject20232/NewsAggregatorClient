package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

public interface IJSONConverter<D extends GenericData> {
    /**
     * Interface này chứa các hàm để chuyển đổi dữ liệu từ JSON sang dạng khác
     */
    public default D convert() {
        return null;
    }
}
