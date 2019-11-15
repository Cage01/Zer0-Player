package com.example.mason.mediaplayer;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * Created by Mason on 6/8/2015.
 */

//TODO make songs stop when video is played
//TODO fix bug that allows first song to start after clicking a song on the list
//TODO not all songs in list are playing, the last few ones have issues playing, but show properly


public class MediaActivity extends ListActivity {
    public Uri uriVideos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    public Uri uriMusicShow = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private MediaPlayer mp = new MediaPlayer();
    public ArrayList<Song> songList = new ArrayList<Song>(); //showing the meta data
    public ArrayList<Video> videoList = new ArrayList<Video>();

    public boolean nowPlaying = false;
    public int songCount;
    public int vidCount;







    @Override
    public void onPause(){
        super.onPause();
        if(nowPlaying = false)
        mp.stop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(nowPlaying = false)
        mp.stop();
    }




    //plays the files based on being clicked
    @Override
    public void onListItemClick(ListView list, View view, final int position, long id) {
        songCount = position;

        ListView lv= (ListView) findViewById(android.R.id.list);
        lv.setSelector(R.drawable.list_selector);


        nowPlaying = true;
        //sets the original play button to the play/pause button since the beginning and the play/pause have different functions
       /*
        */
    }


}

