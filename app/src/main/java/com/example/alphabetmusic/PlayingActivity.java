package com.example.alphabetmusic;


import static com.example.alphabetmusic.AlbumDetailsAdapter.albumFiles;
import static com.example.alphabetmusic.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PlayingActivity extends AppCompatActivity {
    TextView playingSongName, artistName, durationPlayed, durationTotal;
    ImageView coverArt, playPauseBtn, nextBtn, previousBtn, backBtn;
    SeekBar seekBar;
    static int position = 0;
    static ArrayList<MusicFilesModel> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    private Thread playPauseThread, previousThread, nextThread, autoPlayNextThread;
    static int songPosition;
    SeekBar volumeSeekbar;
    AudioManager audioManager;
    ImageView contactActivityBtn;
    Animation coverArtAnim, seekbarAnim, seekbarAnimOut;
    //    private static int change=0;
    private static final int NOTIFICATION_ID = 100;
    private static final String CHANNEL_ID = "100";
    private static final int PENDING_INTENT_REQ_CODE = 100;
    NotificationManager nm;
    Notification notification;
    Bitmap bitmap;
    PendingIntent pi;
    Intent notificationIntent;
    int duration_Total;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        initViews();
        getIntentMethod();
        autoPlayNextThreadMethod();


//        -----------------------------TO CONTACTS ACTIVITY-------------------------
        contactActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactActivity = new Intent(PlayingActivity.this, ContactActivity.class);
                startActivity(contactActivity);

            }
        });

//        --------------------------------VOLUME CHANGE SEEKBAR--------------------------
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekbar.setMax(maxVolume);
        volumeSeekbar.setProgress(currentVolume);
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.seekbar_anim);
                seekbarAnim.setFillAfter(true);
                seekBar.startAnimation(seekbarAnim);


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarAnimOut=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.seekbar_anim_out);
                seekbarAnimOut.setFillAfter(true);
                seekBar.startAnimation(seekbarAnimOut);


            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        artistName.setText(listSongs.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.seekbar_anim);
                seekbarAnim.setFillAfter(true);
                seekBar.startAnimation(seekbarAnim);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.seekbar_anim_out);
                seekbarAnim.setFillAfter(true);
                seekBar.startAnimation(seekbarAnim);

            }
        });

        PlayingActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));

                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }

    }

//    ---------------------------------SETTING ALBUM ART--------------------------

    private void metadata(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        duration_Total = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(duration_Total));
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Glide.with(getApplicationContext()).asBitmap().load(art).into(coverArt);
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@androidx.annotation.Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        ImageView gradient = findViewById(R.id.image_view_gradient);
                        RelativeLayout mContainer = findViewById(R.id.main_container);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        //for gradient
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        //for container
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), swatch.getRgb()});
                        playingSongName.setTextColor(swatch.getTitleTextColor());
                        artistName.setTextColor(swatch.getBodyTextColor());
                        mContainer.setBackground(gradientDrawableBg);
                    }//i can write the else here as well
                }
            });
        } else {
            Glide.with(this).asBitmap().load(R.drawable.music_app_icon_in_png).into(coverArt);
            ImageView gradient = findViewById(R.id.image_view_gradient);
            RelativeLayout mContainer = findViewById(R.id.main_container);
            gradient.setBackgroundResource(R.drawable.gradient_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);

        }
        playingSongName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
    }

    private void getIntentMethod() {

        position = getIntent().getIntExtra("position", position);
        int positions = getIntent().getIntExtra("positions", -1);
        int check = getIntent().getIntExtra("check", 0);
        String sender = getIntent().getStringExtra("sending");
        if (sender != null && sender.equals("albumDetails00")) {
            listSongs = albumFiles;
        } else {
            listSongs = musicFiles; //from main activity
        }
        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null) {
            if (check == -1) {
                if (mediaPlayer.isPlaying()) {
                    playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
                    uri = Uri.parse(listSongs.get(position).getPath());
                } else {
                    coverArtAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.album_art_anim);
                    coverArt.startAnimation(coverArtAnim);
                    coverArtAnim.setFillAfter(true);
                    playPauseBtn.setImageResource(R.drawable.play_icon_filled);
                }
//                mediaPlayer.start();

            } else {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                autoplayNextSong();

            }

        } else {
            if (check == -1) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                playPauseBtn.setImageResource(R.drawable.play_icon_filled);
            } else {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
            }
        }

        autoplayNextSong();
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metadata(uri);
        simpleNotification();
    }

    private void initViews() {
        playingSongName = findViewById(R.id.songName_txt);
        artistName = findViewById(R.id.artistName);
        durationPlayed = findViewById(R.id.durationPlayed);
        durationTotal = findViewById(R.id.durationLeft);
        coverArt = findViewById(R.id.cover_art);
        playPauseBtn = findViewById(R.id.play_btn);
        nextBtn = findViewById(R.id.next_song_btn);
        previousBtn = findViewById(R.id.previous_song_btn);
        backBtn = findViewById(R.id.back_btn_playing_activity);
        seekBar = findViewById(R.id.seekbar);
        volumeSeekbar = findViewById(R.id.seekbar_volume);
        contactActivityBtn = findViewById(R.id.contact_btn);
    }

    @Override
    protected void onResume() {

        nextThreadBtn();
        playPauseThreadBtn();
        previousThreadBtn();

        super.onResume();

    }

