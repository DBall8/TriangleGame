package dcball;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {

    private GameManager game;

    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private Thread listener;

    public ClientSocket(GameManager game){
        this.game = game;
    }

    public void connect(String url, int port){
        try {
            socket = new Socket(url, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Client successfully connected.");

            int pnum = Integer.parseInt(in.readLine());
            System.out.println("Connected as player " + pnum);

            game.addPlayer(pnum, true);

            listen();

        } catch (IOException e) {
            System.out.println("Could not connect.");
            e.printStackTrace();
        }
    }

    public void listen(){
        listener = new Thread(){
            @Override
            public void run() {
                String msg;
                try{
                    while((msg = in.readLine()) != null){
                        System.out.println("Server: " + msg);
                        JSONObject obj = new JSONObject(msg);
                        if(obj.getString("type").equals("connection")){
                            int pnum = obj.getInt("pnum");
                            game.addPlayer(pnum, false);
                        }
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
        if(listener != null){
            listener.interrupt();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Socket closed.");
    }
}
