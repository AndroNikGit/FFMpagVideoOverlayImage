package com.piclens.ffmpegvideodemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_VIDEO = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private Button video, image, save;
    private String videoPath;
    private VideoView videoview;
    private String picturePath;
    private ImageView setimage;
    private String finalFileName;
    private FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FFmpeg.getInstance(this).isSupported()) {
            // ffmpeg is supported
//            Toast.makeText(this, "ffmpeg Supported", Toast.LENGTH_SHORT).show();
            Log.e("ffmpeg","Supported");
        } else {
            // ffmpeg is not supported
//            Toast.makeText(this, "ffmpeg Not Supported", Toast.LENGTH_SHORT).show();
            Log.e("ffmpeg","Not Supported");
        }

        ffmpeg = FFmpeg.getInstance(MainActivity.this);



        video = (Button) findViewById(R.id.video);
        image = (Button) findViewById(R.id.images);
        save = (Button) findViewById(R.id.savebtn);
        videoview = (VideoView) findViewById(R.id.videoview);
        setimage = (ImageView) findViewById(R.id.setimage);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_VIDEO);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videosave();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            videoview.setVideoPath(videoPath);
            videoview.start();
            Log.e("videopath", "" + videoPath);
            cursor.close();
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.e("imgpath", "" + picturePath);
            setimage.setImageURI(Uri.parse(picturePath));
            videoview.start();
            cursor.close();
        }
    }


    private void videosave() {


        ProgressDialog dialog;
        dialog = new ProgressDialog(MainActivity.this);
        dialog = ProgressDialog.show(MainActivity.this,
                "Saving Video...", "Video is Editing , Please wait...", false, false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        createfilename();

//        String cmd[] = new String[]{"-i", videoPath,
//                "-i", picturePath,
//                "-filter_complex", "[1][0]scale2ref[i][m];[m][i]overlay[v]", "-map", "[v]",
//                "-map", "0:a?", "-ac", "2", "/storage/emulated/0/Download/" + finalFileName + ".mp4"};

//        String cmd[]=new String[]{"-i",videoPath,"-i",picturePath,"-filter_complex","overlay=main_w-overlay_w-10:main_h-overlay_h-10",
//                "-strict","-2", "-r", "30",
//                "/storage/emulated/0/Download/" + finalFileName + ".mp4"};


//        String cmd[]=new String[]{"-i",videoPath,"-i",picturePath,"-filter_complex","[1:v] fade=out:125:25:alpha=1 [intro]; [0:v][intro] overlay [v]",
//                "-map", "[v]", "-map", "0:a", "-acodec", "copy",
//                "/storage/emulated/0/Download/" + finalFileName + ".mp4" };

//        String cmd[]=new String[]{"-i", videoPath,
//                "-vcodec",
//                "libvpx-vp9",
//                "-i", picturePath,
//                "-filter_complex", "overlay",
//                "-preset",
//                "ultrafast",
//                "-crf","30",
//                "/storage/emulated/0/Download/" + "finalvideo11" + ".mp4"};
//        fmpeg -i in.mp4 -r 25 -loop 1 -i intro.png -filter_complex
//    "[1:v] fade=out:125:25:alpha=1 [intro]; [0:v][intro] overlay [v]" -map "[v]" -map 0:a -acodec copy out.mp4
//        -i input.mp4 -i watermark.png -filter_complex "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2" output.mp4

//        -i image.png -i video.mp4 -filter_complex "overlay=(W-w)/2:shortest=1" output.mp4
//        -i /storage/sdcard/BigBuckBunny_320x180.mp4 -i /sdcard/android.png -filter_complex
//                [0:v][1:v]overlay=10:10[out] -map [out] -map 0:a -vcodec h264 -pix_fmt yuv420p /storage/sdcard/overlay.mp4

        //right
                String cmd[]=new String[]{"-i",picturePath,"-i",videoPath,"-filter_complex","[0:v][1:v] overlay=25:25:enable='between(t,0,20)'",
                        "-pix_fmt" ,"yuv420p", "-c:a" ,"copy", "/storage/emulated/0/Download/" + finalFileName + ".mp4" };

        final ProgressDialog finalDialog = dialog;
        final ProgressDialog finalDialog1 = dialog;


        ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
                Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                Log.e("msg", "start");
            }

            @Override
            public void onProgress(String message) {
//                        Toast.makeText(MainActivity.this, "progrees", Toast.LENGTH_SHORT).show();
                Log.e("progress msg", message);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                Log.e("failure msg", message);
                finalDialog1.dismiss();

            }

            @Override
            public void onSuccess(String message) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("pathlink", "/storage/emulated/0/Download/" + finalFileName + ".mp4");
                startActivity(intent);
                Log.e("success msg", message);
                finalDialog.dismiss();
            }

            @Override
            public void onFinish() {

                Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();
                Log.e("finish msg", "finish");

            }

        });
    }

    private void createfilename() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString();
        finalFileName = "Video" + timeStamp;
        Log.e("pathnewvideo==", finalFileName);

    }
}