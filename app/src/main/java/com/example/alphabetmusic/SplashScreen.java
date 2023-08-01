package com.example.alphabetmusic;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
                //redirecting to main activity, if the permissions are granted
                if(checkPermission()){
                    Intent main=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(main);
                    //to remove the activity from the stack
                    finish();
                }else{
                    startActivity(permissionActivity);
                    //to remove the activity from the stack
                    finish();
                }
            }
        }, 3000);
        //for splash screen animation
        Animation animate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        imgAnim.startAnimation(animate);
    }

    public boolean checkPermission() {
        //CHECKING READ PERMISSION
        int getReadPermission = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        //CHECKING WRITE PERMISSION
        int getWritePermission = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        return getReadPermission == PackageManager.PERMISSION_GRANTED&&getWritePermission == PackageManager.PERMISSION_GRANTED;

    }

}