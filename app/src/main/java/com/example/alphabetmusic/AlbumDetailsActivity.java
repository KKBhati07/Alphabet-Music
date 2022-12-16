package com.example.alphabetmusic;

import static com.example.alphabetmusic.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AlbumDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView albumPhoto, nowPlaying;
    String albumName;
    ArrayList <MusicFilesModel> albumSongss=new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView=findViewById(R.id.albums_recycler_view);
        albumPhoto=findViewById(R.id.album_details_album_art);
        albumName=getIntent().getStringExtra("albumNames");
        nowPlaying=findViewById(R.id.now_playing_from_album_details);

//        nowPlaying.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent nowPlayingIntent=new Intent(getApplicationContext(),PlayingActivity.class);
//                nowPlayingIntent.putExtra("check",-1);
//                nowPlayingIntent.putExtra("sending","albumDetails00");
//                startActivity(nowPlayingIntent);
//            }
//        });

        int j=0;
        for(int i=0;i<musicFiles.size();i++){
            if(albumName.equals(musicFiles.get(i).getAlbum())){
                albumSongss.add(j,musicFiles.get(i));
                j++;
            }
        }
        byte[] image=getAlbumArt(albumSongss.get(0).getPath());
        Glide.with(getApplicationContext()).load(image)
                .apply(RequestOptions.placeholderOf(R.drawable.music_app_icon_in_png).centerCrop())
                .into(albumPhoto);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongss.size()<1)){
            albumDetailsAdapter=new AlbumDetailsAdapter(this,albumSongss);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }

    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art;
        try {
            art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        } catch (Exception e) {
            e.printStackTrace();
            return art = null;
        }
    }
}