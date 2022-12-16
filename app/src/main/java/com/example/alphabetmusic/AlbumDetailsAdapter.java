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
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.myViewHolderAlbumsDetails> {
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
//        holder.artistName.setText(mFiles.get(position).getArtist());    //-----------------some error here needs to be fixed>>FIXED
        byte[] image=getAlbumArt(albumFiles.get(positions).getPath());
        Glide.with(mContext).load(image)
                .apply(RequestOptions.placeholderOf(R.drawable.music_app_icon_in_png).centerCrop())
                .into(holder.albumImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playing= new Intent(mContext,PlayingActivity.class);
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
