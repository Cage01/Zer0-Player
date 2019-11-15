package com.example.mason.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mason on 7/9/2015.
 */
public class HomeScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/neuropol.ttf");
        setContentView(R.layout.home_screen);



        Button songCategory = (Button) findViewById(R.id.songsButton);
        songCategory.setTypeface(font);
        Button videoCategory = (Button) findViewById(R.id.videosButton);
        videoCategory.setTypeface(font);
        Button zStats = (Button) findViewById(R.id.playlistButton);
        zStats.setTypeface(font);

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

        zStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://zer0player.com/data.cfm"));
                startActivity(intent);
            }
        });


    }



    @Override
    public void onBackPressed(){
        System.exit(0);
    }
}
