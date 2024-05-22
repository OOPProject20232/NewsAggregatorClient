package org.newsaggregator.newsaggregatorclient.database;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.util.List;

public class SQLite2NewsItemData {
    List<NewsItemData> bookmarkedList;
    SQLiteJDBC db;
    public SQLite2NewsItemData() {
        db = new SQLiteJDBC();
        bookmarkedList = db.select();
    }

    public List<NewsItemData> getBookmarkedList() {
        return bookmarkedList;
    }

    public void insert(NewsItemData item) {
        db.insert(item);
        bookmarkedList = db.select();
    }

    public void delete(String guid) {
        db.delete(guid);
        bookmarkedList = db.select();
    }
}
