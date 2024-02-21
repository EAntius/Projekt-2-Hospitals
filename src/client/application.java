package client;

import java.io.IOException;

public class application {
    public static swingTextEditor editor = new swingTextEditor();
    public static void main(String[] args) throws Exception {
        while (true) {
            String[] loginCredentials = login.getUserCredentials();
            String keystore = loginCredentials[0];
            String password = loginCredentials[1];

            connection conn = connectionStarter.startConnection(password.toCharArray(), keystore);
            if (!conn.connectedSuccessfully()) {
                System.out.println("Wrong username or password");
                continue;
            }
            
            System.out.println("You can now communicate with server:");
            String msg;
            while (true) {
                System.out.print("\n\n>");
                msg = conn.getInput();
                if (msg.equalsIgnoreCase("quit")) {
                  break;
                }
                conn.send(msg);

                switch (msg.split(" ")[0]) {
                    case "write":
                        write(conn, msg);
                        break;
                    case "create":
                        create(conn, msg);
                        break;
                    default:
                        System.out.println(conn.getResponse());
                        break;
                }
            }
            break;
        }
    }

    public static void write(connection conn, String msg) throws IOException, InterruptedException {
        String text = conn.getResponse();
        if (text == "You don't have privileges for this action") {
            System.out.println(text);
            return;
        }
        String editedText = editor.openTextEditor(text);
        conn.send(editedText);
    }

    public static void create(connection conn, String msg) throws IOException, InterruptedException {
        String text = conn.getResponse();
        if (text == "You don't have privileges for this action") {
            System.out.println(text);
            return;
        }

        //TODO - somehow parse response and create file
        String editedText = editor.openTextEditor(text);
        
        conn.send(editedText);
    }
}
