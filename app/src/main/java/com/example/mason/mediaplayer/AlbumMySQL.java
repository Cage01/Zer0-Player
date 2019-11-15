package com.example.mason.mediaplayer;

/**
 * Created by Mason on 8/25/2015.
 */
public class AlbumMySQL {

    private Integer id;
    private String albumName;
    private Integer artistID;

    public AlbumMySQL(Integer id, String name, Integer artistID) {
        this.id = id;
        this.albumName = name;
        this.artistID = artistID;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public void setAlbumName(String name) {
        this.albumName = name;
    }

    public void setArtistID(Integer id) {
        this.artistID = id;
    }

    public Integer getID() {
        return this.id;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public Integer getArtistID() {
        return this.artistID;
    }

}
