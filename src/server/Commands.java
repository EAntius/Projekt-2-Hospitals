package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

/*Describes what should happen based on which command was sent from client */
public class Commands {
    private PrintWriter out;
    private BufferedReader in;

    public Commands(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void execute(String[] command, String fileName, String[] userdata){
        File root = new File("./src/hospitaldatabase/Departments");
        switch(command[0]){
            case "read":
                File file = findFile(fileName, root);
                if(file != null) {
                    try{
                        List<String> fileLines = Files.readAllLines(file.toPath());
                        String fileContent = String.join("\n", fileLines);
                        out.write(fileContent);
                    } catch(IOException e) {
                        
                    }
                }
            break;
            case "write":
                /*Send file to client, recieve file and update old file */
               

            break;

            case "delete":

            break;
            case "create":

            break;

            case "available":

            break;

            case "ls":
            String menu = "** Files **\n";
            if(command[1] != null) {
                String path = root.getPath() + command[1];
                File file2 = new File(path);
                File[] fileList = file2.listFiles();
                for(int i = 0; i < fileList.length; i++) {
                    try {    
                        Scanner scan = new Scanner(fileList[i]);
                        String[] personel = scan.nextLine().trim().split(" ");
                        if(true) {
                            menu += String.format("* %d - %s\n", i+1, fileList[i].getName());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                File[] fileList = root.listFiles();
                for(int i = 0; i < fileList.length; i++) {
                    menu += String.format("* %d - %s\n", i+1, fileList[i].getName());
                }
            }

            
            break;
            
            default:

            break;
        }
    }

    /*The first time, file is set to "Departments" */
    public File findFile(String name, File file) {
        File[] list = file.listFiles();
        if(list != null) {
            for(File f : list) {
                if(file.isDirectory()) {
                    findFile(name, f);
                } else if(name.equals(f.getName())) {
                    return f;
                }
            }
        }
        return null;
        
    }
}

