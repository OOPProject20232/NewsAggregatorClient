package org.newsaggregator.newsaggregatorclient.database;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJDBC {
    // =))))))))
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/sqlite/bookmarks.db";
    private static final String SQL_SELECT = "SELECT * FROM BOOKMARKS, CATEGORIES, BOOKMARKS_CATEGORIES WHERE BOOKMARKS.guid = BOOKMARKS_CATEGORIES.bookmark_guid AND CATEGORIES.id = BOOKMARKS_CATEGORIES.category_id";
    private static final String SQL_INSERT_BOOKMARK = "INSERT INTO BOOKMARKS (guid, title, author, description, content, url, url_to_image, published_at, publisher, publisher_logo_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CATEGORY = "INSERT INTO CATEGORIES (name) VALUES (?)";
    private static final String SQL_INSERT_BOOKMARK_CATEGORY = "INSERT INTO BOOKMARKS_CATEGORIES (bookmark_guid, category_id) VALUES (?, ?)";
    private static final String SQL_DELETE_BOOKMARK = "DELETE FROM BOOKMARKS WHERE guid = ?";
    private static final String SQL_CREATE_BOOKMARK = "CREATE TABLE BOOKMARKS " +
            "(guid TEXT PRIMARY KEY NOT NULL," +
            "title TEXT NOT NULL," +
            "author TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "content TEXT NOT NULL," +
            "url TEXT NOT NULL," +
            "url_to_image TEXT," +
            "published_at TEXT NOT NULL," +
            "publisher TEXT NOT NULL," +
            "publisher_logo_url TEXT NOT NULL)";
    private static final String SQL_CREATE_CATEGORY = "CREATE TABLE CATEGORIES " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL)";
    private static final String SQL_CREATE_BOOKMARK_CATEGORY = "CREATE TABLE BOOKMARKS_CATEGORIES " +
            "(bookmark_guid TEXT NOT NULL," +
            "category_id INTEGER NOT NULL," +
            "FOREIGN KEY (bookmark_guid) REFERENCES BOOKMARKS(guid)," +
            "FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)," +
            "PRIMARY KEY (bookmark_guid, category_id))";

    public SQLiteJDBC() {

    }

    public List<NewsItemData> select() throws RuntimeException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<NewsItemData> data = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_SELECT);
            while(rs.next()) {
                NewsItemData newsItemData = new NewsItemData();
                newsItemData.setGuid(rs.getString("guid"));
                newsItemData.setTitle(rs.getString("title"));
                newsItemData.setAuthor(rs.getString("author"));
                newsItemData.setDescription(rs.getString("description"));
                newsItemData.setContent(rs.getString("content"));
                newsItemData.setUrl(rs.getString("url"));
                newsItemData.setUrlToImage(rs.getString("url_to_image"));
                newsItemData.setPublishedAt(rs.getString("published_at"));
                newsItemData.setPublisher(rs.getString("publisher"));
                newsItemData.setPublisherLogoURL(rs.getString("publisher_logo_url"));
                // 1 news item can have multiple categories
                List<String> categories = new ArrayList<>();
                categories.add(rs.getString("name"));
                newsItemData.setCategory(categories);
                data.add(newsItemData);
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return data;
    }

    public int insert(NewsItemData data) throws RuntimeException {
        if(data.getGuid() == null) {
            throw new IllegalArgumentException("GUID cannot be null");
        }
        if (data.getTitle() == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (data.getAuthor() == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (data.getDescription() == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (data.getArticleDetailedContent() == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (data.getUrl() == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        if (data.getUrlToImage() == null) {
            throw new IllegalArgumentException("URL to image cannot be null");
        }
        if (data.getPublishedAt() == null) {
            throw new IllegalArgumentException("Published at cannot be null");
        }
        if (data.getPublisher() == null) {
            throw new IllegalArgumentException("Publisher cannot be null");
        }
        if (data.getPublisherLogoURL() == null) {
            throw new IllegalArgumentException("Publisher logo URL cannot be null");
        }
        if (data.getCategory() == null) {
            throw new IllegalArgumentException("Categories cannot be null");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        int result = 0;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.prepareStatement(SQL_INSERT_BOOKMARK);
            stmt.setString(1, data.getGuid());
            stmt.setString(2, data.getTitle());
            stmt.setString(3, data.getAuthor());
            stmt.setString(4, data.getDescription());
            stmt.setString(5, data.getArticleDetailedContent());
            stmt.setString(6, data.getUrl());
            stmt.setString(7, data.getUrlToImage());
            stmt.setString(8, data.getPublishedAt());
            stmt.setString(9, data.getPublisher());
            stmt.setString(10, data.getPublisherLogoURL());
            result = stmt.executeUpdate();
            stmt.close();
            stmt = conn.prepareStatement(SQL_INSERT_CATEGORY);
            for(String category : data.getCategory()) {
                stmt.setString(1, category);
                stmt.executeUpdate();
                stmt.close();
                stmt = conn.prepareStatement(SQL_INSERT_BOOKMARK_CATEGORY);
                stmt.setString(1, data.getGuid());
                stmt.setInt(2, 1);
                result = stmt.executeUpdate();
                stmt.close();
            }
            result = 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    public int delete(String guid) throws RuntimeException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int result = 0;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.prepareStatement(SQL_DELETE_BOOKMARK);
            stmt.setString(1, guid);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
}
