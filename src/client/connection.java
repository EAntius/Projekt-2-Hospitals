package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;

public class connection {
    private BufferedReader read;
    private PrintWriter out;
    private BufferedReader in;
    private SSLSocket socket;
    private boolean successfulConnection;


    public connection(BufferedReader read, PrintWriter out, BufferedReader in, SSLSocket socket) {
        this.read = read;
        this.out = out;
        this.in = in;
        this.socket = socket;
        this.successfulConnection = true;
    }

    public connection(){
        this.successfulConnection = false;
    }

    public boolean connectedSuccessfully() {
        return successfulConnection;
    }

    public void send(String msg) {
        for (String line :msg.split("\n")) {
            out.println(line);
            out.flush();
          }
        out.println("END_OF_FILE");
        out.flush();
    }

    public String getResponse() throws IOException{
        StringBuilder responseBuilder = new StringBuilder();
        String line;

        while (!(line = in.readLine()).equals("END_OF_FILE")) {
            responseBuilder.append(line).append("\n");
        }

        return responseBuilder.toString().trim();
    }

    public String getInput() throws IOException{
        String msg = read.readLine();
        return msg;
    }

    public void endConnection() throws IOException {
        in.close();
        out.close();
        read.close();
        socket.close();
    }
}
