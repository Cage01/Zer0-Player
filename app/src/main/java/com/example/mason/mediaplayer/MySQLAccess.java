package com.example.mason.mediaplayer;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Mason on 8/25/2015.
 */
public class MySQLAccess {


    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    // The following methods are for reading from the database.

    // Check if artist exists in the database.
    public boolean artistExists(String input) {
        // Set initial answer to no
        Boolean exists = false;
        openDatabase();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM zer0stats.artists");
            while (resultSet.next()) {
                String temp = resultSet.getString("artistName");
                if (input.equals(temp)) exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Check if artist exists in the database.
    public boolean albumExists(String input) {
        // Set initial answer to no
        Boolean exists = false;
        openDatabase();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM zer0stats.albums");
            while (resultSet.next()) {
                String temp = resultSet.getString("albumName");
                if (input.equals(temp)) exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Check if artist exists in the database.
    public boolean songExists(String songName, Integer album, Integer artist) {
        // Set initial answer to no
        Boolean exists = false;
        openDatabase();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM zer0stats.songs");
            while (resultSet.next()) {
                String tempName = resultSet.getString("songTitle");
                Integer tempArtistID = resultSet.getInt("fk_artistID");
                Integer tempAlbumID = resultSet.getInt("albumID");
                if (songName.equals(tempName) && album.equals(tempAlbumID) && artist.equals(tempArtistID)) exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // This is used when you need to get the database primary key (row) id for an artist.
    public Integer getPlayCount(Integer song) {
        Integer playCount = 0;
        // Open the database.
        openDatabase();
        try {
            String query = "select playCount from songs where id = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setInt(1,  song);
            ResultSet result = statement.executeQuery();
            result.first();
            playCount = result.getInt("playCount");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return playCount;
    }

    // This is used when you need to get the database primary key (row) id for an artist.
    public Integer getArtistID(String name) {
        Integer artistID = 0;
        // Open the database.
        openDatabase();
        try {
            String query = "select id from artists where artistName = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  name);
            ResultSet result = statement.executeQuery();
            result.first();
            artistID = result.getInt("id");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return artistID;
    }

    // This is used when you need to get the database primary key (row) id for an artist.
    public Integer getAlbumID(String name) {
        Integer albumID = 0;
        // Open the database.
        openDatabase();
        try {
            String query = "select id from albums where albumName = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  name);
            ResultSet result = statement.executeQuery();
            result.first();
            albumID = result.getInt("id");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return albumID;
    }

    // This is used when you need to get the database primary key (row) id for an artist.
    public Integer getSongID(String title, Integer artist, Integer album) {
        // Open the database.
        Integer songID = 0;
        openDatabase();
        try {
            String query = "select id from songs where songTitle = ? AND albumID = ? AND fk_artistID = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  title);
            statement.setInt(2, album);
            statement.setInt(3, artist);
            ResultSet result = statement.executeQuery();
            result.first();
            songID = result.getInt("id");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return songID;
    }

    // Read data from the Artist table.
    public void readArtists() throws Exception {
        // Open a connection to the database.
        openDatabase();
        try {
            // Statements allow you to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set gets the result of the SQL query.  This query gets all of the data from the artists table
            // and orders it alphabetically by artist name.
            resultSet = statement.executeQuery("select * from zer0stats.artists order by artistName");
            // Moves to the last row of the table to get the number of artists in the table.
            resultSet.last();
            // Assigns the number of artists (or records) in the artists table to a variable.
            Integer totalRows = resultSet.getRow();
            // Create an array of artists equaling the total number of artists in the database.
            ArtistMySQL[] artists = new ArtistMySQL[totalRows];
            // Moves to the beginning of the query results in order to assign the results to local variables.
            resultSet.beforeFirst();
            // Set a counter for the artist array.
            Integer count = 0;
            // Create a header for the result set display in the console.
            System.out.println("\nArtists read from the database:");
            System.out.println("-------------------------------");

            while (resultSet.next()) {
                // Populate the local artist array.
                artists[count] = new ArtistMySQL(resultSet.getInt("id"), resultSet.getString("artistName"));
                // Display the results of the import in the console.
                System.out.println(artists[count].getID() + " \t " + artists[count].getArtistName());
                // Increment the counter so that we can populate the next artist in the array with the next record result.
                count++;
            }
            // Now catch any exceptions.
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    // Read all album data from the database.
    public void readAlbums() throws Exception {
        // Open a new connection to the database.
        openDatabase();
        try {
            // Statements allow you to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set gets the result of the SQL query.  This query gets all of the data from the artists table
            // and orders it alphabetically by artist name.
            resultSet = statement.executeQuery("select * from zer0stats.albums order by albumName");
            // Moves to the last row of the table to get the number of artists in the table.
            resultSet.last();
            // Assigns the number of artists (or records) in the artists table to a variable.
            Integer totalRows = resultSet.getRow();
            // Create an array of artists equaling the total number of artists in the database.
            AlbumMySQL[] albums = new AlbumMySQL[totalRows];
            // Moves to the beginning of the query results in order to assign the results to local variables.
            resultSet.beforeFirst();
            // Set a counter for the artist array.
            Integer count = 0;
            // Create a header for the result set display in the console.
            System.out.println("\nAlbums read from the database:");
            System.out.println("-------------------------------");

            while (resultSet.next()) {
                // Populate the local artist array.
                albums[count] = new AlbumMySQL(resultSet.getInt("id"), resultSet.getString("albumName"), resultSet.getInt("artistID"));
                // Display the results of the import in the console.
                System.out.println(albums[count].getID() + " \t " + albums[count].getAlbumName());
                // Increment the counter so that we can populate the next artist in the array with the next record result.
                count++;
            }
            // Now catch any exceptions.
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    // Read all data about the songs in the database.
    public void readSongs() throws Exception {
        // Open a connection to the database.
        openDatabase();

        try {
            // Statements allow you to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set gets the result of the SQL query.  This query gets all of the data from the artists table
            // and orders it alphabetically by artist name.
            resultSet = statement.executeQuery("select * from zer0stats.songs order by songTitle");
            // Moves to the last row of the table to get the number of artists in the table.
            resultSet.last();
            // Assigns the number of artists (or records) in the artists table to a variable.
            Integer totalRows = resultSet.getRow();
            // Create an array of artists equaling the total number of artists in the database.
            SongMySQL[] songs = new SongMySQL[totalRows];
            // Moves to the beginning of the query results in order to assign the results to local variables.
            resultSet.beforeFirst();
            // Set a counter for the artist array.
            Integer count = 0;
            // Create a header for the result set display in the console.
            System.out.println("\nSongs read from the database:");
            System.out.println("-------------------------------");

            while (resultSet.next()) {
                // Populate the local artist array.
                songs[count] = new SongMySQL(resultSet.getInt("id"), resultSet.getString("songTitle"), resultSet.getInt("playCount"), resultSet.getInt("fk_artistID"), resultSet.getInt("albumID"));
                // Display the results of the import in the console.
                System.out.println(songs[count].getID() + " \t " + songs[count].getSongTitle() + " \t " + songs[count].getPlayCount() + " plays.");
                // Increment the counter so that we can populate the next artist in the array with the next record result.
                count++;
            }
            // Now catch any exceptions.
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    // The following methods are for writing to the database.
    public void updatePlayCount(Integer count, Integer songID) throws SQLException {
        openDatabase();
        // Set up the update statement.
        String updateSQLStatement = "UPDATE songs SET playCount = ? " + " WHERE id = ?";
        PreparedStatement sqlStatement = connect.prepareStatement(updateSQLStatement);
        sqlStatement.setInt(1, count);
        sqlStatement.setInt(2,  songID);
        // Perform the update.
        sqlStatement.executeUpdate();
        close();
    }

    // This method adds an artist to the Artist table.
    public void addArtist(String name) {
        openDatabase();
        // Set up the insert SQL statement.
        try {
            String query = "insert into artists" + "(artistName) VALUES" + "(?)";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  name);
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // This method adds an album to the database
    public void addAlbum(String name, Integer artistID) {
        openDatabase();
        // Set up the insert SQL statement.
        try {
            String query = "insert into albums" + "(albumName, artistID) VALUES" + "(?,?)";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  name);
            statement.setInt(2, artistID);
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // This method adds a song to the database.
    public void addSong(String title, Integer albumID, Integer artistID) {
        openDatabase();
        // Set up the insert SQL statement.
        try {
            String query = "insert into songs" + "(songTitle, albumID, fk_artistID, playCount) VALUES" + "(?,?,?,?)";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1,  title);
            statement.setInt(2, albumID);
            statement.setInt(3, artistID);
            statement.setInt(4, 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    // Open a connection to the database.
    private void openDatabase() {
        // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);

        // This will load the MySQL driver... each DB has its own driver
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Setup the connection with the DB
        try {
            connect = DriverManager.getConnection("jdbc:mysql://db.zer0player.com:3306/zer0stats?"
                    + "user=zer0player&password=!Zer0P@$$w0rd");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }




}
