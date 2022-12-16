package com.example.alphabetmusic;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.myViewHolderAlbums> {
    private Context mContext;
    private ArrayList<MusicFilesModel> albumFiles;

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
//        holder.artistName.setText(mFiles.get(position).getArtist());    //-----------------some error here needs to be fixed>>FIXED
        byte[] image=getAlbumArt(albumFiles.get(position).getPath());
        Glide.with(mContext).load(image)
                .apply(RequestOptions.placeholderOf(R.drawable.music_app_icon_in_png).centerCrop())
                .into(holder.albumImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
