package com.example.alphabetmusic;

import java.util.ArrayList;


//class to store the response from the api call
public class ApiResponse {
    private String message;
    private ArrayList<SongsModel> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SongsModel> getSongs() {
        return data;
    }

    public void setSongs(ArrayList<SongsModel> data) {
        this.data = data;
    }
}
