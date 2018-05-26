package dcball.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

public class Server {

    private int port;
    private int nextPlayer = 0;

    private List<ClientConnection> connections = new ArrayList<>();

    public Server(int port){
        this.port = port;
    }

    public void start(){

        ServerSocket server;
        try {
            server = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("Could not initiate server socket.");
            e.printStackTrace();
            return;
        }

        System.out.println("Server listening on port " + port);

        while(true){
            try {
                Socket client = server.accept();
                setupClient(client);

            } catch (IOException e) {
                System.out.println("Accepting a client threw an error.");
                e.printStackTrace();
                break;
            }
        }

        try {
            server.close();
            System.out.println("Server closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupClient(Socket client){
        int pnum = nextPlayer;
        nextPlayer++;

        ClientConnection cc = new ClientConnection(this, client, pnum);

        JSONObject obj = new JSONObject();
        obj.put("type", "connection");

        for(ClientConnection c: connections){
            obj.put("pnum", c.getPnum());
            cc.send(obj.toString());
        }

        obj.put("pnum", pnum);
        broadcast(obj.toString());

        connections.add(cc);

        System.out.println("Client accepted!");
    }


    protected void remove(ClientConnection cc){
        connections.remove(cc);
    }


    public void broadcast(String msg){
        for(ClientConnection c: connections){
            c.send(msg);
        }
    }

}
