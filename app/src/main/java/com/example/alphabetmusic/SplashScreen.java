package com.example.alphabetmusic;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    ImageView imgAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgAnim = findViewById(R.id.toAnimate);

        Intent permissionActivity = new Intent(getApplicationContext(), Permission_Activity.class);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(checkPermission()){
                    Intent main=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(main);
                    finish();
                }else{
                    startActivity(permissionActivity);
                    finish();
                }
            }
        }, 3000);

        Animation animate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        imgAnim.startAnimation(animate);


    }

    public boolean checkPermission() {

        int result = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;

    }

}