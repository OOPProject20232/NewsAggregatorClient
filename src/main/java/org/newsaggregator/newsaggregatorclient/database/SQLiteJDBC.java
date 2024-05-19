package org.newsaggregator.newsaggregatorclient.database;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteJDBC {
    // =))))))))
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/sqlite/bookmarks.db";
    private static final String SQL_SELECT = "SELECT DISTINCT * FROM BOOKMARKS, CATEGORIES, BOOKMARKS_CATEGORIES WHERE BOOKMARKS.guid = BOOKMARKS_CATEGORIES.bookmark_guid AND CATEGORIES.id = BOOKMARKS_CATEGORIES.category_id";
    private static final String SQL_CHECK_CATEGORY = "SELECT id FROM CATEGORIES WHERE name = ?";
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
            "name TEXT NOT NULL, " +
            "UNIQUE (name) ON CONFLICT IGNORE)";
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
                data.add(newsItemData);
            }
            stmt.close();

            for (NewsItemData newsItemData : data) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT name FROM CATEGORIES, BOOKMARKS_CATEGORIES WHERE CATEGORIES.id = BOOKMARKS_CATEGORIES.category_id AND BOOKMARKS_CATEGORIES.bookmark_guid = '" + newsItemData.getGuid() + "'");
                List<String> categories = new ArrayList<>();
                while(rs.next()) {
                    categories.add(rs.getString("name"));
                }
                newsItemData.setCategory(categories);
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

    public void insert(NewsItemData data) throws RuntimeException {
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
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);
            // Insert bookmark
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
            stmt.executeUpdate();
            stmt.close();

            // Insert or link categories
            for (String category : data.getCategory()) {
                stmt = conn.prepareStatement(SQL_CHECK_CATEGORY);
                stmt.setString(1, category);
                ResultSet rs = stmt.executeQuery();
                long category_id;
                if (!rs.next()) {
                    stmt = conn.prepareStatement(SQL_INSERT_CATEGORY);
                    stmt.setString(1, category);
                    stmt.executeUpdate();
                    category_id = stmt.getGeneratedKeys().getLong(1);
                }
                else {
                    category_id = rs.getLong(1);
                }
                stmt.close();

                stmt = conn.prepareStatement(SQL_INSERT_BOOKMARK_CATEGORY);
                stmt.setString(1, data.getGuid());
                stmt.setLong(2, category_id);
                stmt.executeUpdate();
                stmt.close();
            }
            conn.commit();
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
    }

    public void delete(String guid) throws RuntimeException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);
            // Delete from BOOKMARKS_CATEGORIES
            stmt = conn.prepareStatement("DELETE FROM BOOKMARKS_CATEGORIES WHERE bookmark_guid = ?");
            stmt.setString(1, guid);
            stmt.executeUpdate();
            stmt.close();
            // Check category references
            stmt = conn.prepareStatement("SELECT COUNT(bookmark_guid) FROM BOOKMARKS_CATEGORIES WHERE category_id = (SELECT category_id FROM BOOKMARKS_CATEGORIES WHERE bookmark_guid = ?)");
            stmt.setString(1, guid);
            ResultSet rs = stmt.executeQuery();
            int refCount = rs.getInt(1);
            rs.close();
            stmt.close();
            // Delete category if no more references
            if (refCount-1 == 0) {
                stmt = conn.prepareStatement("DELETE FROM CATEGORIES WHERE id = (SELECT category_id FROM BOOKMARKS_CATEGORIES WHERE bookmark_guid = ?)");
                stmt.setString(1, guid);
                stmt.executeUpdate();
                stmt.close();
            }
            // Delete bookmarks
            stmt = conn.prepareStatement(SQL_DELETE_BOOKMARK);
            stmt.setString(1, guid);
            stmt.executeUpdate();
            stmt.close();

            conn.commit();
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
    }

    public static void main(String[] args) {
        // drop table
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS BOOKMARKS");
            stmt.execute("DROP TABLE IF EXISTS CATEGORIES");
            stmt.execute("DROP TABLE IF EXISTS BOOKMARKS_CATEGORIES");
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        // create table
//        Connection conn = null;
//        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            stmt.execute(SQL_CREATE_BOOKMARK);
            stmt.execute(SQL_CREATE_CATEGORY);
            stmt.execute(SQL_CREATE_BOOKMARK_CATEGORY);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    }
}
