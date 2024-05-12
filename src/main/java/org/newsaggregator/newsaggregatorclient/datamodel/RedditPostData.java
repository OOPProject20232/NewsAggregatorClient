package org.newsaggregator.newsaggregatorclient.datamodel;

public class RedditPostData extends GenericData{
    private String title;
    private String author;
    private String url;
    private String subreddit;
    private String postedTime;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    private String mediaUrl;

    private int upvotes;

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    private int downvotes;
    private String postContent;

    public RedditPostData(){
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

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    @Override
    public String toString() {
        return """
                RedditPostData{
                    title='%s',
                    author='%s',
                    url='%s',
                    subreddit='%s',
                    postedTime='%s',
                    upvotes=%d,
                    downvotes=%d,
                    postContent='%s'
                }
                """.formatted(title, author, url, subreddit, postedTime, upvotes, downvotes, postContent);
    }
}
