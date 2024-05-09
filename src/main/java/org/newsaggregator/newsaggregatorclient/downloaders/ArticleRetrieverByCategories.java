package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

@Deprecated
public class ArticleRetrieverByCategories extends NewsRetriever implements ICacheDownloader {

    @Override
    public int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        /**
         * Class này chứa các hàm để gửi request đến server
         *
         */
        return 0;
    }
}
