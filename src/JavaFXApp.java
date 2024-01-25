import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaFXApp extends Application {

    private Scene registerScene;
    private Scene loginScene;
    private Scene adminScene;
    private Scene userScene;
    private Scene addSongToDBScene;
    private Scene addAlbumToDBScene;
    private Scene printSongsFromDBScene;
    private Scene printAlbumsFromDBScene;
    private Scene createNewPlaylistScene;
    private Scene deletePlaylistScene;
    private Scene addSongToPlaylistScene;
    private Scene removeSongFromPlaylistScene;
    private Scene viewPlaylistScene;
    private Scene printPlaylist;
    private static StackPane stackPane;
    AdminManagementSystem admin = new AdminManagementSystem();

    private GridPane createRegisterGrid() {
        GridPane registerGrid = new GridPane();
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);
        registerGrid.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        registerGrid.add(nameLabel, 0, 0);
        registerGrid.add(nameField, 1, 0);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        registerGrid.add(emailLabel, 0, 1);
        registerGrid.add(emailField, 1, 1);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        registerGrid.add(passwordLabel, 0, 2);
        registerGrid.add(passwordField, 1, 2);

        ToggleGroup accountTypeGroup = new ToggleGroup();

        RadioButton userRadioButton = new RadioButton("User Account");
        userRadioButton.setToggleGroup(accountTypeGroup);
        userRadioButton.setSelected(true);

        RadioButton adminRadioButton = new RadioButton("Admin Account");
        adminRadioButton.setToggleGroup(accountTypeGroup);

        HBox accountTypeBox = new HBox(10, userRadioButton, adminRadioButton);
        registerGrid.add(accountTypeBox, 0, 3, 2, 1);

        Button registerButton = new Button("Register");
        registerGrid.add(registerButton, 1, 4);

        Label errorLabel = new Label();
        registerGrid.add(errorLabel, 0, 6, 2, 1);

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String accountType = userRadioButton.isSelected() ? "user" : "admin";

            if (!admin.isValidEmail(email)) {
                errorLabel.setText("Invalid email. Please make sure that the email contains '@' and '.com'.");
            }
            else {
                if (admin.isValidRegister(name, email, password)) {
                    admin.saveUserCredentials(name, email, password, accountType);
                    errorLabel.setText("Registration successful!");
                    clearFields(nameField, emailField, passwordField);
                } else {
                    errorLabel.setText("Account already exists.");
                }
            }
        });

        Button switchToLoginButton = new Button("Switch to Login");
        switchToLoginButton.setOnAction(e -> switchScene(loginScene));

        registerGrid.add(switchToLoginButton, 1, 5);

        return registerGrid;
    }

    private GridPane createLoginGrid() {
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(20, 20, 20, 20));

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        loginGrid.add(emailLabel, 0, 1);
        loginGrid.add(emailField, 1, 1);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        loginGrid.add(passwordLabel, 0, 2);
        loginGrid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        loginGrid.add(loginButton, 1, 3);

        Label errorLabel = new Label();
        loginGrid.add(errorLabel, 0, 4, 2, 1);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (!admin.isValidEmail(email)) {
                errorLabel.setText("Invalid email. Please make sure that the email contains '@' and '.com'.");
            }
            else {
                if (admin.isValidLogin(email, password)) {
                    if (admin.checkUserCredentials(email, password).equals("admin"))
                        switchScene(adminScene);
                    else switchScene(userScene);
                } else {
                    errorLabel.setText("Account doesnt exist.");
                }
            }
        });

        Button switchToRegisterButton = new Button("Switch to Register");
        switchToRegisterButton.setOnAction(e -> switchScene(registerScene));

        loginGrid.add(switchToRegisterButton, 1, 5);

        return loginGrid;
    }
    private VBox createAdminBox() {
        VBox adminBox = new VBox();
        adminBox.setSpacing(10);
        adminBox.setPadding(new Insets(20, 20, 20, 20));

        Label welcomeLabel = new Label("Admin Management System");
        Label empty = new Label();
        Button option1 = new Button("Add a new song to the database");
        Button option2 = new Button("Add a new album to the database");
        Button option3 = new Button("Print the songs in the database");
        Button option4 = new Button("Print the albums in the database");
        Button option5 = new Button("Exit application");

        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option1.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option2.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option3.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option4.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option5.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        adminBox.getChildren().addAll(welcomeLabel,empty,option1,option2,option3,option4,option5);

        option1.setOnAction( e -> switchScene(addSongToDBScene));
        option2.setOnAction( e -> switchScene(addAlbumToDBScene));
        option3.setOnAction( e -> switchScene(printSongsFromDBScene));
        option4.setOnAction( e -> switchScene(printAlbumsFromDBScene));
        option5.setOnAction( e -> System.exit(1));

        return adminBox;
    }
    private VBox createUserBox() {
        VBox userBox = new VBox();
        userBox.setSpacing(10);
        userBox.setPadding(new Insets(20, 20, 20, 20));

        Label welcomeLabel = new Label("Welcome, User!");
        Label empty = new Label();
        Button option1 = new Button("Create a new Playlist");
        Button option2 = new Button("Delete a Playlist");
        Button option3 = new Button("Add a song to a Playlist");
        Button option4 = new Button("Remove a song from a Playlist");
        Button option5 = new Button("View a Playlist");
        Button option6 = new Button("Exit application");

        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option1.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option2.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option3.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option4.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option5.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        option6.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        userBox.getChildren().addAll(welcomeLabel,empty,option1,option2,option3,option4,option5,option6);

        option1.setOnAction( e -> switchScene(createNewPlaylistScene));
        option2.setOnAction( e -> switchScene(deletePlaylistScene));
        option3.setOnAction( e -> switchScene(addSongToPlaylistScene));
        option4.setOnAction( e -> switchScene(removeSongFromPlaylistScene));
        option5.setOnAction( e -> switchScene(viewPlaylistScene));
        option6.setOnAction( e -> System.exit(1));

        return userBox;
    }

    private GridPane createAddSongToDB() {
        GridPane songToDBGrid = new GridPane();
        songToDBGrid.setHgap(10);
        songToDBGrid.setVgap(10);
        songToDBGrid.setPadding(new Insets(20, 20, 20, 20));

        Label songNameLabel = new Label("Song Name:");
        TextField songNameField = new TextField();
        songToDBGrid.add(songNameLabel, 0, 0);
        songToDBGrid.add(songNameField, 1, 0);

        Label songAlbumLabel = new Label("Song Album:");
        TextField songAlbumField = new TextField();
        songToDBGrid.add(songAlbumLabel, 0, 1);
        songToDBGrid.add(songAlbumField, 1, 1);

        Label songArtistLabel = new Label("Song Artist:");
        TextField songArtistField = new TextField();
        songToDBGrid.add(songArtistLabel, 0, 2);
        songToDBGrid.add(songArtistField, 1, 2);

        Label songLengthLabel = new Label("Song Length:");
        TextField songLengthField = new TextField();
        songToDBGrid.add(songLengthLabel, 0, 3);
        songToDBGrid.add(songLengthField, 1, 3);

        Button addButton = new Button("Add");
        songToDBGrid.add(addButton, 1, 4);

        Label errorLabel = new Label();
        songToDBGrid.add(errorLabel, 0, 6, 2, 1);

        addButton.setOnAction(e -> {
            String songName = songNameField.getText();
            String songArtist = songArtistField.getText();
            String songAlbum = songAlbumField.getText();
            int songLength = Integer.parseInt(songLengthField.getText());
            if(admin.isValidSong(songName,songArtist,songAlbum,songLength).equals("y")) {
                if(admin.SongAlreadyInDB(songName,songArtist,songAlbum,songLength)){
                    errorLabel.setText("Song already in the database.");
                }
                else {
                    admin.addSong(songName, songArtist, songAlbum, songLength, 1);
                    errorLabel.setText("Song added successfully!");
                }
            }
            else errorLabel.setText(admin.isValidSong(songName,songArtist,songAlbum,songLength));
        });

//        addButton.setOnAction(e -> {
//            String songName = songNameField.getText();
//            String songArtist = songArtistField.getText();
//            String songAlbum = songAlbumField.getText();
//            int songLengthText = Integer.parseInt(songLengthField.getText());
//
//            String validationError = admin.isValidSong(songName, songArtist, songAlbum, songLengthText);
//            if (!validationError.equals("y")) {
//                errorLabel.setText(validationError);
//                return;
//            }
//            Task<Void> addSongTask = new Task<Void>() {
//                @Override
//                protected Void call() throws Exception {
//                    admin.addSong(songName, songArtist, songAlbum, songLengthText, 1);
//                    return null;
//                }
//            };
//            addSongTask.setOnSucceeded(event -> {
//                errorLabel.setText("Song added successfully!");
//            });
//
//            addSongTask.setOnFailed(event -> {
//                errorLabel.setText("Error adding the song.");
//            });
//
//            Thread addSongThread = new Thread(addSongTask);
//            addSongThread.start();
//        });

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(adminScene));

        songToDBGrid.add(switchToMenu, 1, 5);

        return songToDBGrid;
    }

    private GridPane createAddAlbumToDB(){
        GridPane albumToDBGrid = new GridPane();
        albumToDBGrid.setHgap(10);
        albumToDBGrid.setVgap(10);
        albumToDBGrid.setPadding(new Insets(20, 20, 20, 20));

        Label albumNameLabel = new Label("Album Name:");
        TextField albumNameField = new TextField();
        albumToDBGrid.add(albumNameLabel, 0, 0);
        albumToDBGrid.add(albumNameField, 1, 0);

        Label albumGenreLabel = new Label("Album Genre:");
        TextField albumGenreField = new TextField();
        albumToDBGrid.add(albumGenreLabel, 0, 1);
        albumToDBGrid.add(albumGenreField, 1, 1);

        Label albumArtistLabel = new Label("Album Artist:");
        TextField albumArtistField = new TextField();
        albumToDBGrid.add(albumArtistLabel, 0, 2);
        albumToDBGrid.add(albumArtistField, 1, 2);

        Button addButton = new Button("Confirm");
        albumToDBGrid.add(addButton, 1, 3);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(adminScene));
        albumToDBGrid.add(switchToMenu, 1, 9);

        Label errorLabel = new Label();
        albumToDBGrid.add(errorLabel, 0, 8, 2, 1);

        addButton.setOnAction(e -> {
            String albumName = albumNameField.getText();
            String albumGenre = albumGenreField.getText();
            String albumArtist = albumArtistField.getText();

            Label songNameLabel = new Label("Song Name:");
            TextField songNameField = new TextField();
            albumToDBGrid.add(songNameLabel, 0, 4);
            albumToDBGrid.add(songNameField, 1, 4);

            Label songLengthLabel = new Label("Song Length:");
            TextField songLengthField = new TextField();
            albumToDBGrid.add(songLengthLabel, 0, 5);
            albumToDBGrid.add(songLengthField, 1, 5);

            Button addButton2 = new Button("Add Song");
            albumToDBGrid.add(addButton2, 1, 6);
            AtomicInteger i = new AtomicInteger(1);

            addButton2.setOnAction( ee -> {
                String songName = songNameField.getText();
                int songLength = Integer.parseInt(songLengthField.getText());

                if(admin.isValidSong(songName,albumArtist,albumName,songLength).equals("y")) {
                    if(admin.SongAlreadyInDB(songName,albumArtist,albumName,songLength)){
                        errorLabel.setText("Song already in the database.");
                    }
                    else {
                        admin.addSong(songName, albumArtist, albumName, songLength, 1);
                        errorLabel.setText("Song added successfully!");
                    }
                }
                else errorLabel.setText(admin.isValidSong(songName,albumArtist,albumName,songLength));
                songNameField.clear();
                songLengthField.clear();
            });

            Button addButton3 = new Button("Add Album");
            albumToDBGrid.add(addButton3,1,7);
            addButton3.setOnAction( eee -> {
                if(admin.isValidAlbum(albumName,albumArtist,albumGenre).equals("y")) {
                    if(!admin.AlbumAlreadyInDB(albumName,albumArtist,albumGenre)) {
                        admin.addAlbum(albumName, albumArtist, albumGenre);
                        errorLabel.setText("Album added succcessfully");
                    }
                    else errorLabel.setText("Album already in the database");
                }
                else errorLabel.setText(admin.isValidAlbum(albumName,albumArtist,albumGenre));
            });
        });

        return albumToDBGrid;
    }

    private ScrollPane createPrintSongsFromDB(){
        VBox songBox = new VBox();
        songBox.setSpacing(10);
        songBox.setPadding(new Insets(20, 20, 20, 20));

        ArrayList<Song> songsFromDB = admin.printSong();
        int i = 1;
        for(Song s : songsFromDB){
            Label songData = new Label(i++ + ". " + s.title + "\t" + s.artist + "\t" + s.albumName + "\t" + s.getLengthMinutes());
            songBox.getChildren().add(songData);
        }

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(adminScene));
        songBox.getChildren().add(switchToMenu);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(songBox);
        return scrollPane;
    }

    private ScrollPane createPrintAlbumsFromDB(){
        VBox albumBox = new VBox();
        albumBox.setSpacing(10);
        albumBox.setPadding(new Insets(20, 20, 20, 20));

        ArrayList<Album> albumsFromDB = admin.printAlbum();
        int i = 1;
        for(Album album : albumsFromDB){
            String albumData = new String(i++ + ". " + album.PlaylistName + "\nby " + album.artist + "\t\t" + album.genre + "\n");
            int j = 1;
            for(Song song : album.songList){
                albumData = albumData + "\t" + j++ + ". " + song.title + "\t" + song.getLengthMinutes() + "\n";
            }
            albumData = albumData + "\n" + album.PlaylistLength() + "\n";
            Label albumDataLabel = new Label(albumData);
            albumBox.getChildren().add(albumDataLabel);
        }

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(adminScene));
        albumBox.getChildren().add(switchToMenu);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(albumBox);
        return scrollPane;
    }
    private GridPane createNewPlaylist(){
        GridPane newPlaylistGrid = new GridPane();
        newPlaylistGrid.setHgap(10);
        newPlaylistGrid.setVgap(10);
        newPlaylistGrid.setPadding(new Insets(20, 20, 20, 20));

        Label albumNameLabel = new Label("Playlist Name:");
        TextField albumNameField = new TextField();
        newPlaylistGrid.add(albumNameLabel, 0, 0);
        newPlaylistGrid.add(albumNameField, 1, 0);

        Button createButton = new Button("Confirm");
        newPlaylistGrid.add(createButton,1,2);

        Label errorLabel = new Label();
        newPlaylistGrid.add(errorLabel, 0, 1, 2, 1);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        newPlaylistGrid.add(switchToMenu, 1, 3);

        createButton.setOnAction( e -> {
            String albumName = albumNameField.getText();
            admin.createPlaylist(albumName);
            errorLabel.setText("Playlist created successfully!");
        });

        return newPlaylistGrid;
    }

    private GridPane deletePlaylist(){
        GridPane deletePlaylistGrid = new GridPane();
        deletePlaylistGrid.setHgap(10);
        deletePlaylistGrid.setVgap(10);
        deletePlaylistGrid.setPadding(new Insets(20, 20, 20, 20));

        Label albumNameLabel = new Label("Playlist Name:");
        TextField albumNameField = new TextField();
        deletePlaylistGrid.add(albumNameLabel, 0, 0);
        deletePlaylistGrid.add(albumNameField, 1, 0);

        Button createButton = new Button("Confirm");
        deletePlaylistGrid.add(createButton,1,2);

        Label errorLabel = new Label();
        deletePlaylistGrid.add(errorLabel, 0, 1, 2, 1);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        deletePlaylistGrid.add(switchToMenu, 1, 3);

        createButton.setOnAction( e -> {
            String albumName = albumNameField.getText();
            admin.deletePlaylist(albumName);
            errorLabel.setText("Playlist deleted successfully!");
        });

        return deletePlaylistGrid;
    }

    private GridPane viewPlaylist(){
        GridPane viewPlaylistGrid = new GridPane();
        viewPlaylistGrid.setHgap(10);
        viewPlaylistGrid.setVgap(10);
        viewPlaylistGrid.setPadding(new Insets(20, 20, 20, 20));

        Label playlistNameLabel = new Label("Playlist Name:");
        TextField playlistNameField = new TextField();
        viewPlaylistGrid.add(playlistNameLabel, 0, 0);
        viewPlaylistGrid.add(playlistNameField, 1, 0);

        Button createButton = new Button("Confirm");
        viewPlaylistGrid.add(createButton,1,2);

        Label errorLabel = new Label();
        viewPlaylistGrid.add(errorLabel, 0, 1, 2, 1);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        viewPlaylistGrid.add(switchToMenu, 1, 3);

        createButton.setOnAction( e -> {
            String albumName = playlistNameField.getText();
            printPlaylist = new Scene(printPlaylist(albumName),800,400);
            switchScene(printPlaylist);
            errorLabel.setText("Playlist deleted successfully!");
        });

        return viewPlaylistGrid;
    }

    private ScrollPane printPlaylist(String albumName){
        VBox playlistBox = new VBox();
        playlistBox.setSpacing(10);
        playlistBox.setPadding(new Insets(20, 20, 20, 20));

        ArrayList<Song> songsFromDB = admin.viewPlaylist(albumName);
        int i = 1;
        for(Song s : songsFromDB){
            Label songData = new Label(i++ + ". " + s.title + "\t" + s.artist + "\t" + s.albumName + "\t" + s.getLengthMinutes());
            playlistBox.getChildren().add(songData);
        }

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        playlistBox.getChildren().add(switchToMenu);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(playlistBox);
        return scrollPane;
    }

    private GridPane addSongToPlaylist(){
        GridPane addSongGrid = new GridPane();
        addSongGrid.setHgap(10);
        addSongGrid.setVgap(10);
        addSongGrid.setPadding(new Insets(20, 20, 20, 20));

        Label playlistNameLabel = new Label("Playlist Name:");
        TextField playlistNameField = new TextField();
        addSongGrid.add(playlistNameLabel, 0, 0);
        addSongGrid.add(playlistNameField, 1, 0);

        Button createButton = new Button("Confirm");
        addSongGrid.add(createButton,1,1);

        Label errorLabel = new Label();
        addSongGrid.add(errorLabel, 0, 4, 2, 1);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        addSongGrid.add(switchToMenu, 1, 5);

        createButton.setOnAction( e -> {
            String albumName = playlistNameField.getText();
            Label songNameLabel = new Label("Song Name:");
            TextField songNameField = new TextField();
            addSongGrid.add(songNameLabel, 0, 2);
            addSongGrid.add(songNameField, 1, 2);

            Button addButton = new Button("Add");
            addSongGrid.add(addButton,1,3);

            addButton.setOnAction( ee -> {
                String songName = songNameField.getText();
                admin.addSongToPlaylist(songName, albumName);
                errorLabel.setText("Song added successfully!");
            });
        });

        return addSongGrid;
    }

    private GridPane removeSongFromPlaylist(){
        GridPane removeSongGrid = new GridPane();
        removeSongGrid.setHgap(10);
        removeSongGrid.setVgap(10);
        removeSongGrid.setPadding(new Insets(20, 20, 20, 20));

        Label playlistNameLabel = new Label("Playlist Name:");
        TextField playlistNameField = new TextField();
        removeSongGrid.add(playlistNameLabel, 0, 0);
        removeSongGrid.add(playlistNameField, 1, 0);

        Button createButton = new Button("Confirm");
        removeSongGrid.add(createButton,1,1);

        Label errorLabel = new Label();
        removeSongGrid.add(errorLabel, 0, 4, 2, 1);

        Button switchToMenu = new Button("Back to menu");
        switchToMenu.setOnAction(e -> switchScene(userScene));
        removeSongGrid.add(switchToMenu, 1, 5);

        createButton.setOnAction( e -> {
            String albumName = playlistNameField.getText();
            Label songNameLabel = new Label("Song Name:");
            TextField songNameField = new TextField();
            removeSongGrid.add(songNameLabel, 0, 2);
            removeSongGrid.add(songNameField, 1, 2);

            Button addButton = new Button("Remove");
            removeSongGrid.add(addButton,1,3);

            addButton.setOnAction( ee -> {
                String songName = songNameField.getText();
                admin.removeSongFromPlaylist(songName, albumName);
                errorLabel.setText("Song added successfully!");
            });
        });

        return removeSongGrid;
    }


    private void switchScene(Scene switchToScene) {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(switchToScene.getRoot());
    }
    private void clearFields(TextField nameField, TextField emailField, PasswordField passwordField) {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Serban's music app");

        stackPane = new StackPane();
        registerScene = new Scene(createRegisterGrid(), 800, 400);
        loginScene = new Scene(createLoginGrid(), 800, 400);
        adminScene = new Scene(createAdminBox(),800,400);
        userScene = new Scene(createUserBox(),800,400);
        addSongToDBScene = new Scene(createAddSongToDB(),800,400);
        addAlbumToDBScene = new Scene(createAddAlbumToDB(),800,400);
        printSongsFromDBScene = new Scene(createPrintSongsFromDB(),800,400);
        printAlbumsFromDBScene = new Scene(createPrintAlbumsFromDB(),800,400);
        createNewPlaylistScene = new Scene(createNewPlaylist(),800,400);
        deletePlaylistScene = new Scene(deletePlaylist(),800,400);
        addSongToPlaylistScene = new Scene(addSongToPlaylist(),800,400);
        removeSongFromPlaylistScene = new Scene(removeSongFromPlaylist(),800,400);
        viewPlaylistScene = new Scene(viewPlaylist(),800,400);

        stackPane.getChildren().add(loginScene.getRoot());

        primaryStage.setScene(new Scene(stackPane, 800, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
