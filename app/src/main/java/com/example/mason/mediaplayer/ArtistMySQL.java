package com.example.mason.mediaplayer;

/**
 * Created by Mason on 8/25/2015.
 */
public class ArtistMySQL {

    private Integer id;
    private String artistName;

    public ArtistMySQL() {
        this.id=0;
        this.artistName = null;
    }

    public ArtistMySQL (Integer id, String name) {
        this.artistName = name;
        this.id = id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public void setArtistName(String name) {
        this.artistName = name;
    }

    public Integer getID() {
        return this.id;
    }

    public String getArtistName() {
        return this.artistName;
    }

}
