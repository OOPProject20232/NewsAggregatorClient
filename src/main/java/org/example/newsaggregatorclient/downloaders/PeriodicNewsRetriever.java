package org.example.newsaggregatorclient.downloaders;

public class PeriodicNewsRetriever extends NewsRetriever{
    /**
     * Class này chứa các hàm để lấy tin tự động theo chu kỳ
     */
    private final int periodBySeconds = 3600;

    public PeriodicNewsRetriever() {
        super();
    }

    public void startRetrieving() {
        /**
         * Hàm này sẽ gửi request đến server sau mỗi period giây
         */
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    sendRequest();
                    Thread.sleep(periodBySeconds * 1000);
                } catch (Exception e) {
                    System.out.println("Error sending request: " + e.getMessage());
                }
            }
        });
        thread.start();
    }

}
