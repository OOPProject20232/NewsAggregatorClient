package org.newsaggregator.newsaggregatorclient.database;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLiteJDBC {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/sqlite/bookmarks.db";
    private static final String SQL_SELECT = "SELECT * FROM BOOKMARKS";
    private static final String SQL_SELECT_BY_GUID = "SELECT EXISTS(SELECT * FROM BOOKMARKS WHERE guid = ?)";
    private static final String SQL_INSERT_BOOKMARK = "INSERT INTO BOOKMARKS (guid, title, author, description, content, url, url_to_image, published_at, publisher, publisher_logo_url, categories) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    private static final String SQL_SEARCH_BOOKMARK = "SELECT * FROM BOOKMARKS WHERE title LIKE ? OR description LIKE ?";

    public SQLiteJDBC() {

    }

    /**
     * Thường chương trình sẽ có sẵn db, trong trường hợp không có, hãy dùng phương thức này để khởi tạo bảng
     */
    public void createTable() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            stmt.execute(SQL_CREATE_BOOKMARK);
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

    /**
     * Phương thức này sẽ trả về danh sách các bài báo đã lưu
     * @return List<NewsItemData> danh sách các bài báo đã lưu
     * @throws RuntimeException
     */
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
                String categories = rs.getString("categories");
                newsItemData.setCategory(Arrays.asList(categories.split("-")));
                data.add(newsItemData);
            }
            stmt.close();
            return data;
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
    }

    /**
     * PPhuowng thức này sẽ thêm thng tin bài báo vào db
     * @param item bài báo cần lưu, yêu cầu kiểu dữ liệu NewsItemData
     * @throws RuntimeException
     *
     * @see NewsItemData
     */
    public void insert(NewsItemData item) throws RuntimeException {
        if(item.getGuid() == null) {
            throw new IllegalArgumentException("GUID cannot be null");
        }
        if (item.getTitle() == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (item.getAuthor() == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (item.getDescription() == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (item.getArticleDetailedContent() == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (item.getUrl() == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        if (item.getUrlToImage() == null) {
            throw new IllegalArgumentException("URL to image cannot be null");
        }
        if (item.getPublishedAt() == null) {
            throw new IllegalArgumentException("Published at cannot be null");
        }
        if (item.getPublisher() == null) {
            throw new IllegalArgumentException("Publisher cannot be null");
        }
        if (item.getPublisherLogoURL() == null) {
            throw new IllegalArgumentException("Publisher logo URL cannot be null");
        }
        if (item.getCategory() == null) {
            throw new IllegalArgumentException("Categories cannot be null");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.prepareStatement(SQL_INSERT_BOOKMARK);
            stmt.setString(1, item.getGuid());
            stmt.setString(2, item.getTitle());
            stmt.setString(3, item.getAuthor());
            stmt.setString(4, item.getDescription());
            stmt.setString(5, item.getArticleDetailedContent());
            stmt.setString(6, item.getUrl());
            stmt.setString(7, item.getUrlToImage());
            stmt.setString(8, item.getPublishedAt());
            stmt.setString(9, item.getPublisher());
            stmt.setString(10, item.getPublisherLogoURL());
            stmt.setString(11, String.join("-",item.getCategory()));
            stmt.executeUpdate();
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
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Phương thức này sẽ xóa bài báo khỏi db
     * @param guid GUID của bài báo cần xóa
     * @throws RuntimeException
     */
    public void delete(String guid) throws RuntimeException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.prepareStatement(SQL_DELETE_BOOKMARK);
            stmt.setString(1, guid);
            stmt.executeUpdate();
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
    }

    public boolean isBookmarked(NewsItemData itemData){
        String guid = itemData.getGuid();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_BY_GUID);
            preparedStatement.setString(1, guid);
            rs = preparedStatement.executeQuery();
            boolean result = rs.getBoolean(1);
            System.out.println("Checking bookmarked from guid " + guid + ": " + result);
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<NewsItemData> search(String searchText){
        searchText = searchText
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<NewsItemData> data = new ArrayList<>();
        try{
            conn = DriverManager.getConnection(DB_URL);
            statement = conn.prepareStatement(SQL_SEARCH_BOOKMARK);
            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            System.out.println("Statement: " + statement);
            rs = statement.executeQuery();
            while (rs.next()) {
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
                String categories = rs.getString("categories");
                newsItemData.setCategory(Arrays.asList(categories.split("-")));
                data.add(newsItemData);
            };
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
