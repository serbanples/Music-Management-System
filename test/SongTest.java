import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SongTest {
    @Test
    void testSongConstructor() {
        // Arrange
        String title = "TestSong";
        int length = 180;  // 3 minutes
        String artist = "TestArtist";
        String albumName = "TestAlbum";

        // Act
        Song song = new Song(title, length, artist, albumName);

        // Assert
        assertEquals(title, song.title);
        assertEquals(length, song.length);
        assertEquals(artist, song.artist);
        assertEquals(albumName, song.albumName);
        assertFalse(song.isFavourite());
        assertFalse(song.isDownloaded());
    }
    @Test
    void testGetLengthMinutes() {
        // Arrange
        Song song = new Song("TestSong", 185, "TestArtist", "TestAlbum");

        // Act
        String result = song.getLengthMinutes();

        // Assert
        assertEquals("3:05", result);
    }
}