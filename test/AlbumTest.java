import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class AlbumTest {

    @Test
    void testAlbumConstructor() {
        // Arrange
        String playlistName = "TestAlbum";
        List<Song> songList = new ArrayList<>();
        String genre = "TestGenre";
        String artist = "TestArtist";

        // Act
        Album album = new Album(playlistName, songList, genre, artist);

        // Assert
        assertEquals(playlistName, album.PlaylistName);
        assertEquals(songList, album.songList);
        assertEquals(genre, album.genre);
        assertEquals(artist, album.artist);
        assertFalse(album.isDownloaded());
    }
    @Test
    void testPrintSongList() {
        // Arrange
        Song song1 = new Song("Song1", 180, "Artist1", "Album1");
        Song song2 = new Song("Song2", 200, "Artist2", "Album2");
        Playlist playlist = new Playlist("TestPlaylist", List.of(song1, song2));

        // Act
        String result = playlist.print_songList();

        // Assert
        String expectedOutput = "\t1. Song1\t\t3:00\n" +
                "\t2. Song2\t\t3:20\n";
        assertEquals(expectedOutput, result);
    }
}