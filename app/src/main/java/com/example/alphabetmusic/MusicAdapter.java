package com.example.alphabetmusic;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.myViewHolder> {
    private Context mContext;
    private ArrayList<MusicFilesModel> mFiles;

    public MusicAdapter(android.content.Context mContext, ArrayList<MusicFilesModel> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
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
        holder.artistName.setText(mFiles.get(position).getArtist());    //-----------------some error here needs to be fixed>>FIXED
        byte[] image=getAlbumArt(mFiles.get(position).getPath());
        Glide.with(mContext).load(image)
                .apply(RequestOptions.placeholderOf(R.drawable.music_app_icon).centerCrop())
                .into(holder.albumArt);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playingActivity=new Intent(mContext,PlayingActivity.class);
                playingActivity.putExtra("position",position);
                mContext.startActivity(playingActivity);

            }
        });

//        -------------------------delete dialog box---------------------
        holder.songsRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog deleteDialog=new Dialog(mContext);
                deleteDialog.setContentView(R.layout.custom_delete_dialog);
                deleteDialog.show();
                TextView deleteYes,deleteNo;
                deleteYes=deleteDialog.findViewById(R.id.delete_yes);
                deleteNo=deleteDialog.findViewById(R.id.delete_no);
                deleteYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDelete(position);



                    }
                });
                deleteNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
                        deleteDialog.dismiss();
                    }
                });



                return true;
            }
        });



    }

    public void toDelete(int position) {
        //uri used to delete the data from the storage
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Long.parseLong(mFiles.get(position).getID()));


        File file=new File(mFiles.get(position).getPath());
        boolean deleted=file.delete();
        if(deleted){
            //his is very imp, will throw error otherwise
            mContext.getContentResolver().delete(contentUri,null,null);


            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mFiles.size());
            Toast.makeText(mContext, "SONG DELETED", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(mContext, "UNABLE TO DELETE THE SONG", Toast.LENGTH_SHORT).show();
        }


    }

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

//    <application
//    android:requestLegacyExternalStorage="true"  // this is very imp to put in manifest file while using metadata
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
