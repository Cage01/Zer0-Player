package com.example.mason.mediaplayer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;


/**
 * Created by Mason on 7/5/2015.
 */
public class CategoryVideo extends MediaActivity {
    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_media);
        buttons();
        updateVideos();



    }

    public void updateVideos() {
        File home = new File(VIDEO_PATH);
        if (home.listFiles(new videoFilter()).length > 0) {
            for (File file : home.listFiles(new videoFilter())) {
                videos.add(file.getName());
            }
            ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.video_item, videos);
            setListAdapter(songList);
        }
    }

    public void onListItemClick(ListView list, View view, final int position, long id){
        path = String.valueOf(uriVideo + videos.get(position));
        vidCount = position;
       // Intent intent = new Intent(this, PlayVideo.class);
      //  startActivity(intent);
        playVid(path);
    }



    //the method that switches to the activity where the video is played
    public void playVid(String path) {
        Intent intent = new Intent(this, PlayVideo.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
}
