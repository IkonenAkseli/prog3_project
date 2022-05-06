package fi.tuni.prog3.sisu;

import org.json.JSONObject;  
import org.json.JSONArray;  
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * class that handles writing and reading the JSON-file 
 * with student data in it.
 */

public class OpiskelijaAsetus {
    
    // Variable where student data is stored while the program is runnig.
    private JSONArray studentData;
    
    /**
     * Reads the JSON-file where student data is stored and saves it into 
     * studentData variable for the duration of programs running.
     */
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
    
    /**
     * @return studentData varibale that contains all the student data
     */
    public JSONArray getStudentData() {
        return studentData;
    }
    
    /**
     * initializes new student object and adds it into studentData variable
     * @param studentNumber user given variably which has his student number
     * @param name user given variable which has his name
     * @return true/false depending if the student already exists, flase if does
     * true if doesn't
     */
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
    
    /**
     * Adds new course into student's planned courses
     * @param studentNumber user given variable which he gave during login in
     * @param courseName course that was chosen by user
     */
    public void addPlannedCourse(String studentNumber, String courseName) {
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray courseData = studentInfo.getJSONArray("courseplans");
                courseData.put(courseName);
                studentInfo.remove("courseplans");
                studentInfo.put("courseplans", courseData);
                studentData.remove(i);
                studentData.put(studentInfo);
            }
        }
    }
    
    /**
     * Adds new course into student's copleted courses only can be add while
     * logged in as teacher
     * @param studentNumber student's number that teacher chose while login in 
     * @param courseName  course that teacher chose to mark as completed
     */
    public void addDoneCourse(String studentNumber, String courseName) {
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
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
    
    /**
     * Removes chosen course from stundent's planned courses
     * @param studentNumber user given variable which he gave during login in
     * @param courseName course that was chosen by user
     */
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
    
    /**
     * Finds the given student from studentData variable and gets his planned 
     * and completed courses into two new lists
     * @param studentNumber user given variable which he gave during login in
     * @return JSONArray which has two JSONArrays inside one for plannedcourses
     * and other for completed courses
     */
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
    
    /**
     * Gets all the student into treemap student number as key and name as value
     * @return counstructed treemap
     */
    public TreeMap<String, String> getStudents(){
        
        TreeMap<String, String> studentMap = new TreeMap<>();
        if (studentData.length() == 0) {
            
            return studentMap;
        }
        else {
            JSONArray numbersNames = new JSONArray();
            for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            
            String number = studentInfo.getString("studentnumber");
            String name = studentInfo.getString("name");
            
            studentMap.put(number, name);
            
            }
            return studentMap;
        }
    }
    
    /**
     * When program is closed writes all the changes into the JSON-file
     */
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
