import java.io.*;
public class Main {
    public static void main(String[] args) {
        OutputDevice outputDevice = new OutputDevice();
        InputDevice inputDevice = new InputDevice();
        Application app = new Application(inputDevice, outputDevice);

        if(args.length == 1 && args[0].equals("scratch")){
            outputDevice.printMessageNl("Creating profile from scratch");
            args = new String[]{"scratch"};
        }
        else if(args.length == 1 && args[0].equals("old") ){
            args = new String[]{"old"};
        }
        else if(args.length == 1 && args[0].equals("admin")){
            args = new String[]{"admin"};
        }
        else if(args.length == 0){

        }
        else{
            outputDevice.printMessageNl("Usage: java Main <scratch>");
            System.exit(1);
        }
        for(String arg:args) outputDevice.printMessage(arg + " ");
        outputDevice.printMessageNl("\n");
        app.run(args);
    }
}