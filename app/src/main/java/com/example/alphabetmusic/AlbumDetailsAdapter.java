package com.example.alphabetmusic;

import static com.example.alphabetmusic.ApiSongsAdapter.playingFromServer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.myViewHolderAlbumsDetails> {
    //declaring variable to check song is playing form album or from song list
    public static boolean isPlayingFromAlbum=false;
    private Context mContext;
    static ArrayList<MusicFilesModel> albumFiles;

    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFilesModel> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    View view;

    @NonNull
    @Override
    public myViewHolderAlbumsDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_details_music_items,parent,false);
        return new myViewHolderAlbumsDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolderAlbumsDetails holder, int positions) {
        holder.songNameAlbum.setText(albumFiles.get(positions).getTitle());

        loadAlbumArtAsync(albumFiles.get(positions).getPath(),holder.albumImage);

//        on clicking on a item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playing= new Intent(mContext,PlayingActivity.class);
                //setting it ti true
                isPlayingFromAlbum=true;
                playingFromServer=false;
//                sending data with changing intent
                playing.putExtra("sending","albumDetails00");
                playing.putExtra("position",positions);
                mContext.startActivity(playing);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class myViewHolderAlbumsDetails extends RecyclerView.ViewHolder{
        ShapeableImageView albumImage;
        TextView songNameAlbum;


        public myViewHolderAlbumsDetails(@NonNull View itemView) {
            super(itemView);
            albumImage=itemView.findViewById(R.id.albumArtAlbum);
            songNameAlbum=itemView.findViewById(R.id.songTitleAlbumItem);
        }
    }

//    ----------------------ALBUM ART-------------------------------
//to load album art asynchronously, for smooth scroll
    private void loadAlbumArtAsync(String path, ImageView imageView) {
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


    //to fetch the album art
    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] art;
        try {
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        } catch (Exception e) {
            e.printStackTrace();
            return art = null;
        }
    }
}
