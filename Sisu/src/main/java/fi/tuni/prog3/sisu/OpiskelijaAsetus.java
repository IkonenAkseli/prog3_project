package fi.tuni.prog3.sisu;

import org.json.JSONObject;  
import org.json.JSONArray;  
import java.io.FileWriter;
import java.io.IOException;
/*import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils; */
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpiskelijaAsetus {
    
    private JSONArray studentData;
    
    
    public OpiskelijaAsetus(){
        Path path = Paths.get("src/students.json");
        String jsonString = "";
        try {
            jsonString = Files.readString(path);
            studentData = new JSONArray(jsonString);
        } catch (IOException ex) {
            Logger.getLogger(OpiskelijaAsetus.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }

    
    public boolean addStudent(String studentNumber, String name) {
        
        
        String fileData = "src/students.json";
        Path path = Paths.get("src/students.json");
        String jsonString = "";
        try {
            jsonString = Files.readString(path);
        } catch (IOException ex) {
            Logger.getLogger(OpiskelijaAsetus.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        JSONArray students = new JSONArray(jsonString);
        
        for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                return false;
            }
        }
        
        JSONArray coursesDone = new JSONArray();
        JSONArray coursePlans = new JSONArray();
        JSONObject student = new JSONObject();
        
        student.put("studentnumber", studentNumber);
        student.put("name", name);
        student.put("coursesdone", coursesDone);
        student.put("courseplans", coursePlans);
        
        students.put(student);
        try {
            FileWriter file = new FileWriter("src/students.json", false);
            file.write(students.toString());
            file.close();
            return true;
        }
        
        catch (IOException e) {
         return false;
      }
    }
    /*
    public boolean editStudentData(String studentNumber, JSONArray tehdytkurssitiedot, suunnitellutKurssit) {
        String fileData = "src/students.json";
        JSONArray students = new JSONArray(fileData);
        
        for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
                
            }
        }
        return true;
    }*/
    public JSONArray getStudentCourses(String studentNumber) {
        try {
            String fileData = "src/students.json";
            Path path = Paths.get("src/students.json");
            String jsonString = Files.readString(path);
            JSONArray students = new JSONArray(jsonString);
            
            for (int i=0; i < students.length(); i++) {
                JSONObject studentInfo = students.getJSONObject(i);
                if (studentNumber == studentInfo.getString("studentnumber")) {
                    JSONArray studentDone = studentInfo.getJSONArray("coursesdone");
                    JSONArray studentPlan = studentInfo.getJSONArray("courseplans");
                    JSONArray returnable = new JSONArray();
                    returnable.put(studentDone);
                    returnable.put(studentPlan);
                    return returnable;
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OpiskelijaAsetus.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONArray noData = new JSONArray();
            return noData;
    }
    public JSONArray getStudents(){
        String fileData = "src/students.json";
        Path path = Paths.get("src/students.json");
        
        String jsonString = "";
        try {
            jsonString = Files.readString(path);
        } catch (IOException ex) {
            Logger.getLogger(OpiskelijaAsetus.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        JSONArray students = new JSONArray(jsonString);
        JSONArray empty = new JSONArray();
        if (students.length() == 0) {
            
            return empty;
        }
        else {
            JSONArray numbersNames = new JSONArray();
            for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            
            JSONObject student = new JSONObject();
            String number = studentInfo.getString("studentnumber");
            String name = studentInfo.getString("name");
            
            student.put("studentnumber", number);
            student.put("name", name);
            
            numbersNames.put(student);
            
            }
            return numbersNames;
        }
    }
}
