import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Profile {
    private String name;
    private ArrayList<Playlist> playlists;
    public Profile(String name){
        this.name = name;
    }
    public String getProfileName(){
        return this.name;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }
    public ArrayList<Playlist> getPlaylists() {
        if(playlists == null) playlists = new ArrayList<>();
        return playlists;
    }

    private void checkName() throws InvalidProfileName{
        if(this.name.length() < 3) throw new InvalidProfileName("Invalid Name");
    }
    public void checkAttributes() throws InvalidProfileAttribute{
        checkName();
    }
    public void createPlaylist(){
        OutputDevice od = new OutputDevice();
        ArrayList<Song> songList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        od.printMessageNl("Enter the Playlist's name:");
        String playlistName = scanner.nextLine();
        Playlist newPlaylist = new Playlist(playlistName,songList);
        playlists.add(newPlaylist);
        od.printMessageNl("Playlist created successfully!");
    }

    public void deletePlaylist(){
        OutputDevice od = new OutputDevice();
        Scanner scanner = new Scanner(System.in);
        od.printMessageNl("Choose a playlist to delete:");
        int count = 1;
        for(Playlist i : playlists){
            od.printMessageNl(count + ". " + i.PlaylistName);
        }
        int option = scanner.nextInt();
        playlists.remove(option-1);
    }

    public void addSongToPlaylist(Playlist playlist){
        OutputDevice od = new OutputDevice();
        Scanner scanner = new Scanner(System.in);
        AdminManagementSystem admin = new AdminManagementSystem();
        admin.load();
        od.printMessageNl("Song name: ");
        String songName = scanner.nextLine();
        for(Song song : admin.getSongsInCSV()){
            if (song.title.equals(songName))
                playlist.songList.add(song);
        }
    }
    public void show_playlist(){
        OutputDevice od = new OutputDevice();
        od.printMessageNl("Select the playlist you want to learn more about: ");
        int count = 1;
        for(Playlist i : playlists){
            od.printMessageNl(count + ". " + i.PlaylistName);
        }
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        count = 1;
        for(Playlist i : playlists) {
            if (count == option) {
                playlistOperations(i);
                break;
            }
            count++;
        }
    }
    public void removeSongFromPlaylist(Playlist playlist){
        OutputDevice od = new OutputDevice();
        Scanner scanner = new Scanner(System.in);
        playlist.printSongs();
        int option = scanner.nextInt();
        playlist.songList.remove(option -1);
    }
    public void playlistOperations(Playlist playlist){
        OutputDevice od = new OutputDevice();
        Scanner scanner = new Scanner(System.in);
        od.printMessageNl("1 - Add a song");
        od.printMessageNl("2 - Delete a song");
        od.printMessageNl("3 - Exit application");
        int option = scanner.nextInt();
        switch (option){
            case 1:
                addSongToPlaylist(playlist);
                break;
            case 2:
                removeSongFromPlaylist(playlist);
                break;
            case 3:
                System.exit(1);
                break;
        }
    }
}
