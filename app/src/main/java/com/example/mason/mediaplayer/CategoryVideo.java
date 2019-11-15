package com.example.mason.mediaplayer;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;



/**
 * Created by Mason on 7/5/2015.
 */
public class CategoryVideo extends MediaActivity {
    String path;
    private int video_column;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_media);
       // updateVideos();

        ListView videoView = (ListView) findViewById(android.R.id.list);
        VideoAdapter videoAdapter = new VideoAdapter(this, videoList);
        videoView.setAdapter(videoAdapter);

        getVideoList();



    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void getVideoList(){
        //query external audio
        int path;
        ContentResolver videoResolver = getContentResolver();
        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
        Cursor videoCursor = videoResolver.query(uriVideos, null, null, null, sortOrder);
      //  ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);


        //iterate over results if valid
        if (videoCursor != null && videoCursor.moveToFirst()) {
            //get columns
        int titleColumn = videoCursor.getColumnIndex
                (MediaStore.Video.Media.DISPLAY_NAME);

            do {
              /*  path = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);
                String filename = videoCursor.getString(path);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filename, MediaStore.Images.Thumbnails.MICRO_KIND);
                thumbnail.setImageBitmap(thumb);*/

                String thisTitle = videoCursor.getString(titleColumn);
                videoList.add(new Video(thisTitle));


            }
            while (videoCursor.moveToNext());
        }

    }


    public void onListItemClick(ListView list, View view, final int position, long id){
        //query external audio
        ContentResolver videoResolver = getContentResolver();
        // Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
        Cursor videoCursor = videoResolver.query(uriVideos, null, null, null, sortOrder);
        videoCursor.moveToPosition(position);
        video_column = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);
        String filename = videoCursor.getString(video_column);


        vidCount = position;
       // Intent intent = new Intent(this, PlayVideo.class);
      //  startActivity(intent);
        playVid(filename);
    }



    //the method that switches to the activity where the video is played
    public void playVid(String path) {
        Intent intent = new Intent(this, PlayVideo.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
}
