package org.newsaggregator.newsaggregatorclient;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DataReaderFromISTest {
    @Test
    public void testFetchJSON() throws MalformedURLException {
        // Benchmark
        // Test case 1: testFetchJSON
        Long startTime = System.currentTimeMillis();
        DataReaderFromIS dataReaderFromIS = new DataReaderFromIS();
        JSONObject jsonObject = dataReaderFromIS.fetchJSON("https://newsaggregator-mern.onrender.com/v1/articles?page=200&limit=20");
        System.out.println(jsonObject);
        assertNotNull(jsonObject);
        Long ellapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Ellapsed time: " + ellapsedTime + "ms");
    }
}
