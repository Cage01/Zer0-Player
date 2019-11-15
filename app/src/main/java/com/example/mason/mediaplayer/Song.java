package com.example.mason.mediaplayer;



/**
 * Created by Mason on 7/22/2015.
 */
public class Song {
    private long id;
    private String title;
    private String artist;


    public Song(String songTitle,String songArtist,long songID){
        id=songID;
        title=songTitle;
        artist=songArtist;
    }


    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

}
