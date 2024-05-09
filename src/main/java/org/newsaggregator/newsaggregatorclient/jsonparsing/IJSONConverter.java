package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

@Deprecated
public abstract class IJSONConverter<D extends GenericData> {
    /**
     * Interface này chứa các hàm để chuyển đổi dữ liệu từ JSON sang dạng khác
     */
    public D convert(){
        return (D) null;
    }
}
