package com.example.alphabetmusic;

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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//SETTING RECYCLER VIEW ADAPTER FOR ALBUMS ACTIVITY
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.myViewHolderAlbums> {
    private Context mContext;

//    creating array list of type MusicFilesModel
    private ArrayList<MusicFilesModel> albumFiles;

//    initializing the constructor
    public AlbumAdapter(Context mContext, ArrayList<MusicFilesModel> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    View view;

    @NonNull
    @Override
    public myViewHolderAlbums onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.albums_items,parent,false);
        return new myViewHolderAlbums(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolderAlbums holder, int position) {
        holder.albumName.setText(albumFiles.get(position).getAlbum());

        loadAlbumArtAsync(albumFiles.get(position).getPath(),holder.albumImage);

        //setting click listener on items in recycler view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting intent for album details activity
                Intent albumDetailsActivity= new Intent(mContext,AlbumDetailsActivity.class);
                albumDetailsActivity.putExtra("albumNames",albumFiles.get(position).getAlbum());
                mContext.startActivity(albumDetailsActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class myViewHolderAlbums extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName;


        public myViewHolderAlbums(@NonNull View itemView) {
            super(itemView);
            albumImage=itemView.findViewById(R.id.album_image_albums);
            albumName=itemView.findViewById(R.id.album_name_album);
        }
    }

//    --------------------ALBUM ART-------------------

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
            return null;
        }
    }
}
