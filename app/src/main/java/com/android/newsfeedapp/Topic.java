package com.android.newsfeedapp;


public class Topic {
    private String title;
    private String sectionName;
    private String publishDate;
    private String author;
    private String webUrl;


    public Topic(String title) {
        this.title = title;
    }

    public Topic(String title, String sectionName, String publishDate, String author, String webUrl) {
        this.title = title;
        this.sectionName = sectionName;
        this.publishDate = publishDate;
        this.author = author;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
