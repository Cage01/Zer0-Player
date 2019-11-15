package com.example.mason.mediaplayer;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mason on 7/5/2015.
 */



public class CategorySongs extends MediaActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_media);
        ListView songView = (ListView) findViewById(android.R.id.list);
        //retrieve list view
        //get songs from device

            getSongList();


        //create and set adapter
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);


    }

    public void onListItemClick(ListView list, View view, final int position, long id) {
        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setSelector(R.drawable.list_selector);

        ContentResolver musicResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, null);
        musicCursor.moveToPosition(position);
        String path1 = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));


        lv = (ListView) findViewById(android.R.id.list);
        lv.setSelector(R.drawable.list_selector);
        songCount = position;
        System.out.println(songCount);



        playSong(path1);
    }

    public void playSong(String path) {
        Intent intent = new Intent(this, NowPlayingScreen.class);
        intent.putExtra("path", path);
        intent.putExtra("position", songCount);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }


    public void getSongList() {
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Cursor musicCursor = musicResolver.query(uriMusicShow, null, selection, null, sortOrder);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
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



                songList.add(new Song(thisTitle, thisArtist, thisId));

            }
            while (musicCursor.moveToNext());
        }
    }
}











