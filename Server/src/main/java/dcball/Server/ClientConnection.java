package dcball.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

    private Server server;

    private int pnum = -1;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Thread listener;

    public ClientConnection(Server server, Socket socket, int pnum){
        this.server = server;
        this.socket = socket;
        this.pnum = pnum;

        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e){
            System.out.println("Could not create streams.");
            e.printStackTrace();
        }

        out.println(pnum);
        listen();

    }

    public void send(String msg){
        out.println(msg);
    }

    public void listen(){
        listener = new Thread(){
            @Override
            public void run() {
                String msg;
                try{
                    while((msg = in.readLine()) != null){
                        System.out.println("Server: " + msg);
                    }
                }catch(IOException e){
                }

                close();

            }
        };
        listener.setDaemon(true);
        listener.start();

    }

    public void close(){
        if(listener != null) {
            listener.interrupt();
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            e.printStackTrace();
        }

        System.out.println("Client connection closed.");
        server.remove(this);
    }

    public int getPnum() {
        return pnum;
    }
}
