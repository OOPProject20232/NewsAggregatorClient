package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

public interface IGenericDataCard<T extends GenericData > {
    public void setCardStyle();
    public void setText();
    public void setImage();
}
