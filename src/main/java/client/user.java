package java.client;

import java.util.*;
import java.io.*;

public class user {
    String name;
    String password;
    String role;
    String department;

    public user(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
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
    public void setDepartment(String department) {
        this.department = department;
    }
}

