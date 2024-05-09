package org.newsaggregator.newsaggregatorclient.datamodel;

public class RedditPostData extends GenericData{
    private String title;
    private String author;
    private String url;
    private String subreddit;
    private String postedTime;

    public RedditPostData(String title, String author, String url, String subreddit) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.subreddit = subreddit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getLinkToSub(){
        return "https://www.reddit.com/r/" + subreddit;
    }

    public String getLinkToAuthor(){
        return "https://www.reddit.com/u/" + author;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

}
