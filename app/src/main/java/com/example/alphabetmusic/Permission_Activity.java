package com.example.alphabetmusic;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.Objects;

public class Permission_Activity extends AppCompatActivity {
    private static final int REQ_CODE=100;
    Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        main=new Intent(getApplicationContext(),MainActivity.class);

        if (checkPermission()){
            startActivity(main);
        }else{
            ActivityCompat.requestPermissions(Permission_Activity.this,new String[]{READ_EXTERNAL_STORAGE},REQ_CODE);

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_CODE){
            if(grantResults.length>0){
                int storagePermission=grantResults[0];
                boolean check=storagePermission==PackageManager.PERMISSION_GRANTED;
                if (check){
                    startActivity(main);
                }else{
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean checkPermission(){

        int result=ActivityCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE);
        return result== PackageManager.PERMISSION_GRANTED;


    }


}