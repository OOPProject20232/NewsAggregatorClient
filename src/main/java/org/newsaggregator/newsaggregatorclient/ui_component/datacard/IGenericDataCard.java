package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

public interface IGenericDataCard<T extends GenericData > {
    public void setCardStyle();
    public void setText();
    public void setImage();
}
