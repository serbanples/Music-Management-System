public class Song implements Favourites, Downloaded, Comparable<Song>{
    String title;
    int length;
    String artist;
    String albumName;
    boolean favourite = false;
    boolean downloaded = false;

    public Song(String title, int length, String artist, String albumName) {
        this.title = title;
        this.length = length;
        this.artist = artist;
        this.albumName = albumName;
    }

    public String getLengthMinutes() {
        int songLength = this.length;
        int min = 0;
        while(songLength >= 60){
            min += 1;
            songLength -= 60;
        }
        int sec = songLength;
        if(sec < 10 && sec >= 0) return min + ":" + "0" + sec;
        else return min + ":" + sec;
    }

    @Override
    public boolean isFavourite() {
        return favourite;
    }

    @Override
    public void addToFavourite() {
        OutputDevice od = new OutputDevice();
        if(isFavourite())
            od.printMessageNl("Tis song is already added to Favourites");
        else {
            favourite = true;
            od.printMessageNl("Song added to Favourites successfully");
        }
    }

    @Override
    public boolean isDownloaded() {
        return downloaded;
    }

    @Override
    public void Download() {
        OutputDevice od = new OutputDevice();
        if(isDownloaded())
            od.printMessageNl("This song is already downloaded");
        else {
            downloaded = true;
            od.printMessageNl("Song downloaded successfully");
        }
    }

    @Override
    public void DeleteFromDownloads() {
        OutputDevice od = new OutputDevice();
        if(isDownloaded()) {
            downloaded = false;
            od.printMessageNl("This song has been deleted successfully");
        }
        else {
            od.printMessageNl("This song is not downloaded");
        }
    }

    @Override
    public String toString(){
        return title + "\t" + getLengthMinutes() + "\nArtist: " + artist + "\nAlbum: " + albumName;
    }

    @Override
    public int compareTo(Song o) {
        if(this.title.equals(o.title))
            if(this.length == o.length)
                if(this.albumName.equals(o.albumName))
                    if(this.artist.equals(o.artist))
                        return 1;
        return 0;
    }
}
