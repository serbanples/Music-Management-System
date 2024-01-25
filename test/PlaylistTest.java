import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
class PlaylistTest {
    @Test
    void testPlaylistConstructor() {
        // Arrange
        String playlistName = "TestPlaylist";
        List<Song> songList = new ArrayList<>();

        // Act
        Playlist playlist = new Playlist(playlistName, songList);

        // Assert
        assertEquals(playlistName, playlist.PlaylistName);
        assertEquals(songList, playlist.songList);
        assertFalse(playlist.isDownloaded());
    }
}