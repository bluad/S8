package com.mantoo.yican.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/8.
 */

public class News {

    private Long id;
    private String newsNumber;
    private String newsTitle;
    private String newContent;
    private String newsDate;
    private String newsTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewsNumber() {
        return newsNumber;
    }

    public void setNewsNumber(String newsNumber) {
        this.newsNumber = newsNumber;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }
}