//    --------------------------------------SETTING PLAY PAUSE BUTTONS--------------------------------

    private void autoPlayNextThreadMethod() {
        autoPlayNextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                autoplayNextSong();
            }
        };
        autoPlayNextThread.start();
    }


    private void playPauseThreadBtn() {
        playPauseThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playPauseThread.start();
    }

    private void playPauseBtnClicked() {
        coverArtAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.album_art_anim);
        coverArt.startAnimation(coverArtAnim);
        if (mediaPlayer.isPlaying()) {
            coverArtAnim.setFillAfter(true);

            playPauseBtn.setImageResource(R.drawable.play_icon_filled);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            Animation coverArtAnimOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.album_art_anim_out);
            coverArt.startAnimation(coverArtAnimOut);
//            coverArtAnim.setFillAfter(true);
            playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });

        }
    }

    private void previousThreadBtn() {
        previousThread = new Thread() {
            @Override
            public void run() {
                super.run();
                previousBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousBtnClicked();
                    }
                });
            }
        };
        previousThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        Animation bntAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttons_anim);
        nextBtn.startAnimation(bntAnim);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metadata(uri);
            playingSongName.setText(listSongs.get(position).getTitle());

            seekBar.setMax(mediaPlayer.getDuration() / 1000); //copied from play pause
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metadata(uri);
            playingSongName.setText(listSongs.get(position).getTitle());

            seekBar.setMax(mediaPlayer.getDuration() / 1000); //copied from play pause
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.play_icon_filled);
        }
        autoplayNextSong();
        simpleNotification();
    }

    private void previousBtnClicked() {
        Animation bntAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttons_anim);
        previousBtn.startAnimation(bntAnim);

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metadata(uri);
            playingSongName.setText(listSongs.get(position).getTitle());

            seekBar.setMax(mediaPlayer.getDuration() / 1000); //copied from play pause
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metadata(uri);
            playingSongName.setText(listSongs.get(position).getTitle());

            seekBar.setMax(mediaPlayer.getDuration() / 1000); //copied from play pause
            PlayingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.play_icon_filled);
        }
        autoplayNextSong();
        simpleNotification();

    }

    private void autoplayNextSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    position = ((position + 1) % listSongs.size());
                    uri = Uri.parse(listSongs.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    metadata(uri);
                    playingSongName.setText(listSongs.get(position).getTitle());

                    seekBar.setMax(mediaPlayer.getDuration() / 1000); //copied from play pause
                    PlayingActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mCurrentPosition);

                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    playPauseBtn.setImageResource(R.drawable.pause_bar_icon);
                    mediaPlayer.start();
                    autoplayNextSong();
                    simpleNotification();
                }
            });
        }
    }

    private void simpleNotification() {
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.music_app_icon_in_png, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap icon = bitmapDrawable.getBitmap();
        notificationIntent=new Intent(getApplicationContext(),PlayingActivity.class);
        notificationIntent.putExtra("check",-1);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pi=PendingIntent.getActivity(PlayingActivity.this,PENDING_INTENT_REQ_CODE,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(PlayingActivity.this).setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.music_app_icon_in_png)
                    .setContentText(listSongs.get(position).getTitle()).setSubText("NOW PLAYING")
                    .setChannelId(CHANNEL_ID).setSilent(true)
                    .setContentIntent(pi)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "PLAYING", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(PlayingActivity.this).setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.music_app_icon_in_png)
                    .setContentText(listSongs.get(position).getTitle()).setSubText("NOW PLAYING")
                    .setContentIntent(pi)
                    .build();
        }
        nm.notify(NOTIFICATION_ID,notification);
    }
}