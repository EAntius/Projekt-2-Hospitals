package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import javax.net.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Scanner;

public class server implements Runnable {
  private ServerSocket serverSocket = null;
  private static int numConnectedClients = 0; 
  
  public server(ServerSocket ss) throws IOException {
    serverSocket = ss;
    newListener();
  }

  public void run() {
    try {
      SSLSocket socket=(SSLSocket)serverSocket.accept();
      newListener();
      SSLSession session = socket.getSession();
      Certificate[] cert = session.getPeerCertificates();
      String subject = ((X509Certificate) cert[0]).getSubjectX500Principal().getName().substring(3);

      numConnectedClients++;
      System.out.println("client connected");
      System.out.println("client name (cert subject DN field): " + subject);

      System.out.println(numConnectedClients + " concurrent connection(s)\n");
      
      PrintWriter out = null;
      BufferedReader in = null;
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Commands commander = new Commands();

      String clientMsg = null;

      String[] userdata = findUser(subject);
      System.out.println(userdata);
      if (userdata == null) {
        out.write("No user found");
        out.flush();
        socket.close();
        return;
      } else {
        System.out.println("askdj");
        out.write("User authenticated");
        out.flush();
      }

      String subjectRole = userdata[1];
      String subjectAttribute = userdata[2]; /*This data should be sent to the reference monitor */

      while ((clientMsg = in.readLine()) != null) {
        String[] recieved = clientMsg.split(" "); /*recieved now holds (a command and text file) */
        if(accessControl(recieved, subjectRole, subjectAttribute)) {
          out.write(commander.execute(recieved, userdata, 0));
          out.flush();
          if (recieved[0] == "write") {
            String editedText = in.readLine();
            commander.writeToFile(editedText, recieved[1]);
          }

        }
        
        System.out.println("done\n");
      }
      in.close();
      out.close();
      socket.close();
      numConnectedClients--;
      System.out.println("client disconnected");
      System.out.println(numConnectedClients + " concurrent connection(s)\n");
    } catch (IOException e) {
      System.out.println("Client died: " + e.getMessage());
      e.printStackTrace();
      return;
    }
  }
  
  private void newListener() { (new Thread(this)).start(); } // calls run()
  public static void main(String args[]) {
    System.out.println("\nServer Started\n");
    int port = 9876;

    String type = "TLSv1.2";
    try {
      ServerSocketFactory ssf = getServerSocketFactory(type);
      ServerSocket ss = ssf.createServerSocket(port);
      ((SSLServerSocket)ss).setNeedClientAuth(true); // enables client authentication
      new server(ss);
    } catch (IOException e) {
      System.out.println("Unable to start Server: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static ServerSocketFactory getServerSocketFactory(String type) {
    if (type.equals("TLSv1.2")) {
      SSLServerSocketFactory ssf = null;
      try { // set up key manager to perform server authentication
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        char[] password = "password".toCharArray();
        // keystore password (storepass)
        ks.load(new FileInputStream("serverkeystore"), password);  
        // truststore password (storepass)
        ts.load(new FileInputStream("servertruststore"), password); 
        kmf.init(ks, password); // certificate password (keypass)
        tmf.init(ts);  // possible to use keystore as truststore here
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ssf = ctx.getServerSocketFactory();
        return ssf;
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      return ServerSocketFactory.getDefault();
    }
    return null;
  }

  /*Finds user in database and returns the data for that user (user, role, attribute) */
  private String[] findUser(String subject) throws FileNotFoundException {
    Scanner scan = new Scanner(new File("./hospitaldatabase/userdatabase/users.txt"));
    while(scan.hasNextLine()) {
        String line = scan.nextLine().toString();
        String[] data = line.split(" ");
        if (data[0].equals(subject)) {
            scan.close();
            return data;
        }
    }
    scan.close();
    return null;
  }
  private boolean accessControl(String[] command, String role, String attribute) {
    switch(command[0]){
            case "read":

            case "write":
            if (role.compareTo("Doctor") == 1 || role.compareTo("Nurse") == 1) {
              if (attribute.compareTo(command[1]) == 1) {
                return true;
              }
            }
            return false;
            case "delete":
            if (role.compareTo("Goverment Body") == 1) {
              return true;
            }
            return false;
            case "create":
            if (role.compareTo("Doctor") == 1) {
              if (attribute.compareTo(command[1]) == 1) {
                return true;
              } 
            }
            return false;
            case "ls":
            switch(role) {
              case "Patient":
              return true;
              case "Nurse":
              if (attribute.compareTo(command[1]) == 1){
                return true;
              }
              return false;

              case "Doctor":
              if (attribute.compareTo(command[1]) == 1){
                return true;
              }
              return false;

              default:
              return false;
            }
            
            default:
            return false;
        }
  }
}
