package dcball;

import dcball.Server.Server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server(8080);
        server.start();

        //Application.launch(VisualsLauncher.class);
    }
}
