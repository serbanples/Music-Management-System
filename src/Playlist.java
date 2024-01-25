import java.util.List;

public class Playlist implements Downloaded {
    String PlaylistName;
    List<Song> songList;
    boolean downloaded = false;

    public Playlist(String playlistName, List<Song> songList) {
        PlaylistName = playlistName;
        this.songList = songList;
    }
    public String PlaylistLength(){
        int playlistLength = 0;
        for(Song song : songList){
            playlistLength += song.length;
        }
        int hour = 0;
        int min = 0;
        while(playlistLength >= 60){
            min += 1;
            playlistLength -= 60;
        }
        while(min >= 60){
            hour += 1;
            min -= 60;
        }
        if(hour > 0) return hour + ":" + min;
        return String.valueOf(min);
    }

    @Override
    public boolean isDownloaded() {
        return downloaded;
    }

    @Override
    public void Download() {
        OutputDevice od = new OutputDevice();
        if(isDownloaded())
            od.printMessageNl("This playlist is already downloaded");
        else {
            downloaded = true;
            od.printMessageNl("Playlist downloaded successfully");
        }
    }

    @Override
    public void DeleteFromDownloads() {
        OutputDevice od = new OutputDevice();
        if (isDownloaded()) {
            downloaded = false;
            od.printMessageNl("This playlist has been deleted successfully");
        } else {
            od.printMessageNl("This playlist is not downloaded");
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
        return PlaylistName + "\n\n" + print_songList() + "\n" + PlaylistLength();
    }
    public String printSongs(){
        int count = 1;
        String printedSongs = "";
        for(Song s : songList){
            printedSongs = printedSongs + count + ". " + s.title + "\n";
        }
        return printedSongs;
    }
}
