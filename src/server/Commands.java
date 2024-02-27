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

    public String execute(String[] command, String[] userdata, int phase){
        File root = new File("./hospitaldatabase/Departments");
        switch(command[0]){
            case "read":
                File file = findFile(command[1], root);
                if(file != null) {
                    try{
                        Scanner scan = new Scanner(file);
                        String[] personel = scan.nextLine().trim().split(" ");
                        if(personel[0].compareTo(userdata[0]) == 1 || userdata[2].compareTo(personel[3]) == 1) {
                            List<String> fileLines = Files.readAllLines(file.toPath());
                            String fileContent = String.join("\n", fileLines);
                            scan.close();
                            return fileContent;
                        }
                        scan.close();
                        return "Read failed due to insufficient acces";
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                return "File not found";
            case "write":
                try{
                    File toExctract = findFile(command[1], root);
                    List<String> fileLines = Files.readAllLines(toExctract.toPath());
                    String fileContent = String.join("\n", fileLines);
                    return fileContent;
                } catch(IOException e) {
                    e.printStackTrace();
                    return "Exception";
                }   

            case "delete":
                File exists = findFile(command[1], root);
                if (exists != null) {
                    exists.delete();
                    return "Delete successful";
                }
                return "Delete unsuccessful";
            case "create":
                File newRecord = new File(command[3]);
                try {    
                    FileWriter recordtext = new FileWriter(newRecord);
                    recordtext.write(command[1] + " " + command[2] + " " + userdata[0] + " " + userdata[2]);
                    recordtext.close();
                    return "creation successful";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Creation unsuccessful";
                }

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
                        if(personel[0].compareTo(userdata[0]) == 1 || userdata[2].compareTo(personel[3]) == 1) {
                            menu += String.format("* %d - %s\n", i+1, fileList[i].getName());
                        }
                        scan.close();
                        return menu;
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

            return menu;
            
            default:
            return "No such command";
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

    public void writeToFile(String editedText, String fileName) {
        File root = new File("./hospitaldatabase/Departments");
        File toEdit = findFile(fileName, root);
        try {    
            FileWriter writer = new FileWriter(toEdit);
            writer.write(editedText);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

