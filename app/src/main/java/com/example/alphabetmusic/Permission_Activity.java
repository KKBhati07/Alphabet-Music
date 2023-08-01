package com.example.alphabetmusic;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


//ACTIVITY TO GET STORAGE PERMISSIONS FROM USER
public class Permission_Activity extends AppCompatActivity {
    private static final int REQ_CODE=100;
    Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        main=new Intent(getApplicationContext(),MainActivity.class);

        // if the permissions granted then redirecting to main activity
        if (checkPermission()) startActivity(main);
            else{
            ActivityCompat.requestPermissions(Permission_Activity.this,new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE},REQ_CODE);
        }

    }

    //to request storage permission from user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_CODE){
            if(grantResults.length>0){
                int readPermission=grantResults[0];
                int writePermission=grantResults[1];

                boolean checkRead=readPermission==PackageManager.PERMISSION_GRANTED;
                boolean checkWrite=writePermission==PackageManager.PERMISSION_GRANTED;
                //if the permissions are granted,
                if (checkRead&&checkWrite) startActivity(main);
                    //if not then,
                    else{
                        //showing shut down toast to user
                    Toast.makeText(this, "Permission Denied, SHUTTING DOWN!", Toast.LENGTH_SHORT).show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //shutting down the app with the delay of 4secs
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    },4000);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean checkPermission(){

        //FOR READ PERMISSION
        int getReadPermission=ActivityCompat.checkSelfPermission(Permission_Activity.this,READ_EXTERNAL_STORAGE);

        //FOR WRITE PERMISSION
        int getWritePermission=ActivityCompat.checkSelfPermission(Permission_Activity.this,WRITE_EXTERNAL_STORAGE);

        return getReadPermission== PackageManager.PERMISSION_GRANTED&&getWritePermission==PackageManager.PERMISSION_GRANTED;


    }


}