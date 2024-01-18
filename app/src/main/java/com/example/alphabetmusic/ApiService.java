package com.example.alphabetmusic;

import retrofit2.Call;
import retrofit2.http.GET;

//interface to make the request call
public interface ApiService {
    @GET("/api/v1/songs/fetch")
    Call<ApiResponse> getResponse();
}
