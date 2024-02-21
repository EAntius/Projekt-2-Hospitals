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
            
            System.out.println("You can now communicate with server:");
            String msg;
            while (true) {
                System.out.print("\n\n>");
                msg = conn.getInput();
                if (msg.equalsIgnoreCase("quit")) {
                  break;
                }
                conn.send(msg);
                System.out.println(conn.getResponse());
            }
            break;
        }
        
    }
}
