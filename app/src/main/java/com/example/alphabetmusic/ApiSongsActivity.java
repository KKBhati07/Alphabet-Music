package com.example.alphabetmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiSongsActivity extends AppCompatActivity {
    static ArrayList<MusicFilesModel> songs;
    ArrayList<SongsModel>fetchedSongs;
    private RecyclerView recyclerView;
    private ApiSongsAdapter apiSongsAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_songs);
        init();
        //checking songs are already fetched
        if(songs==null ||songs.isEmpty()){

            //to show the progress bar
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading Songs..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //making request
            //creating retrolfit instance
            Retrofit retrofit=new Retrofit.Builder().baseUrl("https://good-tan-beaver-hat.cyclic.app")
                    .addConverterFactory(GsonConverterFactory.create()).client(UnsafeHttpClient.getUnsafeOkHttpClient()).build();
            //creating instance of the interface
            ApiService apiService=retrofit.create(ApiService.class);
            Call<ApiResponse> call=apiService.getResponse();
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse=response.body();
                        fetchedSongs = apiResponse.getSongs();
                        songs=convertToMusicFilesModel(fetchedSongs);
                        displaySongs(songs);
                    } else Toast.makeText(ApiSongsActivity.this,"Unable to load songs..",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                    progressDialog.dismiss();
                    Toast.makeText(ApiSongsActivity.this,"Unable to load songs..",Toast.LENGTH_LONG).show();
                }
            });
        }else displaySongs(songs);
    }
    private void init(){
        recyclerView=findViewById(R.id.recyclerViewSongs);
    }

    private void displaySongs(ArrayList<MusicFilesModel> songs){
        apiSongsAdapter=new ApiSongsAdapter(this,songs);
        recyclerView.setAdapter(apiSongsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);


    }

    public ArrayList<MusicFilesModel> convertToMusicFilesModel(ArrayList<SongsModel> songsModels) {
        ArrayList<MusicFilesModel> musicFilesModels = new ArrayList<>();

        for (SongsModel songsModel : songsModels) {
            MusicFilesModel musicFilesModel = new MusicFilesModel(songsModel);
            musicFilesModels.add(musicFilesModel);
        }
        return musicFilesModels;
    }
}