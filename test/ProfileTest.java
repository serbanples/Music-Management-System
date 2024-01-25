import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
class ProfileTest {
    @Test
    void testProfileConstructor() {
        // Arrange
        String profileName = "TestProfile";

        // Act
        Profile profile = new Profile(profileName);

        // Assert
        assertEquals(profileName, profile.getProfileName());
        assertNotNull(profile.getPlaylists());
    }

    @Test
    void testShowPlaylist() {
        // Arrange
        ArrayList<Playlist> playlists = new ArrayList<>();
        Playlist playlist1 = new Playlist("Playlist1", new ArrayList<>());
        Playlist playlist2 = new Playlist("Playlist2", new ArrayList<>());
        playlists.add(playlist1);
        playlists.add(playlist2);

        Profile profile = new Profile("TestProfile");
        profile.setPlaylists(playlists);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        profile.show_playlist();

        // Assert
        String expectedOutput = "Select the playlist you want to learn more about: \n" +
                "1. Playlist1\n" +
                "2. Playlist2\n";
        assertEquals(expectedOutput, outputStream.toString());

        // Reset System.in and System.out
        System.setIn(System.in);
        System.setOut(System.out);
    }
}