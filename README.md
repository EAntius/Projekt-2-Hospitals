# Projekt-2-Hospitals

Compiling and running programs:


Client:
cd src/client
javac -d compiled/ *.java
java -cp compiled/ client.application


Server:
cd src/server
javac -d compiled/ *.java
java -cp compiled/ server.server



Commands:

read <FILENAME>
write <FILENAME>
delete <FILENAME>
create <PATIENT_NAME> <NURSE_NAME> <FILENAME>
ls <optional - DEPARTMENT_NAME>


Role departments: (in user.txt)
Patient - 0
Government body - 1
Nurse/Doctor - DEPARTMENT_NAME