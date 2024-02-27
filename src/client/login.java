package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.Console;

public class login {
    private static Scanner scanner = new Scanner(System.in);

    public static String[] getUserCredentials() throws Exception {
        Console console = System.console();
        Map<String,String> keystores = getKeystores();

        while (true) {
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine();

            String keystore = (keystores.containsKey(username)) ? keystores.get(username) : "";

            char[] passwordArray = console.readPassword("Enter Password: ");
            String password = new String(passwordArray);

            if (!keystores.containsKey(username)) {
                Thread.sleep(10); //prevent brute-force attacks
                System.out.println("Wrong password or username");
                continue;
            } 
            
            String[] returnValue = {keystore, password};
            return returnValue;
        }
    }

    private static Map<String,String> getKeystores() throws FileNotFoundException{
        Scanner keystoreScanner = new Scanner(new File("./user.txt"));
        Map<String,String> keystores = new HashMap<>();

        while (keystoreScanner.hasNextLine()) {
            String[] line = keystoreScanner.nextLine().split(" ");
            keystores.put(line[0], line[1]);
        }
        keystoreScanner.close();

        return keystores;
    }
}
