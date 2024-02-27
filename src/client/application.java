package client;

import java.io.IOException;

public class application {
    public static swingTextEditor editor = new swingTextEditor();
    public static void main(String[] args) throws Exception {
        while (true) {
            String[] loginCredentials = login.getUserCredentials();
            String keystore = loginCredentials[0];
            String password = loginCredentials[1];

            System.out.println(password);
            connection conn = connectionStarter.startConnection(password.toCharArray(), keystore);
            if (!conn.connectedSuccessfully()) {
                System.out.println("Wrong username or password");
                Thread.sleep(10);
                continue;
            }

            String handshake_response = conn.getResponse();
            System.out.println(handshake_response);
            if (!handshake_response.equals("User authenticated")) {
                System.out.println("Wrong username or password");
                Thread.sleep(10);
                continue;
            }

            System.out.println("You can now communicate with server:");
            serverLoop(conn);
            conn.endConnection();
        }
    }

    private static void serverLoop(connection connection) throws IOException, InterruptedException {
        String msg;
        while (true) {
            System.out.print("\n\n>");
            msg = connection.getInput();
            if (msg.equalsIgnoreCase("quit")) {
              break;
            }
            connection.send(msg);
            String response = connection.getResponse();
            if (response == null) {
                continue;
            } if (response.equals("Command not found")) {
                System.out.println(response);
                continue;
            }

            switch (msg.split(" ")[0]) {
                case "quit":
                    System.out.println("\n\n...Exiting medical records...\n\n");
                    return;
                case "write":
                    connection.send(editor.openTextEditor(response));
                    System.out.println(connection.getResponse());
                    break;
                case "create":
                    connection.send(editor.openTextEditor(""));
                    System.out.println(connection.getResponse());
                    break;
                default:
                    System.out.println(response);
                    break;
            }
        }
        return;
    }
}
