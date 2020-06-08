package com.justice.storyapp;

import java.util.Date;

public class StoryData
{
    private String title;
    private String story;
    private Date date;
    private int genre;

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public StoryData() {
    }

    public StoryData(String title, String story, Date date) {
        this.title = title;
        this.story = story;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
