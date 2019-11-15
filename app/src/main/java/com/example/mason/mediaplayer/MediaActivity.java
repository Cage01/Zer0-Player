package com.example.mason.mediaplayer;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name){
        return (name.endsWith(".mp3"));
    }
}

class videoFilter implements FilenameFilter{
    public boolean accept(File dir, String name){
        return (name.endsWith(".mp4") || name.endsWith(".wmv") || name.endsWith(".3gp"));
    }
}

//TODO make songs stop when video is played
//TODO fix bug that allows first song to start after clicking a song on the list


public class MediaActivity extends ListActivity {
    Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/");
    public Uri uriMusicPlay = Uri.parse("/storage/emulated/0/MUSIC/");
    public Uri uriMusicShow = MediaStore.Audio.Media.getContentUriForPath("/storage/emulated/0/MUSIC/");
    public Uri uriVideo = Uri.parse("/storage/emulated/0/VIDEOS/");
    private static final String SD_PATH = new String(Environment.getExternalStorageDirectory().getPath() + "/");
    public static final String MUSIC_PATH = new String("/storage/emulated/0/MUSIC/");
    public static final String VIDEO_PATH = new String("/storage/emulated/0/VIDEOS/");


    public int songCount;
    public int vidCount;
    private boolean isPaused = false;
    int pos;









    public List<String> videos = new ArrayList<String>();
    public List<String> songs = new ArrayList<String>(); //list that plays
    private MediaPlayer mp = new MediaPlayer();

    public ArrayList<Song> songList = new ArrayList<Song>(); //showing the meta data



    public void buttonChange(){

        final Button playPauseButton = (Button) findViewById(R.id.playPauseButton);
        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setVisibility(View.GONE);
        startButton.setClickable(false);
        playPauseButton.setVisibility(View.VISIBLE);
        playPauseButton.setClickable(true);


    }


    //plays the files based on being clicked
    @Override
    public void onListItemClick(ListView list, View view, final int position, long id) {
        songCount = position;
        try {

            mp.reset(); //wipes currently playing

            mp.setDataSource(uriMusicPlay + songs.get(position));//tells device where the file is located

            mp.prepare();
            mp.start();//plays the file
            mp.isPlaying();

        } catch (IOException e) {
            Log.v(getString(R.string.app_name), e.getMessage());
        }



        buttonChange();

        //sets the original play button to the play/pause button since the beginning and the play/pause have different functions
       /*
        */






        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mp) {
                //on completion of the song, it plays the very next in the array of songs
                songCount = position;
                if (songCount < songs.size()) {

                    try {

                        mp.reset(); //wipes currently playing
                        mp.setDataSource(uriMusicPlay + songs.get(++songCount));//tells device where the file is located

                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();
                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }
                    buttons();
                }


            }
        });
    }


    public void buttons() {
        final Button playPauseButton = (Button) findViewById(R.id.playPauseButton);
        final Button startButton = (Button) findViewById(R.id.startButton);
        final Button videoCategoryButton = (Button) findViewById(R.id.videoCategoryButton);
        final Button songCategoryButton = (Button) findViewById(R.id.songCategoryButton);
        Button skipButton = (Button) findViewById(R.id.skipButton);


        videoCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, CategoryVideo.class);
                mp.release();
                startActivity(intent);

            }
        });

        songCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, CategorySongs.class);
                mp.release();
                startActivity(intent);



            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {

            //starts up the player, and plays the first song in the playlist
            @Override
            public void onClick(View v) {

                mp.reset();
                try {
                    mp.setDataSource(uriMusicPlay + songs.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
                mp.isPlaying();

                isPaused = false;

                int pos1 = 0;
                pos = pos1 + 1;
                songCount = 0;
                //changes the button whether or not a song has been playing previously or not
                buttonChange();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(final MediaPlayer mp) {
                        //on completion of the song, it plays the very next in the array of songs
                        songCount = 0;
                        if (songCount < songs.size()) {

                            try {

                                mp.reset(); //wipes currently playing
                                mp.setDataSource(uriMusicPlay + songs.get(++songCount));//tells device where the file is located

                                mp.prepare();
                                mp.start();//plays the file
                                mp.isPlaying();
                            } catch (IOException e) {
                                Log.v(getString(R.string.app_name), e.getMessage());
                            }
                            buttons();
                        }


                    }
                });


            }
        });


        skipButton.setOnClickListener(new View.OnClickListener() {

            //skips the player to the next song
            @Override
            public void onClick(View v) {

                if (songCount < songs.size() ) {

                    try {

                        mp.reset(); //wipes currently playing
                        mp.setDataSource(uriMusicPlay + songs.get(++songCount));//tells device where the file is located

                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();
                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }
                }


            }
        });



        playPauseButton.setOnClickListener(new View.OnClickListener() {


            //makes the play and pause button essentially the same button, transforms

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {

                    isPaused = true;
                    playPauseButton.setText(getResources().getString(R.string.play_button));
                    playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                    mp.pause();
                } else {
                    mp.start();
                    playPauseButton.setText(getResources().getString(R.string.pause_button));
                    playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                }

            }

        });
    }
    public void updateMusic() {

        File home = new File(MUSIC_PATH);
        if (home.listFiles(new mp3Filter()).length > 0) {
            for (File file : home.listFiles(new mp3Filter())) {
                songs.add(file.getName());
            }
            ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.song_item, songs);
            setListAdapter(songList);


        }
    }


    public void updateVideos() {
        File home = new File(VIDEO_PATH);
        if (home.listFiles(new videoFilter()).length > 0) {
            for (File file : home.listFiles(new videoFilter())) {
                videos.add(file.getName());
            }
            ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.song_item, videos);
            setListAdapter(songList);
        }
    }
}
