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
import java.util.TreeMap;
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

    public JSONArray getStudentData() {
        return studentData;
    }
    public boolean addStudent(String studentNumber, String name) {
        
        
       
        
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
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
        
        studentData.put(student);
        return true;
        
    }
    public void addPlannedCourse(String studentNumber, String courseName) {
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
                JSONArray courseData = studentInfo.getJSONArray("courseplans");
                courseData.put(courseName);
                studentInfo.remove("courseplans");
                studentInfo.put("courseplans", courseData);
                studentData.remove(i);
                studentData.put(studentInfo);
            }
        }
    }
    
    public void addDoneCourse(String studentNumber, String courseName) {
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
                JSONArray courseData = studentInfo.getJSONArray("coursesdone");
                courseData.put(courseName);
                studentInfo.remove("coursesdone");
                studentInfo.put("coursesdone", courseData);
                studentData.remove(i);
                studentData.put(studentInfo);
                return;
            }
        }
    }
    
    public void removePlannedCourse (String studentNumber, String courseName) {
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray courseData = studentInfo.getJSONArray("courseplans");
                for (int a=0; a < courseData.length(); a++) {
                    if (courseName.equals(courseData.getString(a))) {
                        courseData.remove(a);
                        studentInfo.remove("courseplans");
                        studentInfo.put("courseplans", courseData);
                        studentData.remove(i);
                        studentData.put(studentInfo);
                        return;
                    }
                }
            }
        }
    }
    
    public JSONArray getStudentCourses(String studentNumber) {
        
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray studentDone = studentInfo.getJSONArray("coursesdone");
                JSONArray studentPlan = studentInfo.getJSONArray("courseplans");
                JSONArray returnable = new JSONArray();
                returnable.put(studentDone);
                returnable.put(studentPlan);
                return returnable;
            }
        }
            
        
        JSONArray noData = new JSONArray();
            return noData;
    }
    public TreeMap<String, String> getStudents(){
        
        JSONArray empty = new JSONArray();
        TreeMap<String, String> studentMap = new TreeMap<>();
        if (studentData.length() == 0) {
            
            return studentMap;
        }
        else {
            JSONArray numbersNames = new JSONArray();
            for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            
            
            
            JSONObject student = new JSONObject();
            String number = studentInfo.getString("studentnumber");
            String name = studentInfo.getString("name");
            
            student.put("studentnumber", number);
            student.put("name", name);
            
            studentMap.put(number, name);
            
            numbersNames.put(student);
            
            }
            return studentMap;
        }
    }
    public void saveData() {
        try {
            FileWriter file = new FileWriter("src/students.json", false);
            file.write(studentData.toString());
            file.close();
        }
        
        catch (IOException e) {
        }
    }
}
