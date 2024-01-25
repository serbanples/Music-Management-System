import com.mysql.cj.exceptions.ConnectionIsClosedException;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.lang.model.type.ArrayType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminManagementSystem {
    private Song[] SongsInCSV = store_songs();
    private Album[] AlbumsInCSV = store_albums();
    OutputDevice od = new OutputDevice();
    Scanner scanner = new Scanner(System.in);
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jdbc-music";
    private static final String username = "root";
    private static final String password = "Gtrfcvbtre70*";

    public Song[] getSongsInCSV() {
        return SongsInCSV;
    }

    private String secondsToMinutes(int seconds){
        int minutes = 0;
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        if(seconds < 10) return minutes + ":0" + seconds;
        else return minutes + ":" + seconds;
    }
    private String secondsToHours(int seconds){
        int hours = 0;
        int minutes = 0;
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        while(minutes >= 60){
            hours++;
            minutes -= 60;
        }
        if(hours == 0) return minutes + "minutes";
        else return hours + " hour and " + minutes + " minutes";
    }

    public void add_song_to_DB() {
        od.printMessageNl("Enter data to store in the Database (type 'exit' to finish):");
        while (true) {
            od.printMessage("Enter Song Name: ");
            String songName = scanner.nextLine();
            if (songName.equalsIgnoreCase("exit")) {
                break;
            }

            od.printMessage("Enter Artist Name: ");
            String ArtistName = scanner.nextLine();

            od.printMessage("Enter Album Name: ");
            String albumName = scanner.nextLine();

            od.printMessage("Enter Song Length: ");
            int length = scanner.nextInt();

            try {
                Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
                String sqlQuery = "INSERT INTO songs (songname, songartist, songalbum, albumid, songlength) VALUES (?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, songName);
                preparedStatement.setString(2, ArtistName);
                preparedStatement.setString(3, albumName);
                preparedStatement.setString(4, "1");
                preparedStatement.setString(5, Integer.toString(length));
                preparedStatement.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }

            od.printMessageNl("Data has been saved into the Database.");
        }
    }
    public void add_album_to_DB() {
        od.printMessageNl("Enter data to store in the Database (type 'exit' to finish):");
        while (true) {
            od.printMessage("Enter Album Name: ");
            String AlbumName = scanner.nextLine();
            if (AlbumName.equalsIgnoreCase("exit")) {
                break;
            }

            od.printMessage("Enter Album Genre: ");
            String genre = scanner.nextLine();

            od.printMessage("Enter Album Artist: ");
            String ArtistName = scanner.nextLine();

            try {
                Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
                String sqlQuery = "INSERT INTO albums (albumname, albumartist, albumgenre) VALUES (?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, AlbumName);
                preparedStatement.setString(2, ArtistName);
                preparedStatement.setString(3, genre);
                preparedStatement.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }

            od.printMessageNl("Enter Album Song List: ");
            int i = 1;
            while (true) {
                od.printMessage("Enter Song Name: ");
                String songName = scanner.nextLine();
                if (songName.equalsIgnoreCase("exit")) {
                    break;
                }

                od.printMessage("Enter Song Length: ");
                int length = scanner.nextInt();
                scanner.nextLine();

                try {
                    Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
                    String sqlQuery = "INSERT INTO songs (songname,songartist,songalbum,albumid,songlength) VALUES (?,?,?,?,?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, songName);
                    preparedStatement.setString(2, ArtistName);
                    preparedStatement.setString(3, AlbumName);
                    preparedStatement.setString(4, Integer.toString(i++));
                    preparedStatement.setString(5, Integer.toString(length));
                    preparedStatement.executeUpdate();
                }catch (Exception e){
                    e.printStackTrace();
                }

                od.printMessageNl("Data has been saved in the Database");
            }
            od.printMessageNl("Data has been saved in the Database");
        }
    }
    public Song[] store_songs(){
        ArrayList<Song> songList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("SongDataBase.csv"))){
            reader.readLine(); //skip header

            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                Song song = new Song(data[0], Integer.parseInt(data[1]), data[2], data[3]);
                songList.add(song);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return songList.toArray(new Song[0]);
    }
    public Album[] store_albums(){
        ArrayList<Album> albumList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("AlbumDataBase.csv"))){
            reader.readLine(); //skip header

            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                String[] songs = data[1].split("#");
                ArrayList<Song> songList = new ArrayList<>();
                for(String s : songs){
                    String[] songData = s.split("@");
                    songList.add(new Song(songData[0], Integer.parseInt(songData[1]), songData[2], songData[3]));
                }
                Album album = new Album(data[0], songList, data[2], data[3]);
                albumList.add(album);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return albumList.toArray(new Album[0]);
    }
    public void print_songs(){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from songs";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            od.printMessageNl("Exisitng songs in the Database:");
            int i = 1;
            while(resultSet.next()){
                String songName = resultSet.getString("songname");
                String artistName = resultSet.getString("songartist");
                int length = resultSet.getInt("songlength");
                od.printMessageNl(i++ + ". " + songName + "\t\t" + secondsToMinutes(length) + "\n" + artistName);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void print_albums(){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from albums";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            od.printMessageNl("Existing albums in the Database:");
            int i = 1;
            while (resultSet.next()) {
                int albumLength = 0;
                String albumName = resultSet.getString("albumname");
                String artistName = resultSet.getString("albumartist");
                String albumGenre = resultSet.getString("albumgenre");
                od.printMessageNl(i++ + ". " + albumName + "\nby " + artistName + "\t\t" + albumGenre);
                String sqlQuery2 = "select * from songs where songalbum = " + "'" + albumName + "'" + " order by albumid";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery2);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while(resultSet2.next()){
                    String songName = resultSet2.getString("songname");
                    int length = resultSet2.getInt("songlength");
                    int albumId = resultSet2.getInt("albumid");
                    od.printMessageNl(albumId + ". " + songName + "\t" + secondsToMinutes(length));
                    albumLength = albumLength + length;
                }
                od.printMessageNl(secondsToHours(albumLength));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void load(){
        store_songs();
        store_songs();
    }
    public void addSong(String songName, String ArtistName, String albumName, int length,int id){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "INSERT INTO songs (songname, songartist, songalbum, albumid, songlength) VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, songName);
            preparedStatement.setString(2, ArtistName);
            preparedStatement.setString(3, albumName);
            preparedStatement.setInt(4, id);
            preparedStatement.setInt(5, length);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addAlbum(String AlbumName, String ArtistName, String genre){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "INSERT INTO albums (albumname, albumartist, albumgenre) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, AlbumName);
            preparedStatement.setString(2, ArtistName);
            preparedStatement.setString(3, genre);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Song> printSong(){
        ArrayList<Song> songList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "select * from songs";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String songName = resultSet.getString("songname");
                String songArtist = resultSet.getString("songartist");
                String songAlbum = resultSet.getString("songalbum");
                int songLength = resultSet.getInt("songlength");
                Song song = new Song(songName,songLength,songArtist,songAlbum);
                songList.add(song);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return songList;
    }
    public ArrayList<Album> printAlbum(){
        ArrayList<Album> albumList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "select * from albums";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ArrayList<Song> songList = new ArrayList<>();
                String albumName = resultSet.getString("albumname");
                String albumArtist = resultSet.getString("albumartist");
                String albumGenre = resultSet.getString("albumgenre");
                String sqlQuery2 = "select * from songs where songalbum = '" + albumName + "' order by albumid";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery2);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while(resultSet2.next()){
                    String songName = resultSet2.getString("songname");
                    String songArtist = resultSet2.getString("songartist");
                    String songAlbum = resultSet2.getString("songalbum");
                    int songLength = resultSet2.getInt("songlength");
                    Song song = new Song(songName,songLength,songArtist,songAlbum);
                    songList.add(song);
                }
                Album album = new Album(albumName,songList,albumGenre,albumArtist);
                albumList.add(album);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return albumList;
    }

    public void createPlaylist(String albumName){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "INSERT INTO playlist (playlistid,playlistname,userid) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,countPlaylist()+1);
            preparedStatement.setString(2,albumName);
            preparedStatement.setInt(3,getCurrentUserId());
            preparedStatement.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deletePlaylist(String playlistName){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "DELETE FROM playlist WHERE playlistname = '" + playlistName + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Song> viewPlaylist(String playlistName) {
        ArrayList<Song> songList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "SELECT * FROM playlist_songs WHERE playlistname = '" + playlistName + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String songName = resultSet.getString("songname");
                String songArtist = resultSet.getString("songartist");
                String songAlbum = resultSet.getString("songalbum");
                int songLength = resultSet.getInt("songlength");
                Song song = new Song(songName,songLength,songArtist,songAlbum);
                songList.add(song);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return songList;
    }

    public void addSongToPlaylist(String songName,String playlistName){
        try {
            String artist = null;
            String album = null;
            int length = 0;
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "SELECT * FROM songs WHERE songname = '" + songName + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                artist = resultSet.getString(2);
                album = resultSet.getString(3);
                length = resultSet.getInt(5);
            }
            String sqlQuery2 = "INSERT INTO playlist_songs (playlistname,songname,songartist,songalbum,songlength) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery2);
            preparedStatement2.setString(1,playlistName);
            preparedStatement2.setString(2,songName);
            preparedStatement2.setString(3,artist);
            preparedStatement2.setString(4,album);
            preparedStatement2.setInt(5,length);
            preparedStatement2.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void removeSongFromPlaylist(String songName,String playlistName){
        try{
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "DELETE FROM playlist_songs WHERE songname = '" + songName + "' and playlistname = '" + playlistName + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
        } catch(Exception e){

        }
    }
    public void saveUserCredentials(String name, String email, String pasword, String accountType) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "INSERT INTO accounts (name,email,password,accounttype) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,pasword);
            preparedStatement.setString(4,accountType);
            preparedStatement.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public String checkUserCredentials(String email, String pasword) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (email.equals(resultSet.getString("email")) && pasword.equals(resultSet.getString("password")))
                    return resultSet.getString("accounttype");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no";
    }
    public boolean isValidEmail(String email){
        return email.contains("@") && email.contains(".com");
    }
    public boolean isValidRegister(String name, String email, String pasword) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (name.equals(resultSet.getString("name")) && email.equals(resultSet.getString("email")) && pasword.equals(resultSet.getString("password")))
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean isValidLogin(String email, String password) {
        return email != null && email.contains("@") && password != null && !password.isEmpty();
    }
    public String isValidSong(String songname,String artistname,String albumname, int length){
        if (songname.isEmpty()) {
            return "Song Name cannot be empty";
        }
        if (artistname.isEmpty()) {
            return "Song Artist cannot be empty";
        }
        if (albumname.isEmpty()) {
            return "Song Album cannot be empty";
        }
        if (length <= 0) {
            return "Song Length must be a positive integer";
        }
        return "y";
    }

    public String isValidAlbum(String albumname,String artistname,String albumgenre){
        if (albumname.isEmpty()) {
            return "Album Name cannot be empty";
        }
        if (artistname.isEmpty()) {
            return "Album Artist cannot be empty";
        }
        if (albumgenre.isEmpty()) {
            return "Album Genre cannot be empty";
        }
        return "y";
    }
    public boolean SongAlreadyInDB(String songname,String artistname,String albumname, int length){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from songs";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (songname.equals(resultSet.getString("songname")) && artistname.equals(resultSet.getString("artistname")) && albumname.equals(resultSet.getString("albumname")) && length == resultSet.getInt("songlength"));
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean AlbumAlreadyInDB(String albumname,String artistname,String albumgenre){
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
            String sqlQuery = "select * from albums";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (albumname.equals(resultSet.getString("albumname")) && artistname.equals(resultSet.getString("albumartist")) && albumgenre.equals(resultSet.getString("albumgenre")));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private int getCurrentUserId(){
        int id = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,username,password);
            String sqlQuery = "select USER() as username";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Assuming the username is in the format 'username@localhost'
                String fullUsername = resultSet.getString("username");
                String username = fullUsername.split("@")[0];

                // Query the users table to get the corresponding user ID
                String getUserIdQuery = "SELECT id FROM users WHERE username = '" + username + "'";
                try (PreparedStatement getUserIdStatement = connection.prepareStatement(getUserIdQuery)) {
                    getUserIdStatement.setString(1, username);

                    try (ResultSet userIdResultSet = getUserIdStatement.executeQuery()) {
                        if (userIdResultSet.next()) {
                            id = userIdResultSet.getInt("id");
                        } else {
                            // Handle the case when the user is not found (return a default value or throw an exception)
                            throw new IllegalStateException("User not found in the database");
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }
    private int countPlaylist() {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(playlistname) FROM playlist");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error counting playlists from the database", e);
        }
        return count;
    }
}
