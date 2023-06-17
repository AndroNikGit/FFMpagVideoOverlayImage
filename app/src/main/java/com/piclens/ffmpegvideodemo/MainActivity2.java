package com.piclens.ffmpegvideodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity2 extends AppCompatActivity {

    private VideoView showvideoview;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        showvideoview=(VideoView)findViewById(R.id.showvideoview);

        Intent intent=getIntent();
        path =intent.getStringExtra("pathlink");
        showvideoview.setVideoPath(path);
        showvideoview.start();
    }
}