package java.server;

public class Commands {
    String userRole;
    String userAttribute;

    public Commands(String role, String attribute) {
        userRole = role;
        userAttribute = attribute;
    }

    public void execute(String[] command){
        switch(command[0]){
            case "read":
                
            break;
            case "write":

            break;

            case "delete":

            break;
            case "create":

            break;

            case "available":

            break;
            default:

            break;
        }
    }
}

