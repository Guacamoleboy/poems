package app;

import app.servers.Server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server();
        server.start(7070);

    }

}