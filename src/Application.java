import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    InputDevice id;
    OutputDevice od;
    AdminManagementSystem admin = new AdminManagementSystem();

    public Application(InputDevice id, OutputDevice od) {
        this.id = id;
        this.od = od;
    }
    public void createFile(){
        File f = new File("/Users/serbanples/Desktop/lab p3/project p3");
        int count = 0;
        File[] s = f.listFiles();
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                if (s[i].getName().endsWith(".txt")) {
                    count++;
                }
            }
        }
        count++;
        od.setFileOutputStream("profile" + count +".txt");
        id.setFileInputStream("profile" + count + ".txt");
    }

    public void recreateFile(){

        od.closeFileOutputStream();
        id.closeFileInputStream();

        File f = new File("/Users/serbanples/Desktop/lab p3/project p3");
        int count = 0;
        File[] s = f.listFiles();
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                if (s[i].getName().endsWith(".txt")) {
                    count++;
                }
            }
        }
        String fileName = "profile" + count++ + ".txt";

        try{
            Files.deleteIfExists(Path.of(fileName));
        }
        catch(NoSuchFileException e){
            e.printStackTrace();
            System.out.println("The file to delete does not exist");
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Error deleting file");
        }

        try{
            Files.createFile(Path.of(fileName));
            od.setFileOutputStream(fileName);
            id.setFileInputStream(fileName);
        }
        catch(FileAlreadyExistsException e){
            e.printStackTrace();
            System.out.println("The file to create already exists");
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating file");
        }
    }

    public void new_Profile(){
        createFile();
        od.printMessageNl("Profile name: ");
        Scanner scanner = new Scanner(System.in);
        String profileInfo1 = scanner.nextLine();
        Profile profile = new Profile(profileInfo1);
        try{
            profile.checkAttributes();
        }
        catch(InvalidProfileAttribute e){
            switch (e.getMessage()){
                case "Invalid name" -> System.out.println("Name must be at least 3 characters long: " + profile.getProfileName());
            }
        }
        od.storeToFile(profile);
        menu(profile);
    }

    public void old_Profile(){
        od.printMessageNl("Starting store from file\nProvide file name:\n");
        String fileName = id.nextLine() + ".txt";
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath) && Files.isReadable(filePath)) {
            id.setFileInputStream(fileName);
            od.setFileOutputStream(fileName);
        } else {
            od.printMessageNl("File does not exist or cannot be read. Please try again.");
            System.exit(1);
        }
        Profile profile = id.readProfileFromFile();
        menu(profile);
    }
    public void run(String[] args) {
        if(args.length == 0) choose_config();
        else if( args.length == 1){
            switch(args[0]){
                case "scratch" -> new_Profile();
                case "old" -> old_Profile();
                case "admin" -> admin_menu();
                default -> System.exit(1);
            }
        }
        else {
            od.printMessageNl("Usage: java Application <scratch|old>");
            System.exit(1);
        }
    }
    public void choose_config(){
        od.printMessageNl("Welcome to Serban's Music Library App!");
        od.printMessageNl("Press:");
        od.printMessageNl("1 - Create a new Profile");
        od.printMessageNl("2 - Load a existing Profile from memory");
        od.printMessageNl("3 - Enter the Admin menu");
        od.printMessageNl("4 - Exit the application");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                new_Profile();
                break;
            case 2:
                old_Profile();
                break;
            case 3:
                admin_menu();
                break;
            case 4:
                System.exit(1);
                break;
        }
        choose_config();
    }

    public void menu(Profile profile){
        od.printMessageNl("This is " + profile.getProfileName() +"'s profile");
        od.printMessageNl("Press:");
        od.printMessageNl("1 - Show Playlists");
        od.printMessageNl("2 - Edit Playlists");
        od.printMessageNl("3 - Check other profiles");
        od.printMessageNl("4 - Exit Application");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                od.printProfilePlaylists(profile);
                break;
            case 2:
                edit_playlist_menu(profile);
                break;
            case 3:
                old_Profile();
                break;
            case 4:
                System.exit(1);
                break;
        }
        menu(profile);
    }
    public void admin_menu() {
        od.printMessageNl("Welcome to the admin management system!");
        od.printMessageNl("Press:");
        od.printMessageNl("1 - Add a new song to the database");
        od.printMessageNl("2 - Add a new album to the database");
        od.printMessageNl("3 - Print songs in the database");
        od.printMessageNl("4 - Print albums in database");
        od.printMessageNl("5 - Exit Application");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch(option){
            case 1:
                admin.add_song_to_DB();
                break;
            case 2:
                admin.add_album_to_DB();;
                break;
            case 3:
                admin.print_songs();
                break;
            case 4:
                admin.print_albums();
                break;
            case 5:
                System.exit(1);
                break;
        }
        admin_menu();
    }
    public void edit_playlist_menu(Profile profile){
        od.printMessageNl("1 - Create a new Playlist");
        od.printMessageNl("2 - Delete a Playlist");
        od.printMessageNl("3 - Add a song to a Playlist");
        od.printMessageNl("4 - Remove a song from a Playlist");
        od.printMessageNl("5 - Exit Application");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option){
            case 1:
                profile.createPlaylist();
                recreateFile();
                od.storeToFile(profile);
                break;
            case 2:
                profile.deletePlaylist();
                recreateFile();
                od.storeToFile(profile);
                break;
            case 3:
                profile.show_playlist();
                break;
            case 4:
                System.exit(1);
                break;
        }
    }
}
