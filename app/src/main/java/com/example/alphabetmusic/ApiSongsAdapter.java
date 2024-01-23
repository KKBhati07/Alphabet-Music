package com.example.alphabetmusic;


import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.example.alphabetmusic.AlbumDetailsAdapter.isPlayingFromAlbum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiSongsAdapter extends RecyclerView.Adapter<ApiSongsAdapter.myViewHolder> {

    public static boolean playingFromServer=false;

    private Context context;
    private ArrayList<MusicFilesModel> songs;
    private RequestOptions glideOptions;

    private Handler handler = new Handler();
    private static final long CHECK_INTERVAL = 1000;

    public ApiSongsAdapter(Context context, ArrayList<MusicFilesModel> songs){
        this.context=context;
        this.songs=songs;
        this.glideOptions=new RequestOptions().placeholder(R.drawable.music_app_icon).centerCrop().diskCacheStrategy(ALL);
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.songs_row_layout,parent,false);
        return new ApiSongsAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        MusicFilesModel currentSong=songs.get(position);
        holder.songTitle.setText(currentSong.getTitle());
        holder.artistName.setText(currentSong.getArtist());
        //calling the async method to load album art
        loadAlbumArtAsync(songs.get(position).getCoverArt(), holder.albumArt,position);

        if(!isInternetAvailable()){
            holder.songTitle.setTextColor(context.getResources().getColor(R.color.grey));
            holder.artistName.setTextColor(context.getResources().getColor(R.color.grey));

//            handler to check for internet connectivity
            handler.postDelayed(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
//                    if internet is available
                    if(isInternetAvailable()){
//                        changing the color scheme to reflect that internet is available
                        holder.songTitle.setTextColor(context.getResources().getColor(R.color.white));
                        holder.artistName.setTextColor(context.getResources().getColor(R.color.white));
                        holder.itemView.setOnClickListener(view -> onClickHandler(position,currentSong));
                        notifyDataSetChanged();
//                        setting handler to check again fo the connectivity
                    }else handler.postDelayed(this,CHECK_INTERVAL);
                }
            },CHECK_INTERVAL);

//            if internet is available
        }else{
            holder.itemView.setOnClickListener(v -> onClickHandler(position,currentSong));
            holder.songTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.artistName.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

//    method to handle the click functionality
    private void onClickHandler(int position,MusicFilesModel currentSong){
        if(!isInternetAvailable())return;
        Intent playingActivity=new Intent(context,PlayingActivity.class);
        isPlayingFromAlbum=false;
        playingActivity.putExtra("position",position);
        playingActivity.putExtra("songUrl",currentSong.getPath());
        playingFromServer=true;
        context.startActivity(playingActivity);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView albumArt;
        TextView songTitle;
        TextView artistName;
        LinearLayout songsRow;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt = itemView.findViewById(R.id.albumArt);
            songTitle = itemView.findViewById(R.id.songTitle);
            artistName = itemView.findViewById(R.id.artistName);
            songsRow = itemView.findViewById(R.id.songs_row);
        }

    }

//    method to check device is connected to internet or not
    private boolean isInternetAvailable(){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo info=manager.getActiveNetworkInfo();
            return info!=null && info.isConnected();
        }
        return false;
    }

//    to load album art asynchronously on a separate thread
    private void loadAlbumArtAsync(String path, ImageView imageView, int position) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Bitmap albumArt = getAlbumArt(path);
                if (albumArt != null) {
                    // Use the UI thread's Handler to update the ImageView
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Setting the loaded album art data as the ImageView image
                            imageView.setImageBitmap(albumArt);
                        }
                    });
                    //if there is no album art with the song
                } else {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(R.drawable.music_app_icon);
                        }
                    });
                }
            }
        });
    }

    //    to fetch the album art data
    public Bitmap getAlbumArt(String url) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream=new URL(url).openStream();
            bitmap=BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            return bitmap;

        }
    }
}
