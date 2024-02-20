package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.Console;
import java.util.Arrays;


public class login {
    public static void main(String args[]) throws Exception {
    Scanner keystoreScanner = new Scanner(new File("./user.txt"));
        Map<String,String> keystores = new HashMap<>();
        while (keystoreScanner.hasNextLine()) {
            String[] line = keystoreScanner.nextLine().split(" ");
            keystores.put(line[0], line[1]);
        }
        keystoreScanner.close();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            String keystore = keystores.get(username);

            Console console = System.console();
            char[] passwordArray = console.readPassword("Enter Password: ");
            if (!keystores.containsKey(username)) {
                Thread.sleep(5); //prevent brute-force attacks
                System.out.println("Wrong password or username");
            } 

            client.startConnection(passwordArray, keystore);
        }
    }
}
