/*
import java.io.InputStream;
import java.util.Scanner;
class InputDevice {
    private final InputStream inputStream;

    public InputDevice(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String readInput() {
        try {
            Scanner scanner = new Scanner(inputStream);
            System.out.println("Please enter some input: ");
            return scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
*/

import javax.lang.model.type.ArrayType;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;


/// InputDevice class is used to read input from the user and from the file and to generate random products
public class InputDevice {
    InputStream consoleInputStream;
    FileInputStream fileInputStream;
    Scanner scanner;
    InputDevice(){
        this.consoleInputStream = System.in;
        this.scanner = new Scanner(System.in);
    }

    public void setFileInputStream(String fileName) {
        try {
            this.fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
            System.out.println("Permission denied for file");
        }
        this.scanner = new Scanner(fileInputStream);
    }

    public void closeFileInputStream() {
        if (this.fileInputStream == null) return;
        try {
            this.fileInputStream.close();
            this.scanner.close();
        } catch (IOException e) {
            System.out.println("Error closing file");
            e.printStackTrace();
        }
    }

    public int nextInt(int minBound, int maxBound) { return ThreadLocalRandom.current().nextInt(minBound, maxBound); }

    public String nextLine() { return scanner.nextLine(); }

    public ArrayList<Playlist> CreatePlaylistsFromFile() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        while (scanner.hasNextLine()) {
            if (scanner.nextLine().equals("This profile has no playlists yet") || scanner.nextLine().equals("This profile has not listened to anything yet"))
                return playlists;
            if (!scanner.hasNextLine()) break;
            String playlistName = scanner.nextLine().split(" ")[1].toUpperCase();
            ArrayList<Song> songList = ReadSongsfromFile();
            playlists.add(new Playlist(playlistName, songList));
        }
        return playlists;
    }

    public Profile readProfileFromFile(){
        if(!scanner.hasNextLine()) return null;
        String ownerInfo = "";
        String[] line;
        line = new String[]{scanner.nextLine()};
        ownerInfo = line[line.length - 1];
        Profile profile = new Profile(ownerInfo);
        try{
            profile.checkAttributes();
        }
        catch(InvalidProfileAttribute e){
            switch (e.getMessage()){
                case "Invalid name" -> System.out.println("Name must be at least 3 characters long: " + profile.getProfileName());
            }
        }
        scanner.nextLine();
        //ArrayList<Playlist> playlists = CreatePlaylistsFromFile();
        //profile.setPlaylists(playlists);
        return profile;
    }

    public ArrayList<Song> ReadSongsfromFile(){
        ArrayList<Song> songList = new ArrayList<>();
        while(scanner.hasNextLine()){
            if(scanner.nextLine().equals("Library is empty")) return songList;
            if(!scanner.hasNextLine()) break;
            String songName = scanner.nextLine().split(" ")[1].toUpperCase();
            songName = songName.substring(0, songName.length() - 1);
            int length = Integer.parseInt(scanner.nextLine().split(" ")[2]);
            String artist = scanner.nextLine().split(" ")[2].toUpperCase();
            String albumName = scanner.nextLine().split(" ")[2].toUpperCase();

            songList.add(new Song(songName, length, artist, albumName));
        }
        return songList;
    }
}

