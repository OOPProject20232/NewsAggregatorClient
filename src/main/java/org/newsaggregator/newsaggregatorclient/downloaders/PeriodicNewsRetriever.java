package org.newsaggregator.newsaggregatorclient.downloaders;

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
        thread = new Thread(() -> {
            while (true) {
                try {
                    sendRequest();
                    Thread.sleep(periodBySeconds * 1000);
                } catch (Exception e) {
                    System.out.println("Error sending request: " + e.getMessage());
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void stopRetrieving() {
        /**
         * Hàm này dùng để dừng việc gửi request định kỳ
         */
        thread.interrupt();
    }
}
