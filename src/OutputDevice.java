import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/// OutputDevice class is used to print messages to console and to file (if file is set)
public class OutputDevice {

    OutputStream consoleOutputStream;
    FileOutputStream fileOutputStream;

    OutputDevice() {
        this.consoleOutputStream = System.out;
    }

    public void setFileOutputStream(String fileName) {
        try {
            this.fileOutputStream = new FileOutputStream(fileName, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        } catch (SecurityException e) {
            e.printStackTrace();
            System.out.println("Permission denied for file");
        }
    }

    public void closeFileOutputStream() {
        if (this.fileOutputStream == null) return;
        try {
            this.fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error closing file");
        }
    }

    public void writeMessageNl(Object message) {
        try
        {
            fileOutputStream.write(message.toString().getBytes());
            fileOutputStream.write("\n".getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Error writing to file");
        }
    }
    public void writeMessage(Object message) {
        try
        {
            fileOutputStream.write(message.toString().getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Error writing to file");
        }
    }

    public void printMessage(Object message) { System.out.print(message); }
    public void printMessageNl(Object message) { System.out.println(message); }

    public void printProfile(Profile profile){
        printMessageNl(profile.getProfileName());
        if(profile.getPlaylists().isEmpty()) printMessageNl("This profile has no playlists");
        int i = 1;
        for(Playlist playlist : profile.getPlaylists()){
            printMessageNl(i + ". " + playlist.toString());
            i++;
        }
    }

    public void printProfilePlaylists(Profile profile){
        if(profile.getPlaylists().isEmpty()) printMessageNl("This profile has no playlists");
        int i = 1;
        for(Playlist playlist : profile.getPlaylists()){
            printMessageNl(i + ". " + playlist.toString());
            i++;
        }
    }

    public void storeToFile(Profile profile){
        writeMessageNl(profile.getProfileName());
        if(profile.getPlaylists().isEmpty()) writeMessageNl("This profile has no playlists");
        else writeMessage("Playlists:\n");
        int i = 1;
        for(Playlist playlist : profile.getPlaylists()){
            writeMessageNl("\t" + i + ". " + playlist.toString());
            i++;
        }
    }

}