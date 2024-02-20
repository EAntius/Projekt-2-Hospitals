package java.clientFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class login {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner keystoreScanner = new Scanner(new File("./user.txt"));
        Map<String,String> keystores = new HashMap<>();
        while (keystoreScanner.hasNextLine()) {
            String[] line = keystoreScanner.nextLine().split(" ");
            keystores.put(line[0], line[1]);
            System.out.println(keystores);
        }


        Scanner scanner = new Scanner(System.in);

        String username = scanner.nextLine();

    }
}
