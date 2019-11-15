package com.example.mason.mediaplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mason on 7/30/2015.
 */
public class SafeMode extends Activity {
    String path;
    Uri uri;
    boolean stop = true;
    public MediaPlayer mp = new MediaPlayer();
    public int position;
    public Uri uriMusicShow = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public ArrayList<Song> songList = new ArrayList<Song>(); //showing the meta data
    ImageView albumArt;
    TextView artist, title,album;
    public static boolean active = false;
    public boolean turn;
    private AudioManager audio;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        setContentView(R.layout.drive_mode);

        getSongList();
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        position = intent.getIntExtra("position", position);
        uri = Uri.parse(path);

        mediaPlayer();


        safeModeButtons();


    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Exit Safe Mode?");
        builder1.setMessage("This function is for your safety! are you sure you want to exit?");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.icon);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(SafeMode.this, NowPlayingScreen.class);

                        intent.putExtra("path", path);
                        intent.putExtra("position", position);
                        startActivity(intent);
                        mp.stop();
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }


    public void safeModeButtons(){
        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        final Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);
        final Button playPauseButton = (Button) findViewById(R.id.ppSafe);
        Button skipButton = (Button) findViewById(R.id.skipSafe);
        Button previousButton = (Button) findViewById(R.id.previousSafe);
        Button volumeDown = (Button) findViewById(R.id.volumeDown);
        Button volumeUp = (Button) findViewById(R.id.volumeUp);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE);

            }
        });

        volumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_VIBRATE);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();

                } else {
                    mp.start();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {


                if (position < songList.size() - 1) {

                    position++;
                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }


                } else {

                    position = 0;
                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }
                    Context context = getApplicationContext();
                    CharSequence text = "Restarting Playlist!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {


                if (position > 0) {
                    position--;
                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }


                } else {
                    position = 0;
                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }


                }
            }
        });
    }


    private void getSongList(){
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"; //removes everything in the list that isnt music
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);

        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            Long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));


            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisTitle,thisArtist, thisId));
            }
            while (musicCursor.moveToNext());

        }
    }

    public void mediaPlayer(){

        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        final Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);
        musicCursor.moveToPosition(position);
        String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));


        try {
            mp.reset(); //wipes currently playing
            mp.setDataSource(path1);
            mp.prepare();
            mp.start();//plays the file
            mp.isPlaying();

        } catch (IOException e) {
            Log.v(getString(R.string.app_name), e.getMessage());
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCompletion(final MediaPlayer mp) {
                final Button playPauseButton = (Button) findViewById(R.id.playPauseButton);

                //sets the right image for the play and pause buttons

                if (mp.isPlaying()) {
                    try {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                    }catch (NullPointerException e){

                    }
                    mp.pause();
                } else {
                    try {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    }catch (NullPointerException e){

                    }
                    mp.start();
                }


                //on completion of the song, it plays the very next in the array of songs
                ContentResolver musicResolver = getContentResolver();
                String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
                String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER; //sets alphabetically
                final Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);

                if (position < songList.size() - 1) {
                    position++;

                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }


                } else {
                    position = 0;
                    musicCursor.moveToPosition(position);
                    final String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    try {
                        mp.reset(); //wipes currently playing
                        mp.setDataSource(path1);
                        mp.prepare();
                        mp.start();//plays the file
                        mp.isPlaying();

                    } catch (IOException e) {
                        Log.v(getString(R.string.app_name), e.getMessage());
                    }
                    Context context = getApplicationContext();
                    CharSequence text = "Restarting Playlist!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                }
            }
        });



    }

}
