package com.example.alphabetmusic;

public class NotifyTrackFiles {
    private String title;
    private String singer;
    private int thumbnail;

    public NotifyTrackFiles(String title, String singer, int thumbnail) {
        this.title = title;
        this.singer = singer;
        this.thumbnail = thumbnail;
    }

    public NotifyTrackFiles() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
