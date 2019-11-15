package com.example.mason.mediaplayer;

/**
 * Created by Mason on 8/25/2015.
 */
public class SongMySQL {

    private Integer id;
    private String songTitle;
    private Integer playCount;
    private Integer artistID;
    private Integer albumID;

    public SongMySQL(Integer id, String title, Integer count, Integer artist, Integer album) {
        this.id = id;
        this.songTitle = title;
        this.playCount = count;
        this.artistID = artist;
        this.albumID = album;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public void setSongTitle(String title) {
        this.songTitle = title;
    }

    public void setPlayCount(Integer count) {
        this.playCount = count;
    }

    public void setArtistID(Integer id) {
        this.artistID = id;
    }

    public void setAlbumID(Integer id) {
        this.albumID = id;
    }

    public Integer getID() {
        return this.id;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public Integer getPlayCount() {
        return this.playCount;
    }

    public Integer getArtistID() {
        return this.artistID;
    }

    public Integer getAlbumID() {
        return this.albumID;
    }

}
