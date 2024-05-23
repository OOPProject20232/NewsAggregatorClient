package org.newsaggregator.newsaggregatorclient.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatter {
    public static String processDateTime(String dateTime){
        /**
         * Xử lý chuỗi ngày tháng
         * Nếu thời gian đăng chưa quá 1 ngày, hiển thị bài đăng cách đây bao nhiêu giờ
         * Nếu thời gian đăng chưa quá 1 tuần, hiển thị bài đăng cách đây bao nhiêu ngày
         * Còn lại hiển thị ngày tháng năm
         */
        LocalDateTime tmp = LocalDateTime.now();
        System.out.println("Current time: " + tmp);
//        ZoneOffset zoneOffset = ZoneOffset.of(ZoneId.systemDefault().getId());
        OffsetDateTime now = tmp.atOffset(ZoneOffset.of("+07:00"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime tmpPostTime = LocalDateTime.parse(dateTime, formatter);
        System.out.println("Post time: " + tmpPostTime);
        OffsetDateTime postTime = tmpPostTime.atOffset(ZoneOffset.of("+00:00"));
        Duration duration = Duration.between(postTime, now);
        long diff = duration.getSeconds();
        System.out.println("Diff: " + diff);
        if (diff < 60){
            return "Just now";
        }
        else if (diff < 120){
            return "1 minute ago";
        }
        else if (diff < 3600){
            return diff/60 + " minutes ago";
        } else if (diff < 7200) {
            return "1 hour ago";

        } else if (diff < 86400){
            return diff/3600 + " hours ago";
        }
        else if (diff < 172800){
            return "Yesterday";
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

    public static String convertISOToNormal(String dateTime){
        /**
         * Chuyển đổi chuỗi ngày tháng từ ISO sang dạng bình thường
         */
        // Get time with current offset
        DateTimeFormatter inp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            LocalDateTime tmpDate = LocalDateTime.parse(dateTime, inp);
            OffsetDateTime date = tmpDate.atOffset(ZoneOffset.of("+00:00"));
            date.atZoneSimilarLocal(ZoneOffset.of("+07:00"));
            return date.format(out);
        }
        catch (Exception e){
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static String convertISOToDate(String dateTime){
        /**
         * Chuyển đổi chuỗi ngày tháng từ ISO sang dạng bình thường
         */
        SimpleDateFormat inp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // Get time with current offset
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
