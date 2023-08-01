package com.example.alphabetmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.example.alphabetmusic.AlbumDetailsAdapter.isPlayingFromAlbum;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.myViewHolder> {
    private Context mContext;
    private ArrayList<MusicFilesModel> mFiles;
    private RequestOptions glideOptions;

    public MusicAdapter(android.content.Context mContext, ArrayList<MusicFilesModel> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.glideOptions=new RequestOptions().placeholder(R.drawable.music_app_icon).centerCrop().diskCacheStrategy(ALL);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.songs_row_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.songTitle.setText(mFiles.get(position).getTitle());
        holder.artistName.setText(mFiles.get(position).getArtist());
        //calling the async method to load album art
        loadAlbumArtAsync(mFiles.get(position).getPath(), holder.albumArt,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playingActivity=new Intent(mContext,PlayingActivity.class);
                isPlayingFromAlbum=false;
                playingActivity.putExtra("position",position);
                mContext.startActivity(playingActivity);

            }
        });

    }


    //to get the item count
    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView albumArt;
        TextView songTitle;
        TextView artistName;
        LinearLayout songsRow;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt=itemView.findViewById(R.id.albumArt);
            songTitle=itemView.findViewById(R.id.songTitle);
            artistName=itemView.findViewById(R.id.artistName);
            songsRow=itemView.findViewById(R.id.songs_row);
        }
    }

//    --------------------------------ALBUM ART------------------------

    // Load album art image asynchronously using thread for optimization, and smooth scrolling experience
    private void loadAlbumArtAsync(String path, ImageView imageView,int position) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                byte[] albumArt = getAlbumArt(path);
                if (albumArt != null) {
                    // Use the UI thread's Handler to update the ImageView
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Setting the loaded album art data as the ImageView image
                            imageView.setImageBitmap(BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length));
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

//    <application
//    android:requestLegacyExternalStorage="true"  // this is very imp to put in manifest file while using metadata

//    to fetch the album art data
    public byte[] getAlbumArt(String uri) {
        Log.d("AlbumArt", "URI: " + uri);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] art = null;
        try {
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            retriever.release();
            return art;

        }
    }
}
