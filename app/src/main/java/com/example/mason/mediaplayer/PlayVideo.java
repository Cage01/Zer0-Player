package com.example.mason.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Mason on 7/5/2015.
 */
public class PlayVideo extends Activity {

    VideoView vid;
    String path;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        vid = (VideoView) findViewById(R.id.videoView);
        //setting the screen that the video will be placed in

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        Log.i("path of video file....", path); //retrieving the path to the video from the
        uri = Uri.parse(path);                //CategoryVideo class through the intent
        vid.setVideoURI(uri);

        vid.setMediaController(new MediaController(this)); //allows for play, pause, rewind etc
        vid.requestFocus();

        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vid.start();
            }
        });

        vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(PlayVideo.this, CategoryVideo.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();

        vid.pause();
    }

    @Override
    public void onResume(){
        super.onResume();

        vid.start();
    }
}
