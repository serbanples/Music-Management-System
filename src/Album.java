import java.util.ArrayList;
import java.util.List;
public class Album extends Playlist implements Downloaded {
    String genre;
    String artist;
    boolean downloaded = false;
    public Album(String playlistName, List<Song> songList, String genre, String artist) {
        super(playlistName, songList);
        this.genre = genre;
        this.artist = artist;
    }
    @Override
    public String PlaylistLength(){
        int albumLength = 0;
        for(Song song : songList){
            albumLength += song.length;
        }
        int hour = 0;
        int min = 0;
        while(albumLength >= 60){
            min += 1;
            albumLength -= 60;
        }
        while(min >= 60){
            hour += 1;
            min -= 60;
        }
        if(hour == 0) return min + " minutes";
        else return hour + "hours and " + min + " minutes";
    }

    @Override
    public boolean isDownloaded() {
        return downloaded;
    }

    @Override
    public void Download() {
        OutputDevice od = new OutputDevice();
        if(isDownloaded())
            od.printMessageNl("This album is already downloaded");
        else {
            downloaded = true;
            od.printMessageNl("Album downloaded successfully");
        }
    }

    @Override
    public void DeleteFromDownloads() {
        OutputDevice od = new OutputDevice();
        if (isDownloaded()) {
            downloaded = false;
            od.printMessageNl("This album has been deleted successfully");
        } else {
            od.printMessageNl("This album is not downloaded");
        }
    }
    public String print_songList(){
        String printableSongList = "";
        int i = 1;
        for(Song song : songList){
            printableSongList = printableSongList + "\t" + i++ + ". " + song.title + "\t\t" + song.getLengthMinutes() + "\n";
        }
        return printableSongList;
    }

    @Override
    public String toString(){
        return PlaylistName + "\nBy " + artist + "\n\n" + print_songList() + "\n" + PlaylistLength();
    }
}
