package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


/*Describes what should happen based on which command was sent from client */
public class Commands {
    private File root = new File("hospitaldatabase/Departments/");

    public String execute(String[] command, String[] userdata) throws FileNotFoundException{

        switch(command[0]){
            case "read":
                File file = findFile(command[1], root);
                if(file != null) {
                    try{
                        Scanner scan = new Scanner(file);
                        String[] personel = scan.nextLine().trim().split(" ");
                        if(((userdata[1].equals("Patient")) && (personel[0].equals(userdata[0]) || userdata[2].equals(personel[3])))
                        || userdata[1].equals("GovernmentBody") 
                        || ((userdata[1].equals("Nurse") || userdata[1].equals("Doctor"))) && personel[3].equals(userdata[2])) {
                            List<String> fileLines = Files.readAllLines(file.toPath());
                            fileLines.remove(0);
                            String fileContent = String.join("\n", fileLines);
                            scan.close();
                            return fileContent;
                        }
                        scan.close();
                        return "Read failed due to insufficient access";
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                return "File not found";
            case "write":
                try{
                    File toExctract = findFile(command[1], root);
                    List<String> fileLines = Files.readAllLines(toExctract.toPath());
                    fileLines.remove(0);
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
                File newRecord = new File(root +"/"+ userdata[2] + "/" + command[3]);
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
                if(command.length != 1) {
                    String path = root.getPath() + "/" + command[1];
                    File[] fileList = new File(path).listFiles();
                    for(int i = 0; i < fileList.length; i++) {
                        Scanner scan = new Scanner(fileList[i]);
                        String[] personel = scan.nextLine().trim().split(" ");
                        if(personel[0].equals(userdata[0]) ||
                            userdata[1].equals("Doctor") ||
                            userdata[1].equals("Nurse") ||
                            userdata[1].equals("GovernmentBody")) {
                            menu += String.format("* - %s\n", fileList[i].getName());
                        }
                        scan.close();
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

    public static void audit(String command, String fileName, String[] userdata, boolean pass) {
        File auditFile = new File("hospitaldatabase/auditing.txt");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now(); 

        try {
            FileWriter auditer = new FileWriter(auditFile, true);
            if(fileName != null) {
                auditer.write("\n" + command + "                "+ fileName +"                " + userdata[0] +  "                " + userdata[1] + "                " + userdata[2] + "                " + dtf.format(now) + "                " + pass);

            } else {
                auditer.write("\n" + command + "                "+ "N/A" +"                " + userdata[0] +  "                " + userdata[1] + "                " + userdata[2] + "                " + dtf.format(now) + "                " + pass);
            }
            auditer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static File findFile(String fileName, File directory) {
        // Check if the given directory is valid
        if (!directory.isDirectory()) {
            return null;
        }

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    File foundFile = findFile(fileName, file);
                    if (foundFile != null) {
                        return foundFile;
                    }
                } else if (file.getName().equals(fileName)) {
                    // File found
                    return file;
                }
            }
        }

        // File not found in the current directory or its subdirectories
        return null;
    }

    public String writeToFile(String editedText, String fileName, String userdata) {
        File start = new File(root +"/"+ userdata);
        File toEdit = findFile(fileName, start);
        if(toEdit == null) {
           return "No such file";
        }
        try {
            BufferedReader existingText = new BufferedReader(new FileReader(toEdit));
            String header = existingText.readLine();
            FileWriter writer = new FileWriter(toEdit);
            writer.write(header + "\n" + editedText);
            writer.close();
            existingText.close();
            return "Success";
        }catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}

