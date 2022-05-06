package fi.tuni.prog3.sisu;

/**
 * Main class for running the gui
 */


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class App extends Application{
    
    

    /**
     * Starts the application
     * @param stage
     * @throws IOException 
     */
    @Override
    public void start(Stage stage) throws IOException {
        

        // Buttons for the starting scene
        
        stage.setTitle("Melkein parempi SISU");
        
        
        Button btnStudent = new Button();
        btnStudent.setText("Opiskelija");
        
        Button btnTeacher = new Button("Opettaja");
        
        OpiskelijaAsetus studentHelper = new OpiskelijaAsetus();
        
        // Arrays for saving information from a lambda function
        
        ArrayList<String> coursesDone = new ArrayList<>();
        ArrayList<String> coursesPlanned = new ArrayList<>();
        
        
        ArrayList<String> currentStudent = new ArrayList<>();
        
        boolean[] isTeacher = new boolean[1];
        isTeacher[0] = false;
        
        
        
        
        
        
        
        // Map for storing all of the programmes
        HashMap<String, JSONObject> programDataMap = new HashMap<>();
        
        // More declarations for GUI
        Button btnClose = new Button(); 
        btnClose.setText("Sulje"); 
        btnClose.setOnAction( e -> {
            studentHelper.saveData();
            stage.close(); 
                });
        
        VBox startBox = new VBox( btnStudent, btnTeacher, btnClose);
        startBox.setAlignment(Pos.CENTER);
        startBox.setSpacing(10);
        
        var scene = new Scene(startBox, 1280, 720);
        
        // Object for accessing HandleApi class
        HandleApi apiHandler = new HandleApi();
        
        ComboBox comboBox = apiHandler.getInitialData(programDataMap);
        comboBox.setPromptText("Valitse tutkinto-ohjelma");
        
        Label studentCredits = new Label();
        ArrayList<Integer> credits = new ArrayList<>();
        
        
        Button btnBack = new Button("Takaisin");
        
        ListView completedCourses = new ListView();
        ListView plannedCourses = new ListView();
        
        // Logic for handling a user click on a programme
        comboBox.setOnAction(new EventHandler() {
            @Override
            public void handle(Event e) {
                
                // Get the name of the programme and fetch it's information
                // from the data map
                String clickedName = "";
                if(!comboBox.getSelectionModel().isEmpty()){
                    clickedName = comboBox.getValue().toString();
                }
                else{
                    return;
                }
                var jsonObj = programDataMap.get(clickedName);
                
                
                
                String groupId = jsonObj.getString("groupId");
                String name = jsonObj.getString("name");
                
                // Form the correct address for starting the recursive search
                String address = "https://sis-tuni.funidata.fi/kori/api/modules/"
                        + "by-group-id?groupId=" + groupId
                        + "&universityId=tuni-university-root-id";
                
                
                try {
                    // Get the JSONArray of the selected programme
                    TreeItem<String> rootItem = new TreeItem<>("Tutkinto-ohjelma");
                    JSONArray jsonArr = new JSONArray(apiHandler.getApiData(address));
                    
                    rootItem.setValue(name);
                    
                    try {
                        
                        TreeView treeView = new TreeView();
                        
                        // Call the recursive search for rest of the data
                        apiHandler.getStructureData(jsonArr, rootItem);
                        
                        // Close button
                        Button btnCloseScene3 = new Button();
                        btnCloseScene3.setText("Sulje"); 
                        btnCloseScene3.setOnAction( e3 -> {
                            studentHelper.saveData();
                            stage.close(); 
                                });
                        
                        GridPane gridPaneTree = new GridPane();
                        gridPaneTree.setHgap(20);
                        
                        

                        gridPaneTree.setAlignment(Pos.CENTER);
                        
                        Scene scene3 = new Scene(gridPaneTree, 1280, 720);
                        
                        // A listener for when an item in the treeview is clicked
                        treeView.getSelectionModel().selectedItemProperty()
                                .addListener((v, oldValue, newValue) -> {
                                if (newValue != null){
                                    String courseName = getTreeItemName(newValue.toString());
                                    HashMap<String, Course> allCourses = apiHandler.getAllCourses();
                                    
                                    // Only move forward if the clicked item was
                                    // a course
                                    if (allCourses.containsKey(courseName)){
                                        System.out.println(courseName);
                                        
                                        Label nameLabel = new Label(courseName);
                                        
                                        Button btnAddCompleted = new Button("Lisää suoritus");
                                        
                                        Button btnAddPlan = new Button("Lisää suunnitelmaan");
                                        Button btnRemovePlan = new Button("Poista suunnitelmasta");
                                        Button btnBack = new Button("Takaisin");
                                        
                                        // Adding a course to plan
                                        btnAddPlan.setOnAction(addHandle ->{
                                            if (coursesPlanned.contains(courseName)){
                                                nameLabel.setText(courseName + "\nKurssi on jo suunnitelmassa!");
                                            }
                                            else {
                                                studentHelper.addPlannedCourse(currentStudent.get(0), courseName);
                                                coursesPlanned.add(courseName);
                                                nameLabel.setText(courseName + "\nLisätty suunnitelmaan!");
                                                plannedCourses.getItems().add(courseName);
                                            }
                                        });
                                        
                                        // Adding the course to completed courses
                                        btnAddCompleted.setOnAction(addHandle ->{
                                            
                                            if (coursesDone.contains(courseName)){
                                                nameLabel.setText(courseName + "\nKurssi on jo suorituksissa!");
                                            }
                                            else{
                                                studentHelper.addDoneCourse(currentStudent.get(0), courseName);
                                                coursesDone.add(courseName);
                                                nameLabel.setText(courseName + "\nLisätty suorituksiin!");
                                                completedCourses.getItems().add(courseName);
                                                
                                                // Remove the course from planned courses
                                                // since it was completed
                                                if(coursesPlanned.contains(courseName)){
                                                    coursesPlanned.remove(courseName);
                                                    studentHelper.removePlannedCourse(currentStudent.get(0), courseName);
                                                    
                                                    // Refresh planned courses list
                                                    plannedCourses.getItems().clear();
                                                
                                                    plannedCourses.getItems().add("Suunnitelma");

                                                    
                                                    for (var plannedCourse : coursesPlanned){
                                                        plannedCourses.getItems().add(plannedCourse);
                                                    }
                                                }
                                                
                                                // Add credits as well
                                                
                                                credits.add(apiHandler.getAllCourses().get(courseName).getCredits());
                                                
                                                int totalCredits = 0;
                        
                                                for (int instance : credits){
                                                    totalCredits += instance;
                                                }

                                                studentCredits.setText("Opintopisteet: " + Integer.toString(totalCredits));
                                                
                                            }
                                            
                                        });
                                        
                                        // Remove course from plan
                                        btnRemovePlan.setOnAction(addHandle ->{
                                            
                                            if (!coursesPlanned.contains(courseName)){
                                                nameLabel.setText(courseName + "\nKurssi ei ole suunnitelmassa!");
                                            }
                                            else{
                                                studentHelper.removePlannedCourse(currentStudent.get(0), courseName);
                                                coursesPlanned.remove(courseName);
                                                nameLabel.setText(courseName + "\nPoistettu suunnitelmasta!");
                                                
                                                // Refresh planned courses list
                                                plannedCourses.getItems().clear();
                                                
                                                plannedCourses.getItems().add("Suunnitelma");
                        
                                                for (var plannedCourse : coursesPlanned){
                                                    plannedCourses.getItems().add(plannedCourse);
                                                }
                                                
                                            }
                                            
                                        });
                                        
                                        btnBack.setOnAction(et -> {
                                            stage.setScene(scene3);
                                        });
                                        
                                        // Display options after clicking on a course
                                        // Only a teacher can add to the completed list
                                        VBox courseBox = new VBox(nameLabel);
                                        
                                        if(isTeacher[0]){
                                            courseBox.getChildren().add(btnAddCompleted);
                                        }
                                        
                                        courseBox.getChildren().addAll(btnAddPlan, btnRemovePlan, btnBack);
                                        
                                        courseBox.setAlignment(Pos.CENTER);
                                        courseBox.setSpacing(10);
                                        
                                        var courseScene = new Scene(courseBox, 1280, 720);
                                        stage.setScene(courseScene);
                                    }
                                }
                                });
                        
                        
                        // Create lists of planned and complete courses
                        completedCourses.getItems().add("Suoritukset");
                        
                        for (var completedCourse : coursesDone){
                            completedCourses.getItems().add(completedCourse);
                            if(apiHandler.getAllCourses().containsKey(completedCourse)){
                                credits.add(apiHandler.getAllCourses().get(completedCourse).getCredits());
                            }
                        }
                        
                        int totalCredits = 0;
                        
                        for (int instance : credits){
                            totalCredits += instance;
                        }
                        
                        studentCredits.setText("Opintopisteet: " + Integer.toString(totalCredits));
                        
                        plannedCourses.getItems().add("Suunnitelma");
                        
                        for (var plannedCourse : coursesPlanned){
                            plannedCourses.getItems().add(plannedCourse);
                        }
                        
                        
                        gridPaneTree.add(treeView, 0, 0);
                        treeView.setMinWidth(600);
                        treeView.setPrefWidth(700);
                        gridPaneTree.add(btnBack, 2, 0);
                        gridPaneTree.add(btnCloseScene3, 2, 1);
                        gridPaneTree.add(completedCourses, 1, 0);
                        gridPaneTree.add(plannedCourses, 1, 1);
                        gridPaneTree.add(studentCredits, 0, 1);
                        
                        
                        treeView.setRoot(rootItem);
                        
                        
                        
                        
                        stage.setScene(scene3);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Declaration for log-in GUI
        TextField stuNumber = new TextField();
        TextField stuName = new TextField();
        Button addStu = new Button();
        addStu.setText("Sisään");
        Button closeStu = new Button();
        closeStu.setText("Takaisin");
        Label stuLabel = new Label("Syötä tiedot:");
        
        stuNumber.setPromptText("Opiskelijanumero");
        stuName.setPromptText("Nimi");
        
        
        
        
        GridPane gridPaneStu = new GridPane();
        gridPaneStu.setHgap(20);
        gridPaneStu.add(stuLabel, 0, 0);
        gridPaneStu.add(stuNumber, 0, 1);
        gridPaneStu.add(stuName, 0, 2);
        gridPaneStu.add(addStu, 1, 1);
        gridPaneStu.add(closeStu, 1, 2);
        
        gridPaneStu.setAlignment(Pos.CENTER);
        
        Scene sceneStu = new Scene (gridPaneStu, 1280, 720);
        closeStu.setOnAction(eh -> {
            isTeacher[0] = false;
            stage.setScene(scene);
                });
        
        Button btnCloseScene2 = new Button();
        btnCloseScene2.setText("Sulje"); 
        btnCloseScene2.setOnAction( e -> {
            studentHelper.saveData();
            stage.close(); 
                });
        
        // Search box for programmes
        Text filterDegreesTFName = new Text("Rajaa tutkinto-ohjelmia");
        TextField filterDegreesTF = new TextField("");
		
        // Checkboxes for selecting only given types of programmes
        
        CheckBox bachDegreeCB = new CheckBox("Kandidaatin tutkinto");
        CheckBox mastDegreeCB = new CheckBox("Maisterin tutkinto");
        CheckBox doctDegreeCB = new CheckBox("Tohtorin/Lisensiaatin tutkinto");
        CheckBox miscDegreeCB = new CheckBox("Erikoislääkäri koulutus");
        
        Button btnUpdate = new Button("Päivitä");
        btnUpdate.setOnAction(e -> updateComboBox(comboBox,
                                                  filterDegreesTF.getText(),
                                                  bachDegreeCB.isSelected(), 
                                                  mastDegreeCB.isSelected(),
                                                  doctDegreeCB.isSelected(),
                                                  miscDegreeCB.isSelected(),
                                                  apiHandler,
                                                  programDataMap));
        
        // Button for getting out of programme interface,
        // Acts as logging out so clearing current user data from utility
        // arrays. Changes made will still be saved upon closing the program.
        Button btnToStart = new Button("Takaisin");
        btnToStart.setOnAction(backBtn -> {
            
            coursesDone.clear();
            coursesPlanned.clear();
            
            credits.clear();

            completedCourses.getItems().clear();
            plannedCourses.getItems().clear();
            
            currentStudent.clear();
            isTeacher[0] = false;
            
            stage.setScene(scene); 
        });
        
        // GUI declaration for programme view
        GridPane gridPaneS2 = new GridPane();
        gridPaneS2.setHgap(20);
        gridPaneS2.add(comboBox, 0, 0);
        gridPaneS2.add(btnCloseScene2, 0, 1);
        gridPaneS2.add(filterDegreesTFName, 1, 0);
        gridPaneS2.add(filterDegreesTF, 1, 1);
        gridPaneS2.add(bachDegreeCB, 1, 2);
        gridPaneS2.add(mastDegreeCB, 1, 3);
        gridPaneS2.add(doctDegreeCB, 1, 4);
        gridPaneS2.add(miscDegreeCB, 1, 5);
        gridPaneS2.add(btnUpdate, 1, 6);
        gridPaneS2.add(btnToStart, 0, 2);
        
        
        gridPaneS2.setAlignment(Pos.CENTER);
        
        Scene scene2 = new Scene(gridPaneS2, 1280, 720);
        
        // Back button
        btnBack.setOnAction((ActionEvent e) -> {
            comboBox.valueProperty().set("Valitse tutkinto-ohjelma");
            comboBox.setPromptText("Valitse tutkinto-ohjelma");
            stage.setScene(scene2);
            
        });
        
        btnStudent.setOnAction(eh -> stage.setScene(sceneStu));
        
        
        btnTeacher.setOnAction(btnTeacherEvent ->{
           isTeacher[0] = true;
           stage.setScene(sceneStu);
        });
        
        
        stage.setScene(scene);
        stage.show();
        
        // Fetches student data and makes it ready to be displayed in the
        // treeview. Creates new student if studentnumber doesn't exist.
        addStu.setOnAction((ActionEvent ehs) -> {
            String number = stuNumber.getText();
            String name = stuName.getText();

            TreeMap<String,String> studentNumbers = studentHelper.getStudents();
            
            
            // Logic to determine what to do.
            if(name == null || name.length() == 0 || name.equals("Nimi")
                    || number == null ||number.length() == 0){
                return;
            }
            else if(!studentNumbers.containsKey(number)){
                
                studentHelper.addStudent(number, name);
            }
            else if(!studentNumbers.get(number).equals(name)){
                stuLabel.setText("Opsikelijanumerolla on eri nimi");
                return;
            }
            else {
                
                
                String coursesStr = studentHelper.getStudentCourses(number).get(0).toString();
                JSONArray coursesArr = new JSONArray(coursesStr);
                
                
                for (var course: coursesArr){
                    String courseName = course.toString();
                    coursesDone.add(courseName);
                }
                String coursePlansStr = studentHelper.getStudentCourses(number).get(1).toString();
                JSONArray coursePlansArr = new JSONArray(coursePlansStr);
                
                for (var course: coursePlansArr){
                    String courseName = course.toString();
                    coursesPlanned.add(courseName);
                }
                
            }
            
            currentStudent.add(number);
            
            // Places null value to comboBox, no idea why not the prompt
            comboBox.valueProperty().set("Valitse tutkinto-ohjelma");
            comboBox.setPromptText("Valitse tutkinto-ohjelma");
            stage.setScene(scene2);
        });
    }

    public static void main(String[] args) {
        launch();
    }
    
    // For updating the box containing all the programmes
    // Updates the given combobox based on rules defined by given parameters.
    private void updateComboBox(ComboBox comboBox,
                                String searchterm,
                                boolean bachDegree, boolean mastDegree,
                                boolean doctDegree, boolean miscDegree,
                                HandleApi apiHandler,
                                HashMap<String, JSONObject> originalMap){
        TreeMap<String, JSONObject> filteredDataMap = new TreeMap<>();
        comboBox.getItems().clear();
        
        if ((!bachDegree && !mastDegree && !doctDegree && !miscDegree)){
            filteredDataMap.putAll(originalMap);
            
        }
        else{
            


            if (bachDegree){
                filteredDataMap.putAll(apiHandler.getBachDegrees());
            }
            if (mastDegree){
                filteredDataMap.putAll(apiHandler.getMastDegrees());
            }
            if (doctDegree){
                filteredDataMap.putAll(apiHandler.getDoctDegrees());
            }
            if (miscDegree){
                filteredDataMap.putAll(apiHandler.getMiscDegrees());
                }
            
        
        }
        for (Iterator<Map.Entry<String, JSONObject>> it = filteredDataMap.entrySet().iterator(); it.hasNext();) {
                var set = it.next();
                if (searchterm == ""){
                    comboBox.getItems().add(set.getKey());
                }
                else {
                    if (set.getKey().toLowerCase().contains(searchterm.toLowerCase())){
                        comboBox.getItems().add(set.getKey());
                }
            }
        }
        comboBox.setPromptText("Valitse tutkinto-ohjelma");
    }

    // A utility function for parsing the name from the string coming from
    // clicking the treeview
    private String getTreeItemName(String item){
        
        String[] lines = item.split("[\r\n]+");
        
        String wanted = lines[0];
        
        StringBuilder sb = new StringBuilder(wanted);
        if (sb.length() > 18){
            sb.delete(0,18);
        }
        
        
        return sb.toString();
    }
    
    
}