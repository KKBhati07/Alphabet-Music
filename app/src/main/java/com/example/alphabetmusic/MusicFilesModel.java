package com.example.alphabetmusic;

//MODEL TO STORE THE SONGS
public class MusicFilesModel {
    private String path;
    private String title;
    private String album;
    private String artist;
    private String duration;
    private String ID;



    public MusicFilesModel(String path, String title, String album, String artist, String duration,String ID) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.ID=ID;
    }

    public MusicFilesModel(){
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
