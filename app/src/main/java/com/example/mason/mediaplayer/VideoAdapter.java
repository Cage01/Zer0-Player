package com.example.mason.mediaplayer;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mason on 8/12/2015.
 */
public class VideoAdapter extends BaseAdapter {
    //song list and layout
    private Uri uriVideoShow = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    public ArrayList<Video> videos;
    private LayoutInflater videosInf;

    //constructor
    public VideoAdapter(Context c, ArrayList<Video> theVideos) {
        videos = theVideos;
        videosInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //map to song layout
        LinearLayout vidLay = (LinearLayout) videosInf.inflate(R.layout.video_item, parent, false);
        //get title and artist views
        TextView vidView = (TextView) vidLay.findViewById(R.id.video_title);
       // ImageView thumbnail = (ImageView) vidLay.findViewById(R.id.thumbnail);
        //get video using position
        Video currVid = videos.get(position);


        //get title and artist strings
        vidView.setText(currVid.getTitle());
        //set position as tag
        vidLay.setTag(position);
        return vidLay;
    }


}

