package client;

public class application {
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

            String msg;
            for (;;) {
                System.out.print(">");
                msg = conn.getInput();
                if (msg.equalsIgnoreCase("quit")) {
                  break;
                }
                System.out.print("sending '" + msg + "' to server...");
                conn.send(msg);
                System.out.println("done");
                System.out.println("received '" + conn.getResponse() + "' from server\n");
              }
        }



    }
}
