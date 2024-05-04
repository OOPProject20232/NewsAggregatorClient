package org.newsaggregator.newsaggregatorclient.downloaders;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicNewsRetriever extends NewsRetriever{
    /**
     * Class này chứa các hàm để lấy tin tự động theo chu kỳ
     */
    private final int periodBySeconds = 3600;
    private Thread thread;

    public PeriodicNewsRetriever() {
        super();
    }

    public void startRetrieving() {
        /**
         * Hàm này sẽ gửi request đến server sau mỗi period giây
         */
        setLimit(50);
        setPageNumber(1);
//        thread = new Thread(() -> {
//            while (true) {
//                try {
//                    sendRequest();
//                    Thread.sleep(periodBySeconds * 1000);
//                } catch (Exception e) {
//                    System.out.println("Error sending request: " + e.getMessage());
//                }
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
        try (ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor()) {
            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendRequest("articles", true, "news.json");
                    } catch (Exception e) {
                        System.out.println("Error sending request: " + e.getMessage());
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    public void stopRetrieving() {
        /**
         * Hàm này dùng để dừng việc gửi request định kỳ
         */
        thread.interrupt();
    }
}
