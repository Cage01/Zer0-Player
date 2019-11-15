package com.example.mason.mediaplayer;

import android.annotation.TargetApi;
import android.app.Activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by Mason on 7/30/2015.
 */


public class NowPlayingScreen extends Activity implements LocationListener{
    public int position;
    public Uri uriMusicShow = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public ArrayList<Song> songList = new ArrayList<Song>(); //showing the meta data
    ImageView albumArt;
    TextView artist, title,album;
    public static boolean active = false;
    private boolean readyToWrite =  false;
    String thisArtist, thisTitle, thisAlbum;
    String path;
    Uri uri;
    MySQLAccess db = new MySQLAccess();

    public MediaPlayer mp = new MediaPlayer();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/neuropol.ttf");
        setContentView(R.layout.now_playing);

        TextView artist = (TextView)findViewById(R.id.artist_info);
        TextView song = (TextView)findViewById(R.id.song_info);
        TextView album = (TextView)findViewById(R.id.album_info);

        artist.setSelected(true);
        song.setSelected(true);
        album.setSelected(true);

        artist.setTypeface(font);
        song.setTypeface(font);
        album.setTypeface(font);

        try {db.readArtists();
             db.readAlbums();
             db.readSongs();
        } catch (Exception e) {
            e.printStackTrace();
        }


        getSongList(); //brings in jthe data from the phone





        Intent intent = getIntent(); //brings in the path for the song that was clicked on the list screen

    path = intent.getStringExtra("path");

    //checking if path is null, and if its not, it starts the player


    position = intent.getIntExtra("position", position);
        mediaPlayer();
        currentlyPlaying(position);
        nowPlayingButtons();



        System.out.println(position);
            uri = Uri.parse(path);


        //activates the gps reader, refreshes in miliseconds ie. 1000 = 1 second



    LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    //first number, amount of time .. second number, minimum distance
    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
    this.onLocationChanged(null);








    }

    //controls all the buttons within the screen
    public void nowPlayingButtons() {

        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        final Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);
        final Button playPauseButton = (Button) findViewById(R.id.playPauseButton);
        Button skipButton = (Button) findViewById(R.id.skipButton);
        Button previousButton = (Button) findViewById(R.id.previousButton);


        playPauseButton.setOnClickListener(new View.OnClickListener() {
            //makes the play and pause button essentially the same button, transforms
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                    mp.pause();
                }else{
                    playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    mp.start();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                boolean restart = false;

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


                    if (mp.isPlaying()) {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    }else{
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                    }


                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
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


                    if (mp.isPlaying()) {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    }else{
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                    }

                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            //skips the player to the next song
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {



                if (position < songList.size()-1){

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


                    if (mp.isPlaying()) {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    }else{
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));}
                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{

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


                    if (mp.isPlaying()) {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    }else{
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.play_button));}

                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
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
                    } catch (NullPointerException e) {

                    }
                    mp.pause();
                } else {
                    try {
                        playPauseButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    } catch (NullPointerException e) {

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

                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
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

                    try {
                        currentlyPlaying(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });



    }

    //shows all the metadata for the song thats currently playing
    private void currentlyPlaying(final int position){

        albumArt = (ImageView) findViewById(R.id.album_art);
        album = (TextView) findViewById(R.id.album_info);
        artist = (TextView) findViewById(R.id.artist_info);
        title = (TextView) findViewById(R.id.song_info);
        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
    String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;


        Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);

        musicCursor.moveToPosition(position);
        int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);


         thisAlbum = musicCursor.getString(albumColumn);
        thisTitle = musicCursor.getString(titleColumn);
        thisArtist = musicCursor.getString(artistColumn);
        album.setText(thisAlbum);
        artist.setText(thisArtist);
        title.setText(thisTitle);







        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    //TODO your background code
                    databaseWrite(thisArtist, thisTitle, thisAlbum);

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




    @Override
    public void onStart(){
        super.onStart();
        active = false;
    }



@Override
public void onBackPressed(){
    mp.release();
    finish();
}


    public void databaseWrite(String thisArtist, String thisTitle, String thisAlbum){
        // See if this artist is in the database and add them if not.


        Boolean artistExist = db.artistExists(thisArtist);
        if (!artistExist) {
            db.addArtist(thisArtist);
        }


        // See if this album is in the database and add if not.
        Boolean albumExist = db.albumExists(thisAlbum);
        if (!albumExist) {
            Integer thisArtistID = db.getArtistID(thisArtist);
            db.addAlbum(thisAlbum, thisArtistID);
        }


        // See if the song exists in the database and add it if it isn't.
        Integer thisArtistID = db.getArtistID(thisArtist);
        Integer thisAlbumID = db.getAlbumID(thisAlbum);
        Boolean songExist = db.songExists(thisTitle, thisAlbumID, thisArtistID);
        if (!songExist) {
            db.addSong(thisTitle, thisAlbumID, thisArtistID);
         //   System.out.println(thisTitle + " has been added to the database.");
        } else {
         //   System.out.println(thisTitle + " by " + thisArtist + " on the " + thisAlbum + " album already exists in the database.");
            Integer thisSongID = db.getSongID(thisTitle, thisArtistID, thisAlbumID);
         //   System.out.println("The song ID is: " + thisSongID);
            Integer thisPlayCount = db.getPlayCount(thisSongID);
            thisPlayCount++;
            try {
                db.updatePlayCount(thisPlayCount, db.getSongID(thisTitle, thisArtistID, thisAlbumID));
            } catch (SQLException e) {
                e.printStackTrace();
            }
         //   System.out.println("Play count for " + thisTitle + " has been updated to " + thisPlayCount + ".");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        final Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);
        musicCursor.moveToPosition(position);
        String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        if (location == null) {
        } else {

            float nCurrentSpeed = location.getSpeed();
            if(nCurrentSpeed >= 4.5 && !active){

                active = true;
                Intent intent = new Intent(this, SafeMode.class);
                intent.putExtra("path", path1);
                intent.putExtra("position", position);
                startActivity(intent);
                mp.stop();




            }
        }
    }




    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }












    }
