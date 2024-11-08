package com.example.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedEntry {
    private String title;
    private String link;
    private String dc_creator;
    private String pubDate;
    private List<String> categories;
    private String guid;
    private String description;

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

    public String getDc_creator() {
        return dc_creator;
    }

    public void setDc_creator(String dc_creator) {
        this.dc_creator = dc_creator;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> category) {
        this.categories = category;
    }

    public void addToCategories(String category) {
        if (categories == null){
            categories = new ArrayList<>();
        }
        categories.add(category);
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

    @Override
    public String toString() {
        return "FeedEntry{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", dc_creator='" + dc_creator + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", categories=" + categories +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
