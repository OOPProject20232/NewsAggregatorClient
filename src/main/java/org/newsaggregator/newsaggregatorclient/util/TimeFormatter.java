package org.newsaggregator.newsaggregatorclient.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeFormatter {
    public static String processDateTime(String dateTime){
        /**
         * Xử lý chuỗi ngày tháng
         * Nếu thời gian đăng chưa quá 1 ngày, hiển thị bài đăng cách đây bao nhiêu giờ
         * Nếu thời gian đăng chưa quá 1 tuần, hiển thị bài đăng cách đây bao nhiêu ngày
         * Còn lại hiển thị ngày tháng năm
         */
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime published = LocalDateTime.parse(dateTime, formatter);
        long diff = Math.abs(now.toEpochSecond(ZoneOffset.UTC) - published.toEpochSecond(ZoneOffset.UTC));
        System.out.println("Diff: " + diff);
        if (diff < 60){
            return "Just now";
        }
        else if (diff < 120){
            return "1 minute ago";
        }
        else if (diff < 3600){
            return diff/60 + " minutes ago";
        }
        else
        if (diff < 86400){
            return diff/3600 + " hours ago";
        }
        else if (diff < 172800){
            return "1 day ago";
        }
        else if (diff < 604800){
            return diff/86400 + " days ago";
        }
        else {
            SimpleDateFormat inp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = inp.parse(dateTime);
                return out.format(date);
            }
            catch (Exception e){
                return "Unknown";
            }
        }
    }
}
