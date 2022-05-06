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
        
        
       
        // This loops checks if added student already exists by iterating the
        // data from JSON-file
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                return false;
            }
        }
        
        JSONArray coursesDone = new JSONArray();
        JSONArray coursePlans = new JSONArray();
        JSONObject student = new JSONObject();
        
        // Initializing new student object with studentnumnber name and two
        // empty JSONArrays for planned and completed courses
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
        // Iterating throught student data to find the right student
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray courseData = studentInfo.getJSONArray("courseplans");
                courseData.put(courseName);
                // when right student is found remove the old plan array
                // and replace it with new one with added course
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
        // Iterating throught student data to find the right student
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray courseData = studentInfo.getJSONArray("coursesdone");
                courseData.put(courseName);
                // when right student is found remove the old completed array
                // and replace it with new one with added course
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
        // Iterating throught student data to find the right student
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                JSONArray courseData = studentInfo.getJSONArray("courseplans");
                // Iterating the right array to remove given course
                for (int a=0; a < courseData.length(); a++) {
                    if (courseName.equals(courseData.getString(a))) {
                        // remve right course
                        courseData.remove(a);
                        // after removing the right course remove whole array
                        // and replace it with new one with out the removed course
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
        // Iterating throught student data to find the right student
        for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            if (studentNumber.equals(studentInfo.getString("studentnumber"))) {
                // adding the found courses to new arrays
                JSONArray studentDone = studentInfo.getJSONArray("coursesdone");
                JSONArray studentPlan = studentInfo.getJSONArray("courseplans");
                JSONArray returnable = new JSONArray();
                returnable.put(studentDone);
                returnable.put(studentPlan);
                // returning one array containgin two course arrays
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
            // if no students have been added return empty treemap
            return studentMap;
        }
        else {
            // Iterating through student data and extracting all the names and
            // student numbers
            for (int i=0; i < studentData.length(); i++) {
            JSONObject studentInfo = studentData.getJSONObject(i);
            
            String number = studentInfo.getString("studentnumber");
            String name = studentInfo.getString("name");
            // adding extracted names and student numbers to treemap 
            // student number as key and name as value
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
            // Writing the edited student data back to the JSON-file
            FileWriter file = new FileWriter("src/students.json", false);
            file.write(studentData.toString());
            file.close();
        }
        
        catch (IOException e) {
        }
    }
}
