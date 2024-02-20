package java.client;

import java.util.*;
import java.io.*;

public class user {
    String name; //username
    String hashedpassword; //hashed version of incoming password
    String role; //Their role: patient, nurse, doctor, goverment body
    String attribute; //For patients, their birthcode. For nurse and doctors, their department. For goverment body, 0

    public user(String name, String hashedpassword, String role, String attribute) {
        this.name = name;
        this.hashedpassword = hashedpassword;
        this.role = role;
        this.attribute = attribute; 
    }

    public void read(String filename) {

    }   

    public void write(String filename) {

    }

    public void create(String filename) {

    }

    public void delete(String filename) {
        
    }
    public String filefinder(String filename) {
        
        return "";
    }
}

