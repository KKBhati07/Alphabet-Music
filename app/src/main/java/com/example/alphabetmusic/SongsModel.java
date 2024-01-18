package com.example.alphabetmusic;

import com.google.gson.annotations.SerializedName;


//model class to store fetched songs
public class SongsModel {
    @SerializedName("_id")
    private String id;
    private String title,duration,path,coverArt;
    private Artist artist;
    private Album album;
    private UploadedBy uploadedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }

    public String getArtist() {
        return artist.getName();
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album.getName();
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public UploadedBy getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UploadedBy uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}

//to store Artist object
class Artist {
    @SerializedName("_id")
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//to store Album object
class Album {
    @SerializedName("_id")
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//to store uploaded by object
class UploadedBy {
    @SerializedName("_id")
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
