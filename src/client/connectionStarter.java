package client;

import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.security.KeyStore;
import java.security.cert.*;

/*
 * This example shows how to set up a key manager to perform client
 * authentication.
 *
 * This program assumes that the client is not inside a firewall.
 * The application can be modified to connect to a server outside
 * the firewall by following SSLSocketClientWithTunneling.java.
 */

public class connectionStarter {
  public static connection startConnection(char[] password, String keystoreName) throws Exception {
    String host = "localhost";
    int port = 9876;

    try {
      SSLSocketFactory factory = null;
      try {
        
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        // keystore password (storepass)
        ks.load(new FileInputStream(keystoreName), password);  
        // truststore password (storepass);
        ts.load(new FileInputStream("./openSSL/clienttruststore"), password); 
        kmf.init(ks, password); // user password (keypass)
        tmf.init(ts); // keystore can be used as truststore here
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        factory = ctx.getSocketFactory();
      } catch (Exception e) {
        throw e;
      }
      SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
      System.out.println("\nsocket before handshake:\n" + socket + "\n");

      /*
       * send http request
       *
       * See SSLSocketClient.java for more information about why
       * there is a forced handshake here when using PrintWriters.
       */

      socket.startHandshake();
      SSLSession session = socket.getSession();
      Certificate[] cert = session.getPeerCertificates();
      String subject = ((X509Certificate) cert[0]).getSubjectX500Principal().getName();
      String issuer = ((X509Certificate) cert[0]).getIssuerDN().getName();
      String serial = ((X509Certificate) cert[0]).getSerialNumber().toString();
      System.out.println("certificate name (subject DN field) on certificate received from server:\n" + subject + "\n");
      System.out.println("client issuer: " + issuer);
      System.out.println("client serialnr: " + serial);
      System.out.println("socket after handshake:\n" + socket + "\n");
      System.out.println("secure connection established\n\n");

      BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      return new connection(read, out, in, socket);
    } catch (Exception e) {
      e.printStackTrace();
      return new connection();
    }
  }
}
