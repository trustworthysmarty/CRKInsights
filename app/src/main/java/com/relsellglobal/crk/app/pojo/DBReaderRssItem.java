package com.relsellglobal.crk.app.pojo;

/**
 * Created by anilkukreti on 05/02/16.
 */
public class DBReaderRssItem implements IRSSItem {
    private String title;
    private String link;
    private String category;
    private String categoryId;
    private String author;
    private String pubDate;
    private String guid;
    private String description;
    private String mediaThumbnail;
    private String thr;
    private String feedBurnerLink;
    private boolean isSectionHeader;


    public boolean isSectionHeader() {
        return isSectionHeader;
    }

    public void setSectionHeader(boolean myHeader) {
        isSectionHeader = myHeader;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public String getThr() {
        return thr;
    }

    public void setThr(String thr) {
        this.thr = thr;
    }

    public String getFeedBurnerLink() {
        return feedBurnerLink;
    }

    public void setFeedBurnerLink(String feedBurnerLink) {
        this.feedBurnerLink = feedBurnerLink;
    }
}
