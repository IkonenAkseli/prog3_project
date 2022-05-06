package fi.tuni.prog3.sisu;

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


public class App extends Application{
    
    

    @Override
    public void start(Stage stage) throws IOException {
        

        var button = new Button("Sisään SISUun");
        stage.setTitle("Melkein parempi SISU");
        
        // lisätään nappula opiskelijan valinnalle
        Button btnOpiskelija = new Button();
        btnOpiskelija.setText("Opiskelija");
        
        OpiskelijaAsetus studentHelper = new OpiskelijaAsetus();
        
        
        
        
        
        // Talletetaan kaikki opintosuunnat tanne
        HashMap<String, JSONObject> programDataMap = new HashMap<>();
        Button btnClose = new Button(); 
        btnClose.setText("Sulje"); 
        btnClose.setOnAction( e -> {
            studentHelper.saveData();
            stage.close(); 
                });
        
        VBox startBox = new VBox(button, btnOpiskelija, btnClose);
        startBox.setAlignment(Pos.CENTER);
        startBox.setSpacing(10);
        
        var scene = new Scene(startBox, 1280, 720);
        
        
        HandleApi apiHandler = new HandleApi();
        
        ComboBox comboBox = apiHandler.getInitialData(programDataMap);
        comboBox.setPromptText("Valitse tutkinto-ohjelma");
        
        //treeView.setRoot(rootItem);
        
        //ArrayList<TreeItem<String>> firstChildren = getInitialData();
        /*
        firstChildren.forEach(item -> {
            rootItem.getChildren().add(item);
        });
        */
        Button btnBack = new Button("Takaisin");
        
        
        
        // Valitaan
        comboBox.setOnAction(new EventHandler() {
            @Override
            public void handle(Event e) {
                String clickedName = comboBox.getValue().toString();
                var jsonObj = programDataMap.get(clickedName);
                
                //System.out.println(jsonObj);
                
                String groupId = jsonObj.getString("groupId");
                String name = jsonObj.getString("name");
                
                // Asetetaan osoite, josta lahdetaan hakemaan
                String address = "https://sis-tuni.funidata.fi/kori/api/modules/"
                        + "by-group-id?groupId=" + groupId
                        + "&universityId=tuni-university-root-id";
                
                
                try {
                    TreeItem<String> rootItem = new TreeItem<>("Tutkinto-ohjelma");
                    JSONArray jsonArr = new JSONArray(apiHandler.getApiData(address));
                    
                    rootItem.setValue(name);
                    
                    try {
                        // Luodaan treeview, VBox ja scene, asetetaan scene
                        // nakymaan
                        TreeView treeView = new TreeView();
                        // Haetaan koko rakenne
                        apiHandler.getStructureData(jsonArr, rootItem);
                        //moduleInfo(jsonArr, rootItem);
                        VBox treeBox = new VBox();
                        Button btnCloseScene3 = new Button();
                        btnCloseScene3.setText("Sulje"); 
                        btnCloseScene3.setOnAction( e3 -> {
                            studentHelper.saveData();
                            stage.close(); 
                                });
                        
                        Scene scene3 = new Scene(treeBox, 1280, 720);
                        
                        // Printtaa kurssin nimen kun sita klikkaa
                        treeView.getSelectionModel().selectedItemProperty()
                                .addListener((v, oldValue, newValue) -> {
                                if (newValue != null){
                                    String courseName = getTreeItemName(newValue.toString());
                                    HashMap<String, Course> allCourses = apiHandler.getAllCourses();
                                    
                                    if (allCourses.containsKey(courseName)){
                                        System.out.println(courseName);
                                        
                                        Label nameLabel = new Label(courseName);
                                        
                                        Button btnAddCompleted = new Button("Lisää suoritus");
                                        Button btnRemoveCompleted = new Button("Poista suoritus");
                                        Button btnAddPlan = new Button("Lisää suunnitelmaan");
                                        Button btnRemovePlan = new Button("Poista suunnitelmasta");
                                        Button btnBack = new Button("Takaisin");
                                        
                                        btnBack.setOnAction(et -> {
                                            stage.setScene(scene3);
                                        });
                                        
                                        
                                        VBox courseBox = new VBox(nameLabel, btnAddCompleted, btnRemoveCompleted, btnAddPlan, btnRemovePlan, btnBack);
                                        courseBox.setAlignment(Pos.CENTER);
                                        courseBox.setSpacing(10);
                                        
                                        var courseScene = new Scene(courseBox, 1280, 720);
                                        stage.setScene(courseScene);
                                    }
                                }
                                });
                        
                        
                        
                        
        
                        
                        
                        treeView.setRoot(rootItem);
                        treeBox.getChildren().add(treeView);
                        treeBox.getChildren().addAll(btnBack, btnCloseScene3);
                        
                        
                        
                        stage.setScene(scene3);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        TextField stuNumber = new TextField("Opiskelijanumero");
        TextField stuName = new TextField("Nimi");
        Button addStu = new Button();
        addStu.setText("Sisään");
        Button closeStu = new Button();
        closeStu.setText("Takaisin");
        
        addStu.setOnAction((ActionEvent ehs) -> {
            String number = stuNumber.getText();
            String name = stuName.getText();
            
            String coursesStr = studentHelper.getStudentCourses("K244522").get(0).toString();
            JSONArray coursesArr = new JSONArray(coursesStr);
            
            for (var x: coursesArr){
                System.out.println(x.toString());
            }
            
            
            
            
            TreeMap<String,String> studentNumbers = studentHelper.getStudents();
            
            if(name == null || name.length() == 0 || name.equals("Nimi")
                    || number == null ||number.length() == 0){
                return;
            }
            else if(!studentNumbers.containsKey(number)){
                studentHelper.addStudent(number, name);
            }
            else if(!studentNumbers.get(number).equals(name)){
                return;
            }
            else {
                
            }
            
        });
        
        
        GridPane gridPaneStu = new GridPane();
        gridPaneStu.setHgap(20);
        gridPaneStu.add(stuNumber, 0, 0);
        gridPaneStu.add(stuName, 0, 1);
        gridPaneStu.add(addStu, 1, 1);
        gridPaneStu.add(closeStu, 1, 2);
        
        gridPaneStu.setAlignment(Pos.CENTER);
        
        Scene sceneStu = new Scene (gridPaneStu, 1280, 720);
        closeStu.setOnAction(eh -> {
            studentHelper.saveData();
            stage.close(); 
                });
        
        Button btnCloseScene2 = new Button();
        btnCloseScene2.setText("Sulje"); 
        btnCloseScene2.setOnAction( e -> stage.close() );
        
        // Rajaus checkboxit
        
        CheckBox bachDegreeCB = new CheckBox("Kandidaatin tutkinto");
        CheckBox mastDegreeCB = new CheckBox("Maisterin tutkinto");
        CheckBox doctDegreeCB = new CheckBox("Tohtorin/Lisensiaatin tutkinto");
        CheckBox miscDegreeCB = new CheckBox("Erikoislääkäri koulutus");
        
        Button btnUpdate = new Button("Päivitä");
        btnUpdate.setOnAction(e -> updateComboBox(comboBox,
                                                  bachDegreeCB.isSelected(), 
                                                  mastDegreeCB.isSelected(),
                                                  doctDegreeCB.isSelected(),
                                                  miscDegreeCB.isSelected(),
                                                  apiHandler,
                                                  programDataMap));
        
        
        VBox rootBox = new VBox();
        rootBox.getChildren().addAll(comboBox, btnCloseScene2);
        
        GridPane gridPaneS2 = new GridPane();
        gridPaneS2.setHgap(20);
        gridPaneS2.add(comboBox, 0, 0);
        gridPaneS2.add(btnCloseScene2, 0, 1);
        gridPaneS2.add(bachDegreeCB, 1, 0);
        gridPaneS2.add(mastDegreeCB, 1, 1);
        gridPaneS2.add(doctDegreeCB, 1, 2);
        gridPaneS2.add(miscDegreeCB, 1, 3);
        gridPaneS2.add(btnUpdate, 1, 4);
        
        
        gridPaneS2.setAlignment(Pos.CENTER);
        
        Scene scene2 = new Scene(gridPaneS2, 1280, 720);
        
        // Takaisin valitsemaan tutkinto-ohjelmaa
        btnBack.setOnAction((ActionEvent e) -> {
            stage.setScene(scene2);
            
        });
        
        btnOpiskelija.setOnAction(eh -> stage.setScene(sceneStu));
        button.setOnAction(e -> stage.setScene(scene2));
        
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void updateComboBox(ComboBox comboBox,
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
                comboBox.getItems().add(set.getKey());
            }
    }

    private String getTreeItemName(String item){
        
        String[] lines = item.split("[\r\n]+");
        
        String wanted = lines[0];
        
        StringBuilder sb = new StringBuilder(wanted);
        
        sb.delete(0,18);
        
        return sb.toString();
    }
    
    
}