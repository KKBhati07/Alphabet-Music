package com.example.alphabetmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AppCompatButton exitBtn;
    static ArrayList<MusicFilesModel> musicFiles;
    static ArrayList<MusicFilesModel> arrAlbums;
    int position=-1;
    ArrayList<NotifyTrackFiles> notifyTrackFilesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrAlbums=new ArrayList<MusicFilesModel>();

//        ------------------------------------NOTIFICATION----------------------------
        notifyTrackFilesArrayList=new ArrayList<>();
        populateFiles();


//        ---------------------------------MANAGING FRAGMENTS------------------------------
        BottomNavigationView bnView=findViewById(R.id.bnView);
        fragManager(new SongsFragment(),
                0);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.navigation_songs){
                    fragManager(new SongsFragment(),1);
                }else if(id==R.id.navigation_albums){
                    fragManager(new AlbumsFragment(),1);
                }else{
                    Intent playingActivity=new Intent(getApplicationContext(),PlayingActivity.class);
                    playingActivity.putExtra("check",-1);
                    startActivity(playingActivity);
                }
                return true;
            }
        });


//        ---------------------------FETCHING SONGS-------------------------------
        musicFiles=getAllSongs(this);



    }

    private void populateFiles() {
        NotifyTrackFiles notifyTrackFiles=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
        NotifyTrackFiles notifyTrackFiles1=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
        NotifyTrackFiles notifyTrackFiles2=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
        NotifyTrackFiles notifyTrackFiles3=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
        NotifyTrackFiles notifyTrackFiles4=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
        NotifyTrackFiles notifyTrackFiles5=new NotifyTrackFiles("Song Name","Singer Name",R.drawable.music_app_icon);
    }

    public void fragManager(Fragment fragment,int check){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(check==0){
            ft.add(R.id.main_container,fragment);
        }else{
            ft.replace(R.id.main_container,fragment);
        }
        ft.commit();

    }

    public static ArrayList<MusicFilesModel> getAllSongs(Context context){
        ArrayList<String> duplicates=new ArrayList<>();
        ArrayList<MusicFilesModel> tempAudioList=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.DATA,//it is for the path
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID

        };
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String path=cursor.getString(0);
                String title=cursor.getString(1);
                String album=cursor.getString(2);
                String artist=cursor.getString(3);
                String duration=cursor.getString(4);
                String ID=cursor.getString(5);
//                Long albumArt=cursor.getLong(5);

//                Uri uri1=Uri.parse("content://media/external/audio/albumart");
//                String artUri=Uri.withAppendedPath(uri,albumArt.toString()).toString();

                MusicFilesModel musicFilesModel=new MusicFilesModel(path,title,album,artist,duration,ID);
                tempAudioList.add(musicFilesModel);
//                Log.e("PATH",path);
//                Log.e("ALBUM",album);


                if(!duplicates.contains(album)){
                    arrAlbums.add(musicFilesModel);
                    duplicates.add(String.valueOf(arrAlbums));

                }
            }
            cursor.close();
        }
        return tempAudioList;


    }
}