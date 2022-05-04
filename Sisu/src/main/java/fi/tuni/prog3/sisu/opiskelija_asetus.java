package fi.tuni.prog3.sisu;

import org.json.JSONObject;  
import org.json.JSONArray;  
import java.io.FileWriter;
import java.io.IOException;

public class opiskelija_asetus {

    
    public bool addStudent(String studentNumber, String name) {
        
        
        String fileData = "src/students.json";
        JSONArray students = new JSONArray(fileData);
        
        for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
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
        
        students.add(student);
        try {
            FileWriter file = new FileWriter("src/students.json", false);
            file.write(students.toJSONString());
            file.close();
            return true;
        }
        
        catch (IOException e) {
         return false;
      }
    }
    
    public bool editStudentData(String studentNumber, tehdytkurssitiedot, suunnitellut kurssit) {
        String fileData = "src/students.json";
        JSONArray students = new JSONArray(fileData);
        
        for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
                
            }
        }
    }
    public JSONArray getStudentCourses(String studentNumber) {
        String fileData = "src/students.json";
        JSONArray students = new JSONArray(fileData);
        
        for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            if (studentNumber == studentInfo.getString("studentnumber")) {
                JSONArray studentDone = studentInfo.getJSONArray("coursesdone");
                JSONArray studentPlan = studentInfo.getJSONArray("courseplans");
                JSONArray returnable = new JSONArray();
                returnable.add(studentDone);
                returnable.add(studentPlan);
                return returnable;
            }
        }
        JSONArray noData = new JSONArray();
        return noData;
    }
    public JSONArray getStudents(){
        String fileData = "src/students.json";
        JSONArray students = new JSONArray(fileData);
        if (students.length() == 0) {
            JSONArray empty = new JSONArray();
            return empty;
        }
        else {
            for (int i=0; i < students.length(); i++) {
            JSONObject studentInfo = students.getJSONObject(i);
            JSONArray numbersNames = new JSONArray();
            JSONObject student = new JSONObject();
            String number = studentInfo.getString("studentnumber");
            String name = studentInfo.getString("name");
            
            student.put("studentnumber", number);
            student.put("name", name);
            
            numbersNames.add(student);
            return numbersNames;
        }
    }

}
