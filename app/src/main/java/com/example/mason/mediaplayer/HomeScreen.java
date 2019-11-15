package com.example.mason.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Mason on 7/9/2015.
 */
public class HomeScreen extends Activity {
    RelativeLayout relativeLayout;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/neuropol.ttf");
        setContentView(R.layout.home_screen);



        Button songCategory = (Button) findViewById(R.id.songsButton);
        songCategory.setTypeface(font);
        Button videoCategory = (Button) findViewById(R.id.videosButton);
        videoCategory.setTypeface(font);
        Button playlistCategory = (Button) findViewById(R.id.playlistButton);
        playlistCategory.setTypeface(font);

        songCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, CategorySongs.class);
                startActivity(intent);
            }
        });

        videoCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, CategoryVideo.class);
                startActivity(intent);
            }
        });

        playlistCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "No playlists yet!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


    }
}
